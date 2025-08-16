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

import com.cg.gold.entity.Payment;
import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.User;
import com.cg.gold.exception.AddressException;
import com.cg.gold.exception.UserException;
import com.cg.gold.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/users")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private Environment environment;

	@PostMapping("/add")
	public ResponseEntity<String> createUser(@Valid @RequestBody User user) throws UserException {
		userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(environment.getProperty("UserService.SUCCESS"));
	}

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/{user_id}")
	public ResponseEntity<User> getUserById(@PathVariable("user_id") Integer userId) throws UserException {
		return ResponseEntity.ok(userService.getUserById(userId));
	}

	@GetMapping("/name/{user_name}")
	public ResponseEntity<User> getUserByEmail(@PathVariable("user_name") String userName) throws UserException {
		return ResponseEntity.ok(userService.getUserByUserName(userName));
	}

	@GetMapping("/by_city/{city}")
	public ResponseEntity<List<User>> getUsersByCity(@PathVariable String city) throws UserException {
		return ResponseEntity.ok(userService.getUsersByCity(city));
	}

	@GetMapping("/by_state/{state}")
	public ResponseEntity<List<User>> getUsersByState(@PathVariable String state) throws UserException {
		return ResponseEntity.ok(userService.getUsersByState(state));
	}

	@GetMapping("/check_balance/{user_id}")
	public ResponseEntity<Double> getUserBalance(@PathVariable("user_id") Integer userId) throws UserException {
		return ResponseEntity.ok(userService.getUserBalanceById(userId));
	}

	@GetMapping("/{user_id}/virtual_gold_holdings")
	public ResponseEntity<Double> getTotalVirtualGold(@PathVariable("user_id") Integer userId) throws UserException {
		return ResponseEntity.ok(userService.getTotalVirtualGoldHoldingsByUserId(userId));
	}

	@GetMapping("/{user_id}/physical_gold_holding")
	public ResponseEntity<Double> getTotalPhysicalGold(@PathVariable("user_id") Integer userId) throws UserException {
		return ResponseEntity.ok(userService.getTotalPhysicalGoldHoldingsByUserId(userId));
	}

	@GetMapping("/{user_id}/transaction_history")
	public ResponseEntity<List<TransactionHistory>> getTransactionHistory(@PathVariable("user_id") Integer userId)
			throws UserException {
		return ResponseEntity.ok(userService.getUserTransactionHistory(userId));
	}

	@GetMapping("/{user_id}/payments")
	public ResponseEntity<List<Payment>> getUserPayments(@PathVariable("user_id") Integer userId) throws UserException {
		return ResponseEntity.ok(userService.getUserPayments(userId));
	}

	@PutMapping("/update/{user_id}")
	public ResponseEntity<String> updateUser(@PathVariable("user_id") Integer userId, @Valid @RequestBody User user)
			throws UserException {
		userService.updateUser(userId, user);
		return ResponseEntity.ok(environment.getProperty("UserService.UPDATE"));
	}

	@PutMapping("/{user_id}/update_balance/{amount}")
	public ResponseEntity<String> updateUserBalance(@PathVariable("user_id") Integer userId,
			@PathVariable Double amount) throws UserException {
		userService.updateUserBalance(userId, amount);
		return ResponseEntity.ok(environment.getProperty("UserService.BALANCE_UPDATE"));
	}

	@PutMapping("/{user_id}/update_address/{address_id}")
	public ResponseEntity<String> updateUserAddress(@PathVariable("user_id") Integer userId,
			@PathVariable("address_id") Integer addressId) throws UserException, AddressException {
		userService.updateUserAddress(userId, addressId);
		return ResponseEntity.ok(environment.getProperty("UserService.ADDRESS_UPDATE"));
	}
}