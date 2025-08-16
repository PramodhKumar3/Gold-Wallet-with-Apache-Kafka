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

@Entity
@Table(name = "virtual_gold_holdings")
public class VirtualGoldHolding {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HOLDING_ID")
	private Integer holdingId;

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

	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

	public VirtualGoldHolding() {
	}

	public VirtualGoldHolding(Integer holdingId, User user, VendorBranch branch, Double quantity,
			LocalDateTime createdAt) {
		this.holdingId = holdingId;
		this.user = user;
		this.branch = branch;
		this.quantity = quantity;
		this.createdAt = createdAt;
	}

	public Integer getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(Integer holdingId) {
		this.holdingId = holdingId;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "VirtualGoldHolding{" + "holdingId=" + holdingId + ", user=" + user + ", branch=" + branch
				+ ", quantity=" + quantity + ", createdAt=" + createdAt + '}';
	}
}