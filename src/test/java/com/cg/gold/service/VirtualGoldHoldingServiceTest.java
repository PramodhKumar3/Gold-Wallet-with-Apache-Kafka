package com.cg.gold.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.gold.entity.Address;
import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.entity.User;
import com.cg.gold.entity.Vendor;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.entity.VirtualGoldHolding;
import com.cg.gold.exception.VirtualGoldHoldingException;
import com.cg.gold.repository.PhysicalGoldTransactionRepository;
import com.cg.gold.repository.UserRepository;
import com.cg.gold.repository.VendorBranchRepository;
import com.cg.gold.repository.VendorRepository;
import com.cg.gold.repository.VirtualGoldHoldingRepository;

@ExtendWith(MockitoExtension.class)
public class VirtualGoldHoldingServiceTest {

	@InjectMocks
	private VirtualGoldHoldingServiceImpl service;

	@Mock
	private VirtualGoldHoldingRepository virtualGoldHoldingRepository;

	@Mock
	private PhysicalGoldTransactionRepository physicalGoldTransactionRepository;

	@Mock
	private VendorBranchRepository vendorBranchRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private VendorRepository vendorRepository;

	private User user;
	private Vendor vendor;
	private VendorBranch branch;
	private VirtualGoldHolding holding;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setUserId(1);
		user.setAddress(new Address());
		user.setBalance(1000.0);
		user.setCreatedAt(LocalDateTime.now());

		vendor = new Vendor();
		vendor.setVendorId(1);

		branch = new VendorBranch();
		branch.setBranchId(1);
		branch.setVendor(vendor);
		branch.setAddress(new Address());
		branch.setQuantity(50.0);
		branch.setCreatedAt(LocalDateTime.now());

		holding = new VirtualGoldHolding();
		holding.setHoldingId(1);
		holding.setUser(user);
		holding.setBranch(branch);
		holding.setQuantity(10.0);
		holding.setCreatedAt(LocalDateTime.now());
	}

	@Test
	void testGetAllVirtualGoldHoldings() {
		Mockito.when(virtualGoldHoldingRepository.findAll()).thenReturn(List.of(holding));
		List<VirtualGoldHolding> result = service.getAllVirtualGoldHoldings();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllVirtualGoldHoldingByUserId_Success() throws VirtualGoldHoldingException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(virtualGoldHoldingRepository.findByUserUserId(1)).thenReturn(List.of(holding));
		List<VirtualGoldHolding> result = service.getAllVirtualGoldHoldingByUserId(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllVirtualGoldHoldingByUserId_UserNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VirtualGoldHoldingException.class, () -> service.getAllVirtualGoldHoldingByUserId(1));
	}

	@Test
	void testGetVirtualGoldHoldingById_Success() throws VirtualGoldHoldingException {
		Mockito.when(virtualGoldHoldingRepository.findById(1)).thenReturn(Optional.of(holding));
		VirtualGoldHolding result = service.getVirtualGoldHoldingById(1);
		Assertions.assertEquals(holding, result);
	}

	@Test
	void testGetVirtualGoldHoldingById_NotFound() {
		Mockito.when(virtualGoldHoldingRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VirtualGoldHoldingException.class, () -> service.getVirtualGoldHoldingById(1));
	}

	@Test
	void testGetVirtualGoldHoldingByUserAndVendor_Success() throws VirtualGoldHoldingException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Mockito.when(virtualGoldHoldingRepository.findByUserUserIdAndBranchVendorVendorId(1, 1))
				.thenReturn(List.of(holding));
		List<VirtualGoldHolding> result = service.getVirtualGoldHoldingByUserAndVendor(1, 1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetVirtualGoldHoldingByUserAndVendor_UserNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VirtualGoldHoldingException.class,
				() -> service.getVirtualGoldHoldingByUserAndVendor(1, 1));
	}

	@Test
	void testGetVirtualGoldHoldingByUserAndVendor_VendorNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(vendorRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VirtualGoldHoldingException.class,
				() -> service.getVirtualGoldHoldingByUserAndVendor(1, 1));
	}

	@Test
	void testGetVirtualGoldHoldingByUserAndVendor_HoldingsEmpty() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Mockito.when(virtualGoldHoldingRepository.findByUserUserIdAndBranchVendorVendorId(1, 1)).thenReturn(List.of());
		Assertions.assertThrows(VirtualGoldHoldingException.class,
				() -> service.getVirtualGoldHoldingByUserAndVendor(1, 1));
	}

	@Test
	void testAddVirtualGoldHolding_Success() throws VirtualGoldHoldingException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		VirtualGoldHolding newHolding = new VirtualGoldHolding();
		newHolding.setUser(user);
		newHolding.setBranch(branch);
		service.addVirtualGoldHolding(newHolding);
		Mockito.verify(virtualGoldHoldingRepository).save(any(VirtualGoldHolding.class));
	}

	@Test
	void testConvertVirtualToPhysical_Success() throws VirtualGoldHoldingException {
		Mockito.when(virtualGoldHoldingRepository.findById(1)).thenReturn(Optional.of(holding));
		service.convertVirtualToPhysical(1);
		Mockito.verify(physicalGoldTransactionRepository).save(any(PhysicalGoldTransaction.class));
		Mockito.verify(virtualGoldHoldingRepository).delete(holding);
	}

	@Test
	void testConvertVirtualToPhysical_NotFound() {
		Mockito.when(virtualGoldHoldingRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VirtualGoldHoldingException.class, () -> service.convertVirtualToPhysical(1));
	}

	@Test
	void testUpdateVirtualGoldHolding_Success() throws VirtualGoldHoldingException {
		VirtualGoldHolding updated = new VirtualGoldHolding();
		updated.setUser(user);
		updated.setBranch(branch);
		updated.setQuantity(20.0);
		Mockito.when(virtualGoldHoldingRepository.findById(1)).thenReturn(Optional.of(holding));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		Integer result = service.updateVirtualGoldHolding(1, updated);
		Assertions.assertEquals(1, result);
		Mockito.verify(virtualGoldHoldingRepository).save(holding);
	}

	@Test
	void testGetTotalVirtualGoldByUser_Success() throws VirtualGoldHoldingException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(virtualGoldHoldingRepository.getTotalQuantityByUserId(1)).thenReturn(100.0);
		Double result = service.getTotalVirtualGoldByUser(1);
		Assertions.assertEquals(100.0, result);
	}

	@Test
	void testGetTotalVirtualGoldByUser_UserNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VirtualGoldHoldingException.class, () -> service.getTotalVirtualGoldByUser(1));
	}
}