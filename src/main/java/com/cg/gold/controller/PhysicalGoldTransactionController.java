package com.cg.gold.controller;

import java.time.LocalDateTime;

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

import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.entity.User;
import com.cg.gold.service.AddressService;
import com.cg.gold.service.PhysicalGoldTransactionService;
import com.cg.gold.service.UserService;
import com.cg.gold.service.VendorBranchService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/physical_gold_transactions")
public class PhysicalGoldTransactionController {

	@Autowired
	private PhysicalGoldTransactionService transactionService;

	@Autowired
	private VendorBranchService branchService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private UserService userService;

	@Autowired
	private Environment environment;

	@GetMapping("/home")
	public String showPhysicalGoldHome(Model model) {
		model.addAttribute("transaction", new PhysicalGoldTransaction());
		model.addAttribute("branches", branchService.getAllVendorBranches());
		model.addAttribute("addresses", addressService.getAllAddresses());
		return "physical-gold-home";
	}

	@PostMapping("/add")
	public String addTransaction(@ModelAttribute PhysicalGoldTransaction transaction, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			Integer userId = transaction.getUser().getUserId();
			Integer branchId = transaction.getBranch().getBranchId();
			Integer addressId = transaction.getDeliveryAddress().getAddressId();
			transaction.setUser(userService.getUserById(userId));
			transaction.setBranch(branchService.getVendorBranchByBranchId(branchId));
			transaction.setDeliveryAddress(addressService.getAddressById(addressId));
			transaction.setCreatedAt(LocalDateTime.now());
			transactionService.addPhysicalGoldTransaction(transaction);
			redirectAttributes.addFlashAttribute("message", "Physical Gold Transactions added successfully!");
			return "redirect:/api/v1/physical_gold_transactions/home";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping
	public String getAllTransactions(Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transactions", transactionService.getAllPhysicalGoldTransactions());
			return "physical-gold-list";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/search/byId")
	public String getById(@RequestParam Integer transactionId, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transaction", transactionService.getPhysicalGoldTransactionById(transactionId));
			return "physical-gold-by-id";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/search/byUser")
	public String getByUserId(@RequestParam Integer userId, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transactions", transactionService.getPhysicalGoldTransactionByUserId(userId));
			return "physical-gold-by-user";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/search/byBranch")
	public String getByBranch(@RequestParam Integer branchId, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transactions", transactionService.getPhysicalGoldTransactionByBranchId(branchId));
			model.addAttribute("branchId", branchId);
			return "physical-gold-by-branch";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/search/byDeliveryCity")
	public String getByCity(@RequestParam String city, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transactions", transactionService.getAllPhysicalGoldTransactionByDeliveryCity(city));
			model.addAttribute("city", city);
			return "physical-gold-by-city";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/search/byDeliveryState")
	public String getByState(@RequestParam String state, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transactions", transactionService.getAllPhysicalGoldTransactionByDeliveryState(state));
			model.addAttribute("state", state);
			return "physical-gold-by-state";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/updatePage/{transaction_id}")
	public String showUpdateTransactionForm(@PathVariable("transaction_id") Integer transactionId, Model model,
			HttpServletRequest request) {
		try {
			PhysicalGoldTransaction transaction = transactionService.getPhysicalGoldTransactionById(transactionId);
			model.addAttribute("transaction", transaction);
			model.addAttribute("branches", branchService.getAllVendorBranches());
			model.addAttribute("addresses", addressService.getAllAddresses());
			return "physical-gold-update";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@PostMapping("/update")
	public String updateTransactionForm(@ModelAttribute PhysicalGoldTransaction transaction, Model model,
			HttpServletRequest request) {
		try {
			Integer transactionId = transaction.getTransactionId();
			Integer branchId = transaction.getBranch().getBranchId();
			Integer addressId = transaction.getDeliveryAddress().getAddressId();

			User existingUser = userService.getUserById(transaction.getUser().getUserId());
			existingUser.setName(transaction.getUser().getName());
			userService.updateUser(existingUser.getUserId(), existingUser);

			transaction.setUser(existingUser);
			transaction.setBranch(branchService.getVendorBranchByBranchId(branchId));
			transaction.setDeliveryAddress(addressService.getAddressById(addressId));
			transactionService.updatePhysicalGoldTransaction(transactionId, transaction);
			return "redirect:/api/v1/physical_gold_transactions/home";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	private String handleError(Exception e, HttpServletRequest request, Model model) {
		model.addAttribute("errorMessage", environment.getProperty(e.getMessage()));
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("timestamp", LocalDateTime.now());
		return "physical-gold-error";
	}

}
