package com.cg.gold.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cg.gold.entity.Address;
import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.User;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.entity.VirtualGoldHolding;
import com.cg.gold.kafka.KafkaVirtualGoldProducer;
import com.cg.gold.service.AddressService;
import com.cg.gold.service.PhysicalGoldTransactionService;
import com.cg.gold.service.TransactionHistoryService;
import com.cg.gold.service.UserService;
import com.cg.gold.service.VendorBranchService;
import com.cg.gold.service.VendorService;
import com.cg.gold.service.VirtualGoldHoldingService;

@Controller
@RequestMapping("/api/v3")
public class DashboardController {

	@Autowired
	private UserService userService;

	@Autowired
	private VirtualGoldHoldingService virtualGoldService;

	@Autowired
	private PhysicalGoldTransactionService physicalGoldService;

	@Autowired
	private TransactionHistoryService transactionService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private VendorBranchService vendorBranchService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private KafkaVirtualGoldProducer kafkaVirtualGoldProducer;

	@Autowired
	private Environment environment;

	@GetMapping("/")
	public String getMainDashBoard() {
		return "main-dashboard";
	}

	@GetMapping("/user_dashboard_profile")
	public String loadUserProfile(@RequestParam(required = false) Integer userId, Model model) {
		model.addAttribute("allUsers", userService.getAllUsers());
		model.addAttribute("selectedUserId", userId);
		if (userId != null) {
			try {
				User user = userService.getUserById(userId);
				model.addAttribute("user", user);
				model.addAttribute("addresses", addressService.getAllAddresses());
				model.addAttribute("vendors", vendorService.getAllVendors());
				model.addAttribute("branches", vendorBranchService.getAllVendorBranches());
				model.addAttribute("virtualGold", userService.getTotalVirtualGoldHoldingsByUserId(userId));
				model.addAttribute("physicalGold", userService.getTotalPhysicalGoldHoldingsByUserId(userId));
				model.addAttribute("virtualHoldings", virtualGoldService.getAllVirtualGoldHoldingByUserId(userId));
				model.addAttribute("physicalHoldings", physicalGoldService.getPhysicalGoldTransactionByUserId(userId));
			} catch (Exception e) {
				model.addAttribute("profileMessage",
						"Some data could not be loaded: " + environment.getProperty(e.getMessage()));
			}
		}
		return "user-dashboard-profile";
	}

	@PostMapping("/users/update/{userId}")
	public String updateUserProfile(@PathVariable Integer userId, @ModelAttribute User user,
			RedirectAttributes redirectAttributes) {
		try {
			userService.updateUser(userId, user);
			redirectAttributes.addFlashAttribute("profileMessage", "User details updated successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("profileMessage",
					"Failed to update user: " + environment.getProperty(e.getMessage()));
		}
		return "redirect:/api/v3/user_dashboard_profile?userId=" + userId;
	}

	@PostMapping("/virtual_gold_holding/add")
	public String buyVirtualGold(@RequestParam Double quantity, @RequestParam Integer vendorId,
			@RequestParam Integer userId, RedirectAttributes redirectAttributes) {
		try {
			List<VendorBranch> branches = vendorBranchService.getVendorBranchesByVendorId(vendorId);
			VendorBranch branch = branches.isEmpty() ? null : branches.get(0);
			User user = userService.getUserById(userId);
			Double pricePerGram = branch.getVendor().getCurrentGoldPrice();
			Double totalAmount = quantity * pricePerGram;
			VirtualGoldHolding holding = new VirtualGoldHolding();
			holding.setUser(user);
			holding.setBranch(branch);
			holding.setQuantity(quantity);
			virtualGoldService.addVirtualGoldHolding(holding);
			// Kafka Integration for User
			kafkaVirtualGoldProducer.sendEvent(holding);
			userService.updateUserBalance(userId, user.getBalance() + totalAmount);
			TransactionHistory tx = new TransactionHistory();
			tx.setUser(user);
			tx.setBranch(branch);
			tx.setQuantity(quantity);
			tx.setAmount(totalAmount);
			tx.setTransactionType(TransactionHistory.TransactionType.Buy);
			tx.setTransactionStatus(TransactionHistory.TransactionStatus.Success);
			transactionService.addTransactionHistory(tx);
			redirectAttributes.addFlashAttribute("virtualMessage", "Virtual gold purchased successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("virtualMessage",
					"Failed to purchase virtual gold: " + environment.getProperty(e.getMessage()));
		}
		return "redirect:/api/v3/user_dashboard_profile?userId=" + userId;
	}

