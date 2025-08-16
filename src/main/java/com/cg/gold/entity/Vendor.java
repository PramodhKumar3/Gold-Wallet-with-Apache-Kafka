package com.cg.gold.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "vendors")
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_ID")
	private Integer vendorId;

	@NotBlank(message = "Vendor name is required")
	@Pattern(regexp = "[A-Za-z\\s]{2,}", message = "Name must be between 2 and 100 characters")
	@Column(name = "VENDOR_NAME", nullable = false)
	private String vendorName;

	@Size(max = 500, message = "Description must be less than 500 characters")
	@Column(name = "DESCRIPTION")
	private String description;

	@Size(max = 100, message = "Contact person name must be less than 100 characters")
	@Pattern(regexp = "[A-Za-z\\s]{2,}", message = "Name must be between 2 and 100 characters")
	@Column(name = "CONTACT_PERSON_NAME")
	private String contactPersonName;

	@Email(message = "Please enter a valid email address")
	@Size(max = 100, message = "Email must be less than 100 characters")
	@Column(name = "CONTACT_EMAIL")
	private String contactEmail;

	@Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
	@Column(name = "CONTACT_PHONE")
	private String contactPhone;

	@Column(name = "WEBSITE_URL")
	private String websiteUrl;

	@NotNull(message = "Quantity is required")
	@Column(name = "TOTAL_GOLD_QUANTITY", nullable = false)
	private Double totalGoldQuantity;

	@NotNull(message = "Gold Price is required")
	@Column(name = "CURRENT_GOLD_PRICE", nullable = false)
	private Double currentGoldPrice;

	@PastOrPresent(message = "CreatedAt must be in the past or present")
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

//	@OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<VendorBranch> vendorBranches;

	public Vendor() {
	}

	public Vendor(Integer vendorId, String vendorName, String description, String contactPersonName,
			String contactEmail, String contactPhone, String websiteUrl, Double totalGoldQuantity,
			Double currentGoldPrice, LocalDateTime createdAt) {
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.description = description;
		this.contactPersonName = contactPersonName;
		this.contactEmail = contactEmail;
		this.contactPhone = contactPhone;
		this.websiteUrl = websiteUrl;
		this.totalGoldQuantity = totalGoldQuantity;
		this.currentGoldPrice = currentGoldPrice;
		this.createdAt = createdAt;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public Double getTotalGoldQuantity() {
		return totalGoldQuantity;
	}

	public void setTotalGoldQuantity(Double totalGoldQuantity) {
		this.totalGoldQuantity = totalGoldQuantity;
	}

	public Double getCurrentGoldPrice() {
		return currentGoldPrice;
	}

	public void setCurrentGoldPrice(Double currentGoldPrice) {
		this.currentGoldPrice = currentGoldPrice;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

//	public List<VendorBranch> getVendorBranches() {
//		return vendorBranches;
//	}
//
//	public void setVendorBranches(List<VendorBranch> vendorBranches) {
//		this.vendorBranches = vendorBranches;
//	}

	@Override
	public String toString() {
		return "Vendor{" + "vendorId=" + vendorId + ", vendorName='" + vendorName + '\'' + ", description='"
				+ description + '\'' + ", contactPersonName='" + contactPersonName + '\'' + ", contactEmail='"
				+ contactEmail + '\'' + ", contactPhone='" + contactPhone + '\'' + ", websiteUrl='" + websiteUrl + '\''
				+ ", totalGoldQuantity=" + totalGoldQuantity + ", currentGoldPrice=" + currentGoldPrice + ", createdAt="
				+ createdAt + '}';
	}
}