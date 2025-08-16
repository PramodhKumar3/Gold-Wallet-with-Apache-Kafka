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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer userId;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is mandatory")
	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;

	@NotBlank(message = "Name is mandatory")
	@Pattern(regexp = "[A-Za-z\\s]{2,}", message = "Name must contain alphabets and between 2 and 100 characters")
	@Column(name = "NAME", nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID")
	@NotNull(message = "User is required")
	private Address address;

	@NotNull(message = "Balance is required")
	@DecimalMin(value = "0.0", inclusive = true, message = "Balance must be non-negative")
	@Column(name = "BALANCE", nullable = false)
	private Double balance;

	@PastOrPresent(message = "CreatedAt must be in the past or present")
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

//	@Column(name = "PASSWORD", nullable = false)
//	private String password;
//
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<VirtualGoldHolding> virtualGoldHoldings;
//
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<PhysicalGoldTransaction> physicalGoldTransactions;
//
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<TransactionHistory> transactionHistories;
//
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<Payment> payments;

	public User() {
	}

	public User(Integer userId, String email, String name, Address address, Double balance, LocalDateTime createdAt) {
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.address = address;
		this.balance = balance;
		this.createdAt = createdAt;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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
//
//	public List<Payment> getPayments() {
//		return payments;
//	}
//
//	public void setPayments(List<Payment> payments) {
//		this.payments = payments;
//	}

	@Override
	public String toString() {
		return "User{" + "userId=" + userId + ", email='" + email + '\'' + ", name='" + name + '\'' + ", address="
				+ address + ", balance=" + balance + ", createdAt=" + createdAt + '}';
	}
}