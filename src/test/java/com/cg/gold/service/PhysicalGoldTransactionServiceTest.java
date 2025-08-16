package com.cg.gold.service;

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
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.gold.entity.Address;
import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.entity.User;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.exception.PhysicalGoldTransactionException;
import com.cg.gold.repository.AddressRepository;
import com.cg.gold.repository.PhysicalGoldTransactionRepository;
import com.cg.gold.repository.UserRepository;
import com.cg.gold.repository.VendorBranchRepository;

@ExtendWith(MockitoExtension.class)
public class PhysicalGoldTransactionServiceTest {

	@Mock
	private PhysicalGoldTransactionRepository transactionRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private VendorBranchRepository vendorBranchRepository;

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private PhysicalGoldTransactionServiceImpl transactionService;

	private User user;
	private VendorBranch branch;
	private Address address;
	private PhysicalGoldTransaction transaction;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setUserId(1);

		branch = new VendorBranch();
		branch.setBranchId(1);

		address = new Address();
		address.setAddressId(1);

		transaction = new PhysicalGoldTransaction();
		transaction.setTransactionId(1);
		transaction.setUser(user);
		transaction.setBranch(branch);
		transaction.setQuantity(10.0);
		transaction.setDeliveryAddress(address);
		transaction.setCreatedAt(LocalDateTime.now());
	}

	@Test
	void testGetAllPhysicalGoldTransactions() {
		Mockito.when(transactionRepository.findAll()).thenReturn(List.of(transaction));

		List<PhysicalGoldTransaction> result = transactionService.getAllPhysicalGoldTransactions();

		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetPhysicalGoldTransactionById_Success() throws PhysicalGoldTransactionException {
		Mockito.when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

		PhysicalGoldTransaction result = transactionService.getPhysicalGoldTransactionById(1);

		Assertions.assertEquals(10.0, result.getQuantity());
	}

	@Test
	void testGetPhysicalGoldTransactionById_NotFound() {
		Mockito.when(transactionRepository.findById(99)).thenReturn(Optional.empty());

		Assertions.assertThrows(PhysicalGoldTransactionException.class,
				() -> transactionService.getPhysicalGoldTransactionById(99));
	}

	@Test
	void testGetPhysicalGoldTransactionByUserId_Success() throws PhysicalGoldTransactionException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(transactionRepository.findAllByUserUserId(1)).thenReturn(List.of(transaction));

		List<PhysicalGoldTransaction> result = transactionService.getPhysicalGoldTransactionByUserId(1);

		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetPhysicalGoldTransactionByUserId_NoTransactions() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(transactionRepository.findAllByUserUserId(1)).thenReturn(List.of());

		Assertions.assertThrows(PhysicalGoldTransactionException.class,
				() -> transactionService.getPhysicalGoldTransactionByUserId(1));
	}

	@Test
	void testGetPhysicalGoldTransactionByBranchId_Success() throws PhysicalGoldTransactionException {
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		Mockito.when(transactionRepository.findAllByBranchBranchId(1)).thenReturn(List.of(transaction));

		List<PhysicalGoldTransaction> result = transactionService.getPhysicalGoldTransactionByBranchId(1);

		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetPhysicalGoldTransactionByBranchId_NoTransactions() {
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		Mockito.when(transactionRepository.findAllByBranchBranchId(1)).thenReturn(List.of());

		Assertions.assertThrows(PhysicalGoldTransactionException.class,
				() -> transactionService.getPhysicalGoldTransactionByBranchId(1));
	}

	@Test
	void testGetAllPhysicalGoldTransactionByDeliveryCity_Success() throws PhysicalGoldTransactionException {
		Mockito.when(transactionRepository.findByDeliveryAddressCity("Pune")).thenReturn(List.of(transaction));

		List<PhysicalGoldTransaction> result = transactionService.getAllPhysicalGoldTransactionByDeliveryCity("Pune");

		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllPhysicalGoldTransactionByDeliveryCity_NoTransactions() {
		Mockito.when(transactionRepository.findByDeliveryAddressCity("Mumbai")).thenReturn(List.of());

		Assertions.assertThrows(PhysicalGoldTransactionException.class,
				() -> transactionService.getAllPhysicalGoldTransactionByDeliveryCity("Mumbai"));
	}

	@Test
	void testGetAllPhysicalGoldTransactionByDeliveryState_Success() throws PhysicalGoldTransactionException {
		Mockito.when(transactionRepository.findByDeliveryAddressState("Maharashtra")).thenReturn(List.of(transaction));

		List<PhysicalGoldTransaction> result = transactionService
				.getAllPhysicalGoldTransactionByDeliveryState("Maharashtra");

		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllPhysicalGoldTransactionByDeliveryState_NoTransactions() {
		Mockito.when(transactionRepository.findByDeliveryAddressState("Karnataka")).thenReturn(List.of());

		Assertions.assertThrows(PhysicalGoldTransactionException.class,
				() -> transactionService.getAllPhysicalGoldTransactionByDeliveryState("Karnataka"));
	}

	@Test
	void testAddPhysicalGoldTransaction_Success() throws PhysicalGoldTransactionException {
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

		transactionService.addPhysicalGoldTransaction(transaction);

		Mockito.verify(transactionRepository).save(Mockito.any(PhysicalGoldTransaction.class));
	}

	@Test
	void testUpdatePhysicalGoldTransaction_Success() throws PhysicalGoldTransactionException {
		Mockito.when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
		Mockito.when(vendorBranchRepository.findById(1)).thenReturn(Optional.of(branch));
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

		Integer result = transactionService.updatePhysicalGoldTransaction(1, transaction);

		Assertions.assertEquals(1, result);
		Mockito.verify(transactionRepository).save(Mockito.any(PhysicalGoldTransaction.class));
	}

	@Test
	void testGetTotalPhysicalGoldByUser_Success() throws PhysicalGoldTransactionException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(transactionRepository.getTotalQuantityByUserId(1)).thenReturn(25.0);

		Double result = transactionService.getTotalPhysicalGoldByUser(1);

		Assertions.assertEquals(25.0, result);
	}

	@Test
	void testGetTotalPhysicalGoldByUser_UserNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());

		Assertions.assertThrows(PhysicalGoldTransactionException.class,
				() -> transactionService.getTotalPhysicalGoldByUser(1));
	}
}
