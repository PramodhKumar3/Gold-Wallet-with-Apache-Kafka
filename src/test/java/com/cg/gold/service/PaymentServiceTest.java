package com.cg.gold.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.gold.entity.Payment;
import com.cg.gold.entity.Payment.PaymentMethod;
import com.cg.gold.entity.Payment.PaymentStatus;
import com.cg.gold.entity.User;
import com.cg.gold.exception.PaymentException;
import com.cg.gold.repository.PaymentRepository;
import com.cg.gold.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private PaymentServiceImpl paymentService;

	private User user;
	private Payment payment;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setUserId(1);

		payment = new Payment();
		payment.setPaymentId(100);
		payment.setUser(user);
		payment.setAmount(500.0);
		payment.setPaymentMethod(PaymentMethod.GooglePay);
		payment.setPaymentStatus(PaymentStatus.Success);
		payment.setCreatedAt(LocalDateTime.now());
	}

	@Test
	void testGetAllPayments() {
		Mockito.when(paymentRepository.findAll()).thenReturn(List.of(payment));
		List<Payment> result = paymentService.getAllPayments();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetPaymentById_Success() throws PaymentException {
		Mockito.when(paymentRepository.findById(100)).thenReturn(Optional.of(payment));
		Payment result = paymentService.getPaymentById(100);
		Assertions.assertEquals(100, result.getPaymentId());
	}

	@Test
	void testGetPaymentById_NotFound() {
		Mockito.when(paymentRepository.findById(999)).thenReturn(Optional.empty());
		Assertions.assertThrows(PaymentException.class, () -> paymentService.getPaymentById(999));
	}

	@Test
	void testGetAllPaymentByUserId_Success() throws PaymentException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(paymentRepository.findByUserUserId(1)).thenReturn(List.of(payment));
		List<Payment> result = paymentService.getAllPaymentByUserId(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllPaymentByUserId_UserNotFound() {
		Mockito.when(userRepository.findById(2)).thenReturn(Optional.empty());
		Assertions.assertThrows(PaymentException.class, () -> paymentService.getAllPaymentByUserId(2));
	}

	@Test
	void testGetAllPaymentByUserId_NoPayments() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(paymentRepository.findByUserUserId(1)).thenReturn(Collections.emptyList());
		Assertions.assertThrows(PaymentException.class, () -> paymentService.getAllPaymentByUserId(1));
	}

	@Test
	void testGetAllSuccessPayments() {
		Mockito.when(paymentRepository.findByPaymentStatus(PaymentStatus.Success)).thenReturn(List.of(payment));
		List<Payment> result = paymentService.getAllSuccessPayments();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllFailedPayments() {
		Mockito.when(paymentRepository.findByPaymentStatus(PaymentStatus.Failed)).thenReturn(List.of());
		List<Payment> result = paymentService.getAllFailedPayments();
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void testGetAllPaymentsByPaymentMethod_Success() throws PaymentException {
		Mockito.when(paymentRepository.findByPaymentMethod(PaymentMethod.GooglePay)).thenReturn(List.of(payment));
		List<Payment> result = paymentService.getAllPaymentsByPaymentMethod(PaymentMethod.GooglePay);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllPaymentsByPaymentMethod_InvalidMethod() {
		Assertions.assertThrows(PaymentException.class, () -> paymentService.getAllPaymentsByPaymentMethod(null));
	}

	@Test
	void testGetAllPaymentsByPaymentMethod_NoPayments() {
		Mockito.when(paymentRepository.findByPaymentMethod(PaymentMethod.Paytm)).thenReturn(Collections.emptyList());
		Assertions.assertThrows(PaymentException.class,
				() -> paymentService.getAllPaymentsByPaymentMethod(PaymentMethod.Paytm));
	}

	@Test
	void testAddPayment_Success() throws PaymentException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		paymentService.addPayment(payment);
		Mockito.verify(paymentRepository, times(1)).save(any(Payment.class));
	}

	@Test
	void testAddPayment_UserNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(PaymentException.class, () -> paymentService.addPayment(payment));
	}

	@Test
	void testGetPaymentsByUser_Success() throws PaymentException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(paymentRepository.findByUserUserIdOrderByCreatedAtDesc(1)).thenReturn(List.of(payment));
		List<Payment> result = paymentService.getPaymentsByUser(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetPaymentsByUser_UserNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(PaymentException.class, () -> paymentService.getPaymentsByUser(1));
	}
}
