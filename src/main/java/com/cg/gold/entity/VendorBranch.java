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
@Table(name = "vendor_branches")
public class VendorBranch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BRANCH_ID")
	private Integer branchId;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID")
	@NotNull(message = "Vendor is required")
	private Vendor vendor;

	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID")
	private Address address;

	@Column(name = "QUANTITY", nullable = false)
	private Double quantity;

	@PastOrPresent(message = "CreatedAt must be in the past or present")
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

//	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<VirtualGoldHolding> virtualGoldHoldings;
//	
//	If we use one to many then use 	@JsonManagedReference at ManyToOne mapping
//
//	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<PhysicalGoldTransaction> physicalGoldTransactions;
//
//	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<TransactionHistory> transactionHistories;

	public VendorBranch() {
	}

	public VendorBranch(Integer branchId, Vendor vendor, Address address, Double quantity, LocalDateTime createdAt) {
		this.branchId = branchId;
		this.vendor = vendor;
		this.address = address;
		this.quantity = quantity;
		this.createdAt = createdAt;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

//	public List<VirtualGoldHolding> getVirtualGoldHoldings() {
//		return virtualGoldHoldings;
//	}
//
//	public void setVirtualGoldHoldings(List<VirtualGoldHolding> virtualGoldHoldings) {
//		this.virtualGoldHoldings = virtualGoldHoldings;
//	}
//
//	public List<PhysicalGoldTransaction> getPhysicalGoldTransactions() {
//		return physicalGoldTransactions;
//	}
//
//	public void setPhysicalGoldTransactions(List<PhysicalGoldTransaction> physicalGoldTransactions) {
//		this.physicalGoldTransactions = physicalGoldTransactions;
//	}
//
//	public List<TransactionHistory> getTransactionHistories() {
//		return transactionHistories;
//	}
//
//	public void setTransactionHistories(List<TransactionHistory> transactionHistories) {
//		this.transactionHistories = transactionHistories;
//	}

	@Override
	public String toString() {
		return "VendorBranch{" + "branchId=" + branchId + ", vendor=" + vendor + ", address=" + address + ", quantity="
				+ quantity + ", createdAt=" + createdAt + '}';
	}
}