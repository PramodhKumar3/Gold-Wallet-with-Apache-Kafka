package com.cg.gold.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.gold.entity.Address;
import com.cg.gold.exception.AddressException;
import com.cg.gold.repository.AddressRepository;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private AddressServiceImpl addressService;

	private Address sampleAddress;

	@BeforeEach
	void setUp() {
		sampleAddress = new Address(1, "123 Street", "City", "State", "123456", "Country");
	}

	@Test
	void testGetAllAddresses() {
		List<Address> addressList = List.of(sampleAddress);
		Mockito.when(addressRepository.findAll()).thenReturn(addressList);

		List<Address> result = addressService.getAllAddresses();

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("123 Street", result.get(0).getStreet());
	}

	@Test
	void testGetAddressById_Success() throws AddressException {
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(sampleAddress));

		Address result = addressService.getAddressById(1);

		Assertions.assertEquals("City", result.getCity());
		Assertions.assertEquals("Country", result.getCountry());
	}

	@Test
	void testGetAddressById_NotFound() {
		Mockito.when(addressRepository.findById(99)).thenReturn(Optional.empty());

		Assertions.assertThrows(AddressException.class, () -> addressService.getAddressById(99));
	}

	@Test
	void testAddAddress() {
		addressService.addAddress(sampleAddress);
		Mockito.verify(addressRepository, Mockito.times(1)).save(sampleAddress);
	}

	@Test
	void testUpdateAddressById_Success() throws AddressException {
		Address updated = new Address(null, "New Street", "New City", "New State", "654321", "New Country");

		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(sampleAddress));

		addressService.updateAddressById(1, updated);

		Mockito.verify(addressRepository).save(Mockito.any(Address.class));
		Assertions.assertEquals("New Street", sampleAddress.getStreet());
		Assertions.assertEquals("New City", sampleAddress.getCity());
		Assertions.assertEquals("New State", sampleAddress.getState());
		Assertions.assertEquals("654321", sampleAddress.getPostalCode());
		Assertions.assertEquals("New Country", sampleAddress.getCountry());
	}

	@Test
	void testUpdateAddressById_NotFound() {
		Address updated = new Address(null, "New Street", "New City", "New State", "654321", "New Country");

		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.empty());

		Assertions.assertThrows(AddressException.class, () -> addressService.updateAddressById(1, updated));
	}
}
