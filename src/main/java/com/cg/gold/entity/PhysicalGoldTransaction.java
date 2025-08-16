package com.cg.gold.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "physical_gold_transactions")
public class PhysicalGoldTransaction {
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

	@Column(name = "QUANTITY", nullable = false)
	private Double quantity;

	@ManyToOne
	@JoinColumn(name = "DELIVERY_ADDRESS_ID")
	private Address deliveryAddress;

	@PastOrPresent(message = "CreatedAt must be in the past or present")
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

	public PhysicalGoldTransaction() {
	}

	public PhysicalGoldTransaction(User user, VendorBranch branch, Double quantity, Address deliveryAddress,
			LocalDateTime createdAt) {
		super();
		this.user = user;
		this.branch = branch;
		this.quantity = quantity;
		this.deliveryAddress = deliveryAddress;
		this.createdAt = createdAt;
	}

	public PhysicalGoldTransaction(Integer transactionId, User user, VendorBranch branch, Double quantity,
			Address deliveryAddress, LocalDateTime createdAt) {
		this.transactionId = transactionId;
		this.user = user;
		this.branch = branch;
		this.quantity = quantity;
		this.deliveryAddress = deliveryAddress;
		this.createdAt = createdAt;
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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "PhysicalGoldTransaction{" + "transactionId=" + transactionId + ", user=" + user + ", branch=" + branch
				+ ", quantity=" + quantity + ", deliveryAddress=" + deliveryAddress + ", createdAt=" + createdAt + '}';
	}
}