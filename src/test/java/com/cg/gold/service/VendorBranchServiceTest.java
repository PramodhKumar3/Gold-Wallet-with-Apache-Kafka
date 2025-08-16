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
import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.Vendor;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.exception.VendorBranchException;
import com.cg.gold.repository.AddressRepository;
import com.cg.gold.repository.TransactionHistoryRepository;
import com.cg.gold.repository.VendorBranchRepository;
import com.cg.gold.repository.VendorRepository;

@ExtendWith(MockitoExtension.class)
public class VendorBranchServiceTest {

	@Mock
	private VendorBranchRepository vendorBranchRepository;
	@Mock
	private TransactionHistoryRepository transactionHistoryRepository;
	@Mock
	private AddressRepository addressRepository;
	@Mock
	private VendorRepository vendorRepository;

	@InjectMocks
	private VendorBranchServiceImpl vendorBranchService;

	private VendorBranch branch;
	private Vendor vendor;
	private Address address;

	@BeforeEach
	void setUp() {
		vendor = new Vendor();
		vendor.setVendorId(1);

		address = new Address();
		address.setAddressId(1);
		address.setCity("Pune");
		address.setState("Maharashtra");
		address.setCountry("India");

		branch = new VendorBranch();
		branch.setBranchId(1);
		branch.setVendor(vendor);
		branch.setAddress(address);
		branch.setQuantity(100.0);
	}

	@Test
	void testGetAllVendorBranches() {
		Mockito.when(vendorBranchRepository.findAll()).thenReturn(List.of(branch));
		List<VendorBranch> result = vendorBranchService.getAllVendorBranches();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetVendorBranchByBranchId_Success() throws VendorBranchException {
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		VendorBranch result = vendorBranchService.getVendorBranchByBranchId(1);
		Assertions.assertEquals(100.0, result.getQuantity());
	}

	@Test
	void testGetVendorBranchByBranchId_NotFound() {
		Mockito.when(vendorBranchRepository.findById(99)).thenReturn(Optional.empty());
		Assertions.assertThrows(VendorBranchException.class, () -> vendorBranchService.getVendorBranchByBranchId(99));
	}

	@Test
	void testGetVendorBranchesByVendorId_Success() throws VendorBranchException {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Mockito.when(vendorBranchRepository.findByVendorVendorId(1)).thenReturn(List.of(branch));
		List<VendorBranch> result = vendorBranchService.getVendorBranchesByVendorId(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetVendorBranchesByVendorId_NotFound() {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VendorBranchException.class, () -> vendorBranchService.getVendorBranchesByVendorId(1));
	}

	@Test
	void testGetVendorBranchesByVendorId_NoBranches() {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Mockito.when(vendorBranchRepository.findByVendorVendorId(1)).thenReturn(List.of());
		Assertions.assertThrows(VendorBranchException.class, () -> vendorBranchService.getVendorBranchesByVendorId(1));
	}

	@Test
	void testGetVendorBranchesByCity_Success() throws VendorBranchException {
		Mockito.when(vendorBranchRepository.findByAddressCityIgnoreCase("Pune")).thenReturn(List.of(branch));
		List<VendorBranch> result = vendorBranchService.getVendorBranchesByCity("Pune");
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetVendorBranchesByCity_NotFound() {
		Mockito.when(vendorBranchRepository.findByAddressCityIgnoreCase("Mumbai")).thenReturn(List.of());
		Assertions.assertThrows(VendorBranchException.class,
				() -> vendorBranchService.getVendorBranchesByCity("Mumbai"));
	}

	@Test
	void testGetVendorBranchesByState_Success() throws VendorBranchException {
		Mockito.when(vendorBranchRepository.findByAddressStateIgnoreCase("Maharashtra")).thenReturn(List.of(branch));
		List<VendorBranch> result = vendorBranchService.getVendorBranchesByState("Maharashtra");
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetVendorBranchesByState_NotFound() {
		Mockito.when(vendorBranchRepository.findByAddressStateIgnoreCase("Goa")).thenReturn(List.of());
		Assertions.assertThrows(VendorBranchException.class, () -> vendorBranchService.getVendorBranchesByState("Goa"));
	}

	@Test
	void testGetVendorBranchesByCountry_Success() throws VendorBranchException {
		Mockito.when(vendorBranchRepository.findByAddressCountryIgnoreCase("India")).thenReturn(List.of(branch));
		List<VendorBranch> result = vendorBranchService.getVendorBranchesByCountry("India");
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetVendorBranchesByCountry_NotFound() {
		Mockito.when(vendorBranchRepository.findByAddressCountryIgnoreCase("USA")).thenReturn(List.of());
		Assertions.assertThrows(VendorBranchException.class,
				() -> vendorBranchService.getVendorBranchesByCountry("USA"));
	}

	@Test
	void testGetVendorBranchTransactionsByBranchId_Success() throws VendorBranchException {
		TransactionHistory history = new TransactionHistory();
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		Mockito.when(transactionHistoryRepository.findByBranch(branch)).thenReturn(List.of(history));
		List<TransactionHistory> result = vendorBranchService.getVendorBranchTransactionsByBranchId(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testAddBranch_Success() throws VendorBranchException {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		vendorBranchService.addBranch(branch);
		Mockito.verify(vendorBranchRepository).save(Mockito.any(VendorBranch.class));
	}

	@Test
	void testUpdateBranch_Success() throws VendorBranchException {
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		vendorBranchService.updateBranch(1, branch);
		Mockito.verify(vendorBranchRepository).save(Mockito.any(VendorBranch.class));
	}

	@Test
	void testTransferGold_Success() throws VendorBranchException {
		VendorBranch source = new VendorBranch();
		source.setBranchId(1);
		source.setQuantity(100.0);

		VendorBranch destination = new VendorBranch();
		destination.setBranchId(2);
		destination.setQuantity(50.0);

		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(source));
		Mockito.when(vendorBranchRepository.findById(2)).thenReturn(Optional.of(destination));

		vendorBranchService.transferGold(1, 2, 30.0);

		Assertions.assertEquals(70.0, source.getQuantity());
		Assertions.assertEquals(80.0, destination.getQuantity());
		Mockito.verify(vendorBranchRepository).save(source);
		Mockito.verify(vendorBranchRepository).save(destination);
	}

	@Test
	void testTransferGold_InsufficientBalance() {
		VendorBranch source = new VendorBranch();
		source.setBranchId(1);
		source.setQuantity(10.0);

		VendorBranch destination = new VendorBranch();
		destination.setBranchId(2);
		destination.setQuantity(50.0);

		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(source));
		Mockito.when(vendorBranchRepository.findById(2)).thenReturn(Optional.of(destination));

		Assertions.assertThrows(VendorBranchException.class, () -> vendorBranchService.transferGold(1, 2, 30.0));
	}
}