	@PostMapping("/virtual_gold_holding/convertToPhysical")
	public String convertToPhysical(@RequestParam Integer holdingId, @RequestParam Integer userId,
			RedirectAttributes redirectAttributes) {
		try {
			virtualGoldService.convertVirtualToPhysical(holdingId);
			VirtualGoldHolding holding = virtualGoldService.getVirtualGoldHoldingById(holdingId);
			TransactionHistory tx = new TransactionHistory();
			tx.setUser(holding.getUser());
			tx.setBranch(holding.getBranch());
			tx.setQuantity(holding.getQuantity());
			tx.setAmount(holding.getQuantity() * holding.getBranch().getVendor().getCurrentGoldPrice());
			tx.setTransactionType(TransactionHistory.TransactionType.ConvertToPhysical);
			tx.setTransactionStatus(TransactionHistory.TransactionStatus.Success);
			transactionService.addTransactionHistory(tx);
			redirectAttributes.addFlashAttribute("convertMessage", "Gold converted to physical successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("convertMessage",
					"Failed to convert gold: " + environment.getProperty(e.getMessage()));
		}
		return "redirect:/api/v3/user_dashboard_profile?userId=" + userId;
	}

	@PostMapping("/physical_gold_transactions/add")
	public String buyPhysicalGold(@RequestParam Double quantity, @RequestParam Integer branchId,
			@RequestParam Integer deliveryAddressId, @RequestParam Integer userId,
			RedirectAttributes redirectAttributes) {
		try {
			User user = userService.getUserById(userId);
			VendorBranch branch = vendorBranchService.getVendorBranchByBranchId(branchId);
			Address address = addressService.getAddressById(deliveryAddressId);
			Double totalAmount = quantity * branch.getVendor().getCurrentGoldPrice();
			PhysicalGoldTransaction tx = new PhysicalGoldTransaction();
			tx.setUser(user);
			tx.setBranch(branch);
			tx.setQuantity(quantity);
			tx.setDeliveryAddress(address);
			physicalGoldService.addPhysicalGoldTransaction(tx);
			userService.updateUserBalance(userId, user.getBalance() + totalAmount);
			TransactionHistory history = new TransactionHistory();
			history.setUser(user);
			history.setBranch(branch);
			history.setQuantity(quantity);
			history.setAmount(totalAmount);
			history.setTransactionType(TransactionHistory.TransactionType.Buy);
			history.setTransactionStatus(TransactionHistory.TransactionStatus.Success);
			transactionService.addTransactionHistory(history);
			redirectAttributes.addFlashAttribute("physicalMessage", "Physical gold purchased successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("physicalMessage",
					"Failed to purchase physical gold: " + environment.getProperty(e.getMessage()));
		}
		return "redirect:/api/v3/user_dashboard_profile?userId=" + userId;
	}

	@GetMapping("/user_dashboard_activity")
	public String loadUserActivity(@RequestParam(required = false) Integer userId, Model model) {
		model.addAttribute("allUsers", userService.getAllUsers());
		model.addAttribute("selectedUserId", userId);
		if (userId != null) {
			try {
				model.addAttribute("transactions", transactionService.getTransactionsByUserSorted(userId));
				model.addAttribute("payments", userService.getUserPayments(userId));
				model.addAttribute("vendors", vendorService.getAllVendors());
			} catch (Exception e) {
				model.addAttribute("activityMessage",
						"Some activity data could not be loaded: " + environment.getProperty(e.getMessage()));
			}
		}
		return "user-dashboard-activity";
	}
}
