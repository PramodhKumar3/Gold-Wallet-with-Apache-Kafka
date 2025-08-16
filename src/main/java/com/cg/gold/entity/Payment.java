package com.cg.gold.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAYMENT_ID")
	private Integer paymentId;

	@NotNull(message = "User is required")
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@NotNull(message = "Amount is required")
	@Column(name = "AMOUNT", nullable = false)
	private Double amount;

	@NotNull(message = "Payment method is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "PAYMENT_METHOD")
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	@Column(name = "TRANSACTION_TYPE")
	private TransactionType transactionType;

	@NotNull(message = "Payment status is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "PAYMENT_STATUS")
	private PaymentStatus paymentStatus;

	@PastOrPresent(message = "CreatedAt must be in the past or present")
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

	public Payment() {
	}

	public Payment(Integer paymentId, User user, Double amount, PaymentMethod paymentMethod,
			TransactionType transactionType, PaymentStatus paymentStatus, LocalDateTime createdAt) {
		this.paymentId = paymentId;
		this.user = user;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.transactionType = transactionType;
		this.paymentStatus = paymentStatus;
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Payment{" + "paymentId=" + paymentId + ", user=" + user + ", amount=" + amount + ", paymentMethod="
				+ paymentMethod + ", transactionType=" + transactionType + ", paymentStatus=" + paymentStatus
				+ ", createdAt=" + createdAt + '}';
	}

	public enum PaymentMethod {
		CreditCard, DebitCard, GooglePay, AmazonPay, PhonePe, Paytm, BankTransfer
	}

	public enum TransactionType {
		CreditedToWallet, DebitedFromWallet
	}

	public enum PaymentStatus {
		Success, Failed
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}