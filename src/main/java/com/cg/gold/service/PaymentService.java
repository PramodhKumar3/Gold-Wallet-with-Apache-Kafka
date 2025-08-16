package com.cg.gold.service;

import java.util.List;

import com.cg.gold.entity.Payment;
import com.cg.gold.entity.Payment.PaymentMethod;
import com.cg.gold.exception.PaymentException;

public interface PaymentService {

	public List<Payment> getAllPayments();

	public Payment getPaymentById(Integer paymentId) throws PaymentException;

	public List<Payment> getAllPaymentByUserId(Integer userId) throws PaymentException;

	public List<Payment> getAllSuccessPayments();

	public List<Payment> getAllFailedPayments();

	public List<Payment> getAllPaymentsByPaymentMethod(PaymentMethod paymentMethod) throws PaymentException;

	public void addPayment(Payment newPayment) throws PaymentException;

	public List<Payment> getPaymentsByUser(Integer userId) throws PaymentException;
}
