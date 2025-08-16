package com.cg.gold.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "addresses")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ADDRESS_ID")
	private Integer addressId;

	@NotBlank(message = "Street is required")
	@Size(max = 255, message = "Street must be less than 255 characters")
	@Column(name = "STREET", nullable = false)
	private String street;

	@NotBlank(message = "City is required")
	@Pattern(regexp = "[A-Za-z]+", message = "City should contain only Alphabets")
	@Size(max = 100, message = "City must be less than 100 characters")
	@Column(name = "CITY", nullable = false)
	private String city;

	@NotBlank(message = "State is required")
	@Pattern(regexp = "[A-Za-z\\s]+", message = "State should contain only Alphabets")
	@Size(max = 100, message = "State must be less than 100 characters")
	@Column(name = "STATE", nullable = false)
	private String state;

	@Pattern(regexp = "\\d{5,6}", message = "Postal code must be 5 or 6 digits")
	@Column(name = "POSTAL_CODE")
	private String postalCode;

	@NotBlank(message = "Country is required")
	@Size(max = 100, message = "Country must be less than 100 characters")
	@Column(name = "COUNTRY", nullable = false)
	private String country;

//	@OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<User> users;
//
//	@OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<VendorBranch> vendorBranches;
//
//	@OneToMany(mappedBy = "deliveryAddress", cascade = CascadeType.ALL)
//	@JsonBackReference
//	private List<PhysicalGoldTransaction> physicalGoldTransactions;

	public Address() {
	}

	public Address(Integer addressId, String street, String city, String state, String postalCode, String country) {
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
//
//	public List<VendorBranch> getVendorBranches() {
//		return vendorBranches;
//	}
//
//	public void setVendorBranches(List<VendorBranch> vendorBranches) {
//		this.vendorBranches = vendorBranches;
//	}
//
//	public List<PhysicalGoldTransaction> getPhysicalGoldTransactions() {
//		return physicalGoldTransactions;
//	}
//
//	public void setPhysicalGoldTransactions(List<PhysicalGoldTransaction> physicalGoldTransactions) {
//		this.physicalGoldTransactions = physicalGoldTransactions;
//	}

	@Override
	public String toString() {
		return "Address{" + "addressId=" + addressId + ", street='" + street + '\'' + ", city='" + city + '\''
				+ ", state='" + state + '\'' + ", postalCode='" + postalCode + '\'' + ", country='" + country + '\''
				+ '}';
	}

	public String getFullAddress() {
		return street + ", " + city + ", " + state + " - " + postalCode + ", " + country;
	}

}