package com.cg.gold.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.exception.PhysicalGoldTransactionException;
import com.cg.gold.service.PhysicalGoldTransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/physical_gold_transactions")
public class PhysicalGoldTransactionRestController {

	@Autowired
	private PhysicalGoldTransactionService transactionService;

	@Autowired
	private Environment environment;

	@PostMapping("/add")
	public ResponseEntity<String> addTransaction(@Valid @RequestBody PhysicalGoldTransaction transaction)
			throws PhysicalGoldTransactionException {
		transactionService.addPhysicalGoldTransaction(transaction);
		return ResponseEntity.ok(environment.getProperty("PhysicalGoldService.SUCCESS"));
	}

	@GetMapping
	public ResponseEntity<List<PhysicalGoldTransaction>> getAllTransactions() {
		return ResponseEntity.ok(transactionService.getAllPhysicalGoldTransactions());
	}

	@GetMapping("/{transaction_id}")
	public ResponseEntity<PhysicalGoldTransaction> getById(@PathVariable("transaction_id") Integer transactionId)
			throws PhysicalGoldTransactionException {
		return ResponseEntity.ok(transactionService.getPhysicalGoldTransactionById(transactionId));
	}

	@GetMapping("/by_user/{user_id}")
	public ResponseEntity<List<PhysicalGoldTransaction>> getByUserId(@PathVariable("user_id") Integer userId)
			throws PhysicalGoldTransactionException {
		return ResponseEntity.ok(transactionService.getPhysicalGoldTransactionByUserId(userId));
	}

	@GetMapping("/by_branch/{branch_id}")
	public ResponseEntity<List<PhysicalGoldTransaction>> getByBranchId(@PathVariable("branch_id") Integer branchId)
			throws PhysicalGoldTransactionException {
		return ResponseEntity.ok(transactionService.getPhysicalGoldTransactionByBranchId(branchId));
	}

	@GetMapping("/by_delivery_city/{city}")
	public ResponseEntity<List<PhysicalGoldTransaction>> getByCity(@PathVariable String city)
			throws PhysicalGoldTransactionException {
		return ResponseEntity.ok(transactionService.getAllPhysicalGoldTransactionByDeliveryCity(city));
	}

	@GetMapping("/by_delivery_state/{state}")
	public ResponseEntity<List<PhysicalGoldTransaction>> getByState(@PathVariable String state)
			throws PhysicalGoldTransactionException {
		return ResponseEntity.ok(transactionService.getAllPhysicalGoldTransactionByDeliveryState(state));
	}

	@PutMapping("/update/{transaction_id}")
	public ResponseEntity<String> updateTransaction(@PathVariable("transaction_id") Integer transactionId,
			@Valid @RequestBody PhysicalGoldTransaction transaction) throws PhysicalGoldTransactionException {
		Integer updatedId = transactionService.updatePhysicalGoldTransaction(transactionId, transaction);
		return ResponseEntity.ok(environment.getProperty("PhysicalGoldService.UPDATED") + updatedId);
	}
}
