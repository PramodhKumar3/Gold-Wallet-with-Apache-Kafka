package com.cg.gold.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.exception.VendorBranchException;
import com.cg.gold.service.VendorBranchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/vendor_branches")
public class VendorBranchRestController {

	@Autowired
	private VendorBranchService vendorBranchService;
	
	@Autowired
	private Environment environment;

	@GetMapping
	public ResponseEntity<List<VendorBranch>> getAllBranches() {
		return ResponseEntity.ok(vendorBranchService.getAllVendorBranches());
	}

	@PostMapping("/add")
	public ResponseEntity<String> addBranch(@Valid @RequestBody VendorBranch branch) throws VendorBranchException {
		vendorBranchService.addBranch(branch);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(environment.getProperty("VendorBranchService.SUCCESS"));
	}

	@GetMapping("/{branch_id}")
	public ResponseEntity<VendorBranch> getBranchById(@PathVariable("branch_id") Integer branchId)
			throws VendorBranchException {
		return ResponseEntity.ok(vendorBranchService.getVendorBranchByBranchId(branchId));
	}

	@GetMapping("/by_vendor/{vendor_id}")
	public ResponseEntity<List<VendorBranch>> getBranchesByVendor(@PathVariable("vendor_id") Integer vendorId)
			throws VendorBranchException {
		return ResponseEntity.ok(vendorBranchService.getVendorBranchesByVendorId(vendorId));
	}

	@GetMapping("/by_city/{city}")
	public ResponseEntity<List<VendorBranch>> getBranchesByCity(@PathVariable String city)
			throws VendorBranchException {
		return ResponseEntity.ok(vendorBranchService.getVendorBranchesByCity(city));
	}

	@GetMapping("/by_state/{state}")
	public ResponseEntity<List<VendorBranch>> getBranchesByState(@PathVariable String state)
			throws VendorBranchException {
		return ResponseEntity.ok(vendorBranchService.getVendorBranchesByState(state));
	}

	@GetMapping("/by_country/{country}")
	public ResponseEntity<List<VendorBranch>> getBranchesByCountry(@PathVariable String country)
			throws VendorBranchException {
		return ResponseEntity.ok(vendorBranchService.getVendorBranchesByCountry(country));
	}

	@GetMapping("/transactions/{branch_id}")
	public ResponseEntity<List<TransactionHistory>> getBranchTransactions(@PathVariable("branch_id") Integer branchId)
			throws VendorBranchException {
		return ResponseEntity.ok(vendorBranchService.getVendorBranchTransactionsByBranchId(branchId));
	}

	@PutMapping("/update/{branch_id}")
	public ResponseEntity<String> updateBranch(@PathVariable("branch_id") Integer branchId,
			@Valid @RequestBody VendorBranch updatedBranch) throws VendorBranchException {
		vendorBranchService.updateBranch(branchId, updatedBranch);
		return ResponseEntity.ok(environment.getProperty("VendorBranchService.UPDATE"));
	}

	@PostMapping("/transfer/{source_branch_id}/{destination_branch_id}/{quantity}")
	public ResponseEntity<String> transferGold(@PathVariable("source_branch_id") Integer sourceBranchId,
			@PathVariable("destination_branch_id") Integer destinationBranchId, @PathVariable Double quantity)
			throws VendorBranchException {
		vendorBranchService.transferGold(sourceBranchId, destinationBranchId, quantity);
		return ResponseEntity.ok(environment.getProperty("VendorBranchService.TRANSFER"));
	}

}
