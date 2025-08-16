package com.cg.gold.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.TransactionHistory.TransactionType;
import com.cg.gold.exception.TransactionHistoryException;
import com.cg.gold.service.TransactionHistoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/transaction_history")
public class TransactionHistoryRestController {

	@Autowired
	private TransactionHistoryService transactionHistoryService;

	@Autowired
	private Environment environment;

	@GetMapping
	public ResponseEntity<List<TransactionHistory>> getAllTransactionHistory() {
		return ResponseEntity.ok(transactionHistoryService.getAllTransactionHistory());
	}

	@PostMapping("/add")
	public ResponseEntity<String> addTransaction(@Valid @RequestBody TransactionHistory newTransaction)
			throws TransactionHistoryException {
		transactionHistoryService.addTransactionHistory(newTransaction);
		return ResponseEntity.ok(environment.getProperty("TransactionHistoryService.SUCCESS"));
	}

	@GetMapping("/{transaction_id}")
	public ResponseEntity<TransactionHistory> getTransactionById(@PathVariable("transaction_id") Integer transactionId)
			throws TransactionHistoryException {
		TransactionHistory transaction = transactionHistoryService.getTransactionHistoryById(transactionId);
		return ResponseEntity.ok(transaction);
	}

	@GetMapping("/by_user/{user_id}")
	public ResponseEntity<List<TransactionHistory>> getAllByUserId(@PathVariable("user_id") Integer userId)
			throws TransactionHistoryException {
		return ResponseEntity.ok(transactionHistoryService.getAllTransactionHistoryByUserId(userId));
	}

	@GetMapping("/successful")
	public ResponseEntity<List<TransactionHistory>> getAllSuccessTransactions() {
		return ResponseEntity.ok(transactionHistoryService.getAllSuccessTransactionHistory());
	}

	@GetMapping("/failed")
	public ResponseEntity<List<TransactionHistory>> getAllFailedTransactions() {
		return ResponseEntity.ok(transactionHistoryService.getAllFailedTransactionHistory());
	}

	@GetMapping("/by_type/{transaction_type}")
	public ResponseEntity<List<TransactionHistory>> getAllByTransactionType(
			@PathVariable("transaction_type") String transactionType) throws TransactionHistoryException {
		TransactionType type = TransactionType.valueOf(transactionType);
		return ResponseEntity.ok(transactionHistoryService.getAllTransactionHistoryByTransactionType(type));
	}

}
