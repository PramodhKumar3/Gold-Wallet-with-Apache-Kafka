package com.cg.gold.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.gold.entity.Payment;
import com.cg.gold.entity.Payment.PaymentMethod;
import com.cg.gold.entity.Payment.PaymentStatus;
import com.cg.gold.exception.PaymentException;
import com.cg.gold.repository.PaymentRepository;
import com.cg.gold.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}

	@Override
	public Payment getPaymentById(Integer paymentId) throws PaymentException {
		return paymentRepository.findById(paymentId)
				.orElseThrow(() -> new PaymentException("PaymentService.PAYMENT_NOT_FOUND"));
	}

	@Override
	public List<Payment> getAllPaymentByUserId(Integer userId) throws PaymentException {
		userRepository.findById(userId).orElseThrow(() -> new PaymentException("UserService.USER_NOT_FOUND"));
		List<Payment> payments = paymentRepository.findByUserUserId(userId);
		if (payments.isEmpty()) {
			throw new PaymentException("PaymentService.USER_NOT_FOUND");
		}
		return payments;
	}

	@Override
	public List<Payment> getAllSuccessPayments() {
		return paymentRepository.findByPaymentStatus(PaymentStatus.Success);
	}

	@Override
	public List<Payment> getAllFailedPayments() {
		return paymentRepository.findByPaymentStatus(PaymentStatus.Failed);
	}

	@Override
	public List<Payment> getAllPaymentsByPaymentMethod(PaymentMethod paymentMethod) throws PaymentException {
		List<PaymentMethod> validMethods = Arrays.asList(PaymentMethod.AmazonPay, PaymentMethod.BankTransfer,
				PaymentMethod.CreditCard, PaymentMethod.DebitCard, PaymentMethod.GooglePay, PaymentMethod.Paytm,
				PaymentMethod.PhonePe);
		if (!validMethods.contains(paymentMethod))
			throw new PaymentException("PaymentService.INVALID_PAYMENT");
		if (paymentRepository.findByPaymentMethod(paymentMethod).isEmpty())
			throw new PaymentException("PaymentService.NO_PAYMENT_AVAILABLE");
		return paymentRepository.findByPaymentMethod(paymentMethod);
	}

	@Override
	public void addPayment(Payment newPayment) throws PaymentException {
		Integer userId = newPayment.getUser().getUserId();
		userRepository.findById(userId).orElseThrow(() -> new PaymentException("UserService.USER_NOT_FOUND"));
		newPayment.setCreatedAt(LocalDateTime.now());
		paymentRepository.save(newPayment);
	}

	@Override
	public List<Payment> getPaymentsByUser(Integer userId) throws PaymentException {
		userRepository.findById(userId).orElseThrow(() -> new PaymentException("UserService.USER_NOT_FOUND"));
		return paymentRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
	}

}
