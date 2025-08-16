package com.cg.gold.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.gold.entity.Payment;
import com.cg.gold.entity.Payment.PaymentMethod;
import com.cg.gold.exception.PaymentException;
import com.cg.gold.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/payments")
public class PaymentRestController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private Environment environment;

	@GetMapping
	public ResponseEntity<List<Payment>> getAllPayments() {
		return new ResponseEntity<>(paymentService.getAllPayments(), HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addPayment(@Valid @RequestBody Payment newPayment) throws PaymentException {
		paymentService.addPayment(newPayment);
		return ResponseEntity.ok(environment.getProperty("PaymentService.SUCCESS"));
	}

	@GetMapping("/{payment_id}")
	public ResponseEntity<Payment> getPaymentById(@PathVariable("payment_id") Integer paymentId)
			throws PaymentException {
		Payment payment = paymentService.getPaymentById(paymentId);
		return ResponseEntity.ok(payment);
	}

	@GetMapping("/by_user/{user_id}")
	public ResponseEntity<List<Payment>> getAllPaymentsByUserId(@PathVariable("user_id") Integer userId)
			throws PaymentException {
		List<Payment> payments = paymentService.getAllPaymentByUserId(userId);
		return ResponseEntity.ok(payments);
	}

	@GetMapping("/successful")
	public ResponseEntity<List<Payment>> getAllSuccessPayments() {
		List<Payment> payments = paymentService.getAllSuccessPayments();
		return ResponseEntity.ok(payments);
	}

	@GetMapping("/failed")
	public ResponseEntity<List<Payment>> getAllFailedPayments() {
		List<Payment> payments = paymentService.getAllFailedPayments();
		return ResponseEntity.ok(payments);
	}

	@GetMapping("/by_payment_method/{payment_method}")
	public ResponseEntity<List<Payment>> getAllPaymentsByMethod(@PathVariable("payment_method") PaymentMethod method)
			throws PaymentException {
		List<Payment> payments = paymentService.getAllPaymentsByPaymentMethod(method);
		return ResponseEntity.ok(payments);
	}

}