package com.cg.gold.service;

import java.util.List;

import com.cg.gold.entity.Address;
import com.cg.gold.exception.AddressException;

public interface AddressService {

	public List<Address> getAllAddresses();

	public Address getAddressById(Integer addressId) throws AddressException;

	public void addAddress(Address address);

	public void updateAddressById(Integer addressId, Address updatedAddress) throws AddressException;
}
