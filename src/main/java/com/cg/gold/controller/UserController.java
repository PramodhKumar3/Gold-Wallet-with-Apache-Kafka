package com.cg.gold.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import com.cg.gold.entity.Address;
import com.cg.gold.entity.Payment;
import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.User;
import com.cg.gold.exception.AddressException;
import com.cg.gold.exception.UserException;
import com.cg.gold.service.AddressService;
import com.cg.gold.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private Environment environment;

	@GetMapping("/home")
	public String showHomePage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("addresses", addressService.getAllAddresses());
		return "home";
	}

//	@PostMapping("/add")
//	public String addUser(@ModelAttribute User user) {
//		userService.createUser(user);
//		return "redirect:/api/v1/users/home";
//	}

	@PostMapping("/add")
	public String addUser(@Valid @ModelAttribute User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("addresses", addressService.getAllAddresses());
			return "home";
		}
		try {
			userService.createUser(user);
			redirectAttributes.addFlashAttribute("message", "User details added successfully");
		} catch (UserException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/api/v1/users/home";
	}

	@GetMapping
	public String showUserForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("users", userService.getAllUsers());
		return "user-list";
	}

	@GetMapping("/search")
	public String redirectToUserById(@RequestParam Integer userId) {
		return "redirect:/api/v1/users/" + userId;
	}

	@GetMapping("/{user_id}")
	public String searchById(@PathVariable("user_id") Integer userId, Model model, HttpServletRequest request) {
		try {
			User user = userService.getUserById(userId);
			model.addAttribute("user", user);
			return "user-by-id";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/searchByName")
	public String redirectToUserByName(@RequestParam String userName) {
		return "redirect:/api/v1/users/name/" + UriUtils.encodePath(userName, StandardCharsets.UTF_8);
	}

	@GetMapping("/name/{user_name}")
	public String getUserByUserName(@PathVariable("user_name") String userName, Model model, HttpServletRequest request)
			throws UserException {
		try {
			User user = userService.getUserByUserName(userName);
			model.addAttribute("user", user);
			return "user-by-name";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/searchByCity")
	public String redirectToCity(@RequestParam String city) {
		return "redirect:/api/v1/users/by_city/" + UriUtils.encodePath(city, StandardCharsets.UTF_8);
	}

	@GetMapping("/by_city/{city}")
	public String getUsersByCity(@PathVariable String city, Model model, HttpServletRequest request)
			throws UserException {
		try {
			List<User> users = userService.getUsersByCity(city);
			model.addAttribute("city", city);
			model.addAttribute("users", users);
			return "users-by-city";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/searchByState")
	public String redirectToState(@RequestParam String state) {
		return "redirect:/api/v1/users/by_state/" + UriUtils.encodePath(state, StandardCharsets.UTF_8);
	}

	@GetMapping("/by_state/{state}")
	public String getUsersByState(@PathVariable String state, Model model, HttpServletRequest request)
			throws UserException {
		try {
			List<User> users = userService.getUsersByState(state);
			model.addAttribute("state", state);
			model.addAttribute("users", users);
			return "users-by-state";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/checkBalance")
	public String redirectToBalance(@RequestParam Integer userId) {
		return "redirect:/api/v1/users/check_balance/" + userId;
	}

	@GetMapping("/check_balance/{user_id}")
	public String getUserBalance(@PathVariable("user_id") Integer userId, Model model, HttpServletRequest request)
			throws UserException {
		try {
			Double balance = userService.getUserBalanceById(userId);
			model.addAttribute("userId", userId);
			model.addAttribute("balance", balance);
			return "user-balance";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/virtualGold")
	public String redirectToVirtualGold(@RequestParam Integer userId) {
		return "redirect:/api/v1/users/" + userId + "/virtual_gold_holdings";
	}

	@GetMapping("/{user_id}/virtual_gold_holdings")
	public String virtualGold(@PathVariable("user_id") Integer userId, Model model, HttpServletRequest request) {
		try {
			Double gold = userService.getTotalVirtualGoldHoldingsByUserId(userId);
			model.addAttribute("userId", userId);
			model.addAttribute("gold", gold);
			return "user-virtual-gold";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/physicalGold")
	public String redirectToPhysicalGold(@RequestParam Integer userId) {
		return "redirect:/api/v1/users/" + userId + "/physical_gold_holding";
	}

	@GetMapping("/{user_id}/physical_gold_holding")
	public String getTotalPhysicalGold(@PathVariable("user_id") Integer userId, Model model,
			HttpServletRequest request) {
		try {
			Double gold = userService.getTotalPhysicalGoldHoldingsByUserId(userId);
			model.addAttribute("userId", userId);
			model.addAttribute("gold", gold);
			return "user-physical-gold";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/transactionHistory")
	public String redirectToTransactionHistory(@RequestParam Integer userId) {
		return "redirect:/api/v1/users/" + userId + "/transaction_history";
	}

	@GetMapping("/{user_id}/transaction_history")
	public String getTransactionHistory(@PathVariable("user_id") Integer userId, Model model,
			HttpServletRequest request) {
		try {
			List<TransactionHistory> transactions = userService.getUserTransactionHistory(userId);
			List<Map<String, Object>> formattedTransactions = transactions.stream().map(txn -> {
				Map<String, Object> map = new HashMap<>();
				map.put("transactionId", txn.getTransactionId());
				map.put("transactionType", txn.getTransactionType());
				map.put("transactionStatus", txn.getTransactionStatus());
				map.put("quantity", txn.getQuantity());
				map.put("amount", txn.getAmount());
				map.put("branchName", txn.getBranch() != null ? txn.getBranch().getBranchId() : "N/A");
				map.put("createdAt", txn.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
				return map;
			}).collect(Collectors.toList());
			model.addAttribute("userId", userId);
			model.addAttribute("transactions", formattedTransactions);
			return "user-transaction-history";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/payments")
	public String redirectToPayments(@RequestParam Integer userId) {
		return "redirect:/api/v1/users/" + userId + "/payments";
	}

	@GetMapping("/{user_id}/payments")
	public String getUserPayments(@PathVariable("user_id") Integer userId, Model model, HttpServletRequest request) {
		try {
			List<Payment> payments = userService.getUserPayments(userId);

			List<Map<String, Object>> formattedPayments = payments.stream().map(payment -> {
				Map<String, Object> map = new HashMap<>();
				map.put("paymentId", payment.getPaymentId());
				map.put("amount", payment.getAmount());
				map.put("paymentMethod", payment.getPaymentMethod());
				map.put("transactionType", payment.getTransactionType());
				map.put("paymentStatus", payment.getPaymentStatus());
				map.put("createdAt", payment.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
				return map;
			}).collect(Collectors.toList());
			model.addAttribute("userId", userId);
			model.addAttribute("payments", formattedPayments);
			return "user-payments";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/updatePage/{user_id}")
	public String showUpdateUserForm(@PathVariable("user_id") Integer userId, Model model, HttpServletRequest request) {
		try {
			User user = userService.getUserById(userId);
			List<Address> addressList = addressService.getAllAddresses();
			model.addAttribute("user", user);
			model.addAttribute("addressList", addressList);
			return "user-update";
		} catch (UserException e) {
			return handleError(e, request, model);
		}
	}

	@PostMapping("/update")
	public String updateUserForm(@Valid @ModelAttribute User user, BindingResult result, Model model,
			HttpServletRequest request) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("addressList", addressService.getAllAddresses());
				return "user-update";
			}
			Integer addressId = user.getAddress().getAddressId();
			Address fullAddress = addressService.getAddressById(addressId);
			user.setAddress(fullAddress);
			userService.updateUser(user.getUserId(), user);
			return "redirect:/api/v1/users/home";
		} catch (UserException | AddressException e) {
			return handleError(e, request, model);
		}
	}

//	@PatchMapping("/{user_id}/update_balance/{amount}")
//	public ResponseEntity<String> updateUserBalance(@PathVariable("user_id") Integer userId,
//			@PathVariable Double amount) throws UserException {
//		userService.updateUserBalance(userId, amount);
//		return ResponseEntity.ok("User Balance updated successfully");
//	}

	@GetMapping("/updateBalancePage")
	public String showUpdateBalancePage() {
		return "user-update-balance";
	}

	@PostMapping("/updateBalancePage")
	public String handleUpdateBalanceForm(@RequestParam Integer userId, @RequestParam Double amount, Model model,
			HttpServletRequest request) {
		try {
			userService.updateUserBalance(userId, amount);
			model.addAttribute("message", "User Balance updated successfully!");
		} catch (Exception e) {
			return handleError(e, request, model);
		}
		return "user-update-balance";
	}

//	@PatchMapping("/{user_id}/update_address/{address_id}")
//	public ResponseEntity<String> updateUserAddress(@PathVariable("user_id") Integer userId,
//			@PathVariable("address_id") Integer addressId) throws UserException, AddressException {
//		userService.updateUserAddress(userId, addressId);
//		return ResponseEntity.ok("User address updated successfully");
//	}

	@GetMapping("/updateAddressPage")
	public String showUpdateAddressPage(Model model) {
		List<Address> addressList = addressService.getAllAddresses();
		model.addAttribute("addressList", addressList);
		return "user-update-address";
	}

	@PostMapping("/updateAddressPage")
	public String handleUpdateAddressForm(@RequestParam Integer userId, @RequestParam Integer addressId, Model model,
			HttpServletRequest request) {
		try {
			userService.updateUserAddress(userId, addressId);
			model.addAttribute("message", "User address updated successfully!");
		} catch (Exception e) {
			return handleError(e, request, model);
		}
		return "user-update-address";
	}

	private String handleError(Exception e, HttpServletRequest request, Model model) {
		model.addAttribute("errorMessage", environment.getProperty(e.getMessage()));
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("timestamp", LocalDateTime.now());
		return "user-error";
	}
}
