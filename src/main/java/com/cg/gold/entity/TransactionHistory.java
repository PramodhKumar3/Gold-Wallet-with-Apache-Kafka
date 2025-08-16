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

@Entity
@Table(name = "transaction_history")
public class TransactionHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTION_ID")
	private Integer transactionId;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	@NotNull(message = "User is required")
	private User user;

	@ManyToOne
	@JoinColumn(name = "BRANCH_ID")
	@NotNull(message = "Vendor branch is required")
	private VendorBranch branch;

	@Enumerated(EnumType.STRING)
	@Column(name = "TRANSACTION_TYPE")
	private TransactionType transactionType;

	@Enumerated(EnumType.STRING)
	@Column(name = "TRANSACTION_STATUS")
	private TransactionStatus transactionStatus;

	@Column(name = "QUANTITY", nullable = false)
	private Double quantity;

	@Column(name = "AMOUNT", nullable = false)
	private Double amount;

	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

	public TransactionHistory() {
	}

	public TransactionHistory(Integer transactionId, User user, VendorBranch branch, TransactionType transactionType,
			TransactionStatus transactionStatus, Double quantity, Double amount, LocalDateTime createdAt) {
		this.transactionId = transactionId;
		this.user = user;
		this.branch = branch;
		this.transactionType = transactionType;
		this.transactionStatus = transactionStatus;
		this.quantity = quantity;
		this.amount = amount;
		this.createdAt = createdAt;
	}

	public enum TransactionType {
		Buy, Sell, ConvertToPhysical
	}

	public enum TransactionStatus {
		Success, Failed
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public VendorBranch getBranch() {
		return branch;
	}

	public void setBranch(VendorBranch branch) {
		this.branch = branch;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "TransactionHistory{" + "transactionId=" + transactionId + ", user=" + user + ", branch=" + branch
				+ ", transactionType=" + transactionType + ", transactionStatus=" + transactionStatus + ", quantity="
				+ quantity + ", amount=" + amount + ", createdAt=" + createdAt + '}';
	}
}