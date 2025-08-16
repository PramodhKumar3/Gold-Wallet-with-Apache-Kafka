package com.cg.gold.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.gold.entity.Vendor;
import com.cg.gold.exception.VendorException;
import com.cg.gold.service.VendorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/vendor")
public class VendorRestController {

	@Autowired
	private VendorService vendorService;

	@Autowired
	private Environment environment;

	@GetMapping
	public ResponseEntity<List<Vendor>> getAllVendors() {
		return ResponseEntity.ok(vendorService.getAllVendors());
	}

	@PostMapping("/add")
	public ResponseEntity<String> addVendor(@Valid @RequestBody Vendor vendor) {
		vendorService.addVendor(vendor);
		return ResponseEntity.status(HttpStatus.CREATED).body(environment.getProperty("VendorService.SUCCESS"));
	}

	@GetMapping("/{vendor_id}")
	public ResponseEntity<Vendor> getVendorById(@PathVariable("vendor_id") Integer vendorId) throws VendorException {
		return ResponseEntity.ok(vendorService.getVendorById(vendorId));
	}

	@GetMapping("/name/{vendor_name}")
	public ResponseEntity<Vendor> getVendorByName(@PathVariable("vendor_name") String vendorName)
			throws VendorException {
		return ResponseEntity.ok(vendorService.getVendorByVendorName(vendorName));
	}

	@PutMapping("/update/{vendor_id}")
	public ResponseEntity<String> updateVendor(@PathVariable("vendor_id") Integer vendorId,
			@Valid @RequestBody Vendor updatedVendor) throws VendorException {
		vendorService.updateVendor(vendorId, updatedVendor);
		return ResponseEntity.ok(environment.getProperty("VendorService.UPDATED"));
	}

	@PatchMapping("/{vendor_id}/total_gold_quantity/{quantity}")
	public ResponseEntity<String> updateVendorQuantity(@PathVariable("vendor_id") Integer vendorId,
			@PathVariable Double quantity) throws VendorException {
		vendorService.updateVendorTotalGoldQuantityById(vendorId, quantity);
		return ResponseEntity.ok(environment.getProperty("VendorService.QUANTITY_UPDATED"));
	}

	@PatchMapping("/new_current_gold_price/{new_price}")
	public ResponseEntity<String> updateAllVendorsPrice(@PathVariable("new_price") Double newPrice) {
		vendorService.updateAllVendorCurrentGoldPriceWithNewPrice(newPrice);
		return ResponseEntity.ok(environment.getProperty("VendorService.GOLD_UPDATED"));
	}

}
