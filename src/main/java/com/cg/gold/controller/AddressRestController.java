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

import com.cg.gold.entity.Address;
import com.cg.gold.exception.AddressException;
import com.cg.gold.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/address")
public class AddressRestController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private Environment environment;

	@PostMapping("/add")
	public ResponseEntity<String> addAddress(@Valid @RequestBody Address address) {
		addressService.addAddress(address);
		return ResponseEntity.status(HttpStatus.CREATED).body(environment.getProperty("AddressService.ADDED"));
	}

	@GetMapping
	public List<Address> getAllAddresses() {
		return addressService.getAllAddresses();
	}

	@GetMapping("/{address_id}")
	public ResponseEntity<Address> getAddressById(@PathVariable("address_id") Integer addressId)
			throws AddressException {
		Address address = addressService.getAddressById(addressId);
		return ResponseEntity.ok(address);
	}

	@PutMapping("/update/{address_id}")
	public ResponseEntity<String> updateAddress(@PathVariable("address_id") Integer addressId,
			@Valid @RequestBody Address address) throws AddressException {
		addressService.updateAddressById(addressId, address);
		return ResponseEntity.ok(environment.getProperty("AddressService.UPDATED"));
	}

}
