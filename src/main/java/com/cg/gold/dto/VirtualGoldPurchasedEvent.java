package com.cg.gold.dto;

import java.time.LocalDateTime;

public class VirtualGoldPurchasedEvent {
	private Integer holdingId;
	private Integer userId;
	private Integer branchId;
	private Double quantity;
	private LocalDateTime createdAt;
	private String userEmail;
	private String branchLocation;

	public VirtualGoldPurchasedEvent() {
	}

	public Integer getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(Integer holdingId) {
		this.holdingId = holdingId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
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

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getBranchLocation() {
		return branchLocation;
	}

	public void setBranchLocation(String branchLocation) {
		this.branchLocation = branchLocation;
	}

	@Override
    public String toString() {
        return "VirtualGoldPurchasedEvent{" +
                "holdingId=" + holdingId +
                ", userId=" + userId +
                ", branchId=" + branchId +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", userEmail='" + userEmail + '\'' +
                ", branchLocation='" + branchLocation + '\'' +
                '}';
    }
}
