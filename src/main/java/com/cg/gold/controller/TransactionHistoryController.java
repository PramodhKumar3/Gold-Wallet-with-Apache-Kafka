package com.cg.gold.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.TransactionHistory.TransactionStatus;
import com.cg.gold.entity.TransactionHistory.TransactionType;
import com.cg.gold.service.TransactionHistoryService;
import com.cg.gold.service.UserService;
import com.cg.gold.service.VendorBranchService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/transaction_history")
public class TransactionHistoryController {

	@Autowired
	private TransactionHistoryService transactionHistoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private VendorBranchService branchService;

	@Autowired
	private Environment environment;

	@GetMapping("/home")
	public String showTransactionHome(Model model) {
		model.addAttribute("transaction", new TransactionHistory());
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("branches", branchService.getAllVendorBranches());
		model.addAttribute("types", TransactionType.values());
		model.addAttribute("statuses", TransactionStatus.values());
		return "transaction-history-home";
	}

	@GetMapping
	public String getAllTransactions(Model model) {
		model.addAttribute("transactions", transactionHistoryService.getAllTransactionHistory());
		return "transaction-history-list";
	}

	@PostMapping("/add")
	public String addTransaction(@ModelAttribute TransactionHistory transaction,
			RedirectAttributes redirectAttributes) {
		try {
			transaction.setUser(userService.getUserById(transaction.getUser().getUserId()));
			transaction.setBranch(branchService.getVendorBranchByBranchId(transaction.getBranch().getBranchId()));
			transaction.setCreatedAt(LocalDateTime.now());
			transactionHistoryService.addTransactionHistory(transaction);
			redirectAttributes.addFlashAttribute("message", "Transaction was successful!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/api/v1/transaction_history/home";
	}

	@GetMapping("/search/byId")
	public String getTransactionById(@RequestParam Integer transactionId, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transaction", transactionHistoryService.getTransactionHistoryById(transactionId));
			return "transaction-history-by-id";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/search/byUser")
	public String getTransactionsByUser(@RequestParam Integer userId, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("transactions", transactionHistoryService.getAllTransactionHistoryByUserId(userId));
			return "transaction-history-by-user";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/successful")
	public String getSuccessfulTransactions(Model model) {
		model.addAttribute("transactions", transactionHistoryService.getAllSuccessTransactionHistory());
		return "transaction-history-successful";
	}

	@GetMapping("/failed")
	public String getFailedTransactions(Model model) {
		model.addAttribute("transactions", transactionHistoryService.getAllFailedTransactionHistory());
		return "transaction-history-failed";
	}

	@GetMapping("/search/byType")
	public String getTransactionsByType(@RequestParam TransactionType transactionType, Model model,
			HttpServletRequest request) {
		try {
			model.addAttribute("transactions",
					transactionHistoryService.getAllTransactionHistoryByTransactionType(transactionType));
			model.addAttribute("type", transactionType);
			return "transaction-history-by-type";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	private String handleError(Exception e, HttpServletRequest request, Model model) {
		model.addAttribute("errorMessage", environment.getProperty(e.getMessage()));
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("timestamp", LocalDateTime.now());
		return "transaction-history-error";
	}

}
