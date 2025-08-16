package com.cg.gold.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Collections;
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

import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.TransactionHistory.TransactionStatus;
import com.cg.gold.entity.TransactionHistory.TransactionType;
import com.cg.gold.entity.User;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.exception.TransactionHistoryException;
import com.cg.gold.repository.TransactionHistoryRepository;
import com.cg.gold.repository.UserRepository;
import com.cg.gold.repository.VendorBranchRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionHistoryServiceTest {

	@Mock
	private TransactionHistoryRepository transactionHistoryRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private VendorBranchRepository vendorBranchRepository;

	@InjectMocks
	private TransactionHistoryServiceImpl transactionHistoryService;

	private User user;
	private VendorBranch branch;
	private TransactionHistory transaction;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setUserId(1);

		branch = new VendorBranch();
		branch.setBranchId(101);

		transaction = new TransactionHistory();
		transaction.setTransactionId(1001);
		transaction.setUser(user);
		transaction.setBranch(branch);
		transaction.setTransactionType(TransactionType.Buy);
		transaction.setTransactionStatus(TransactionStatus.Success);
		transaction.setCreatedAt(LocalDateTime.now());
	}

	@Test
	void testGetAllTransactionHistory() {
		Mockito.when(transactionHistoryRepository.findAll()).thenReturn(List.of(transaction));
		List<TransactionHistory> result = transactionHistoryService.getAllTransactionHistory();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetTransactionHistoryById_Success() throws TransactionHistoryException {
		Mockito.when(transactionHistoryRepository.findById(1001)).thenReturn(Optional.of(transaction));
		TransactionHistory result = transactionHistoryService.getTransactionHistoryById(1001);
		Assertions.assertEquals(1001, result.getTransactionId());
	}

	@Test
	void testGetTransactionHistoryById_NotFound() {
		Mockito.when(transactionHistoryRepository.findById(9999)).thenReturn(Optional.empty());
		Assertions.assertThrows(TransactionHistoryException.class,
				() -> transactionHistoryService.getTransactionHistoryById(9999));
	}

	@Test
	void testGetAllTransactionHistoryByUserId_Success() throws TransactionHistoryException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(transactionHistoryRepository.findByUserUserId(1)).thenReturn(List.of(transaction));
		List<TransactionHistory> result = transactionHistoryService.getAllTransactionHistoryByUserId(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllTransactionHistoryByUserId_UserNotFound() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(TransactionHistoryException.class,
				() -> transactionHistoryService.getAllTransactionHistoryByUserId(1));
	}

	@Test
	void testGetAllTransactionHistoryByUserId_NoTransactions() {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(transactionHistoryRepository.findByUserUserId(1)).thenReturn(Collections.emptyList());
		Assertions.assertThrows(TransactionHistoryException.class,
				() -> transactionHistoryService.getAllTransactionHistoryByUserId(1));
	}

	@Test
	void testGetAllSuccessTransactionHistory() {
		Mockito.when(transactionHistoryRepository.findByTransactionStatus(TransactionStatus.Success))
				.thenReturn(List.of(transaction));
		List<TransactionHistory> result = transactionHistoryService.getAllSuccessTransactionHistory();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllFailedTransactionHistory() {
		Mockito.when(transactionHistoryRepository.findByTransactionStatus(TransactionStatus.Failed))
				.thenReturn(Collections.emptyList());
		List<TransactionHistory> result = transactionHistoryService.getAllFailedTransactionHistory();
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void testGetAllTransactionHistoryByTransactionType_Success() throws TransactionHistoryException {
		Mockito.when(transactionHistoryRepository.findByTransactionType(TransactionType.Buy))
				.thenReturn(List.of(transaction));
		List<TransactionHistory> result = transactionHistoryService
				.getAllTransactionHistoryByTransactionType(TransactionType.Buy);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetAllTransactionHistoryByTransactionType_InvalidType() {
		Assertions.assertThrows(TransactionHistoryException.class,
				() -> transactionHistoryService.getAllTransactionHistoryByTransactionType(null));
	}

	@Test
	void testGetAllTransactionHistoryByTransactionType_NoTransactions() {
		Mockito.when(transactionHistoryRepository.findByTransactionType(TransactionType.Sell))
				.thenReturn(Collections.emptyList());
		Assertions.assertThrows(TransactionHistoryException.class,
				() -> transactionHistoryService.getAllTransactionHistoryByTransactionType(TransactionType.Sell));
	}

	@Test
	void testAddTransactionHistory_Success() throws TransactionHistoryException {
		Mockito.when(vendorBranchRepository.findById(101)).thenReturn(Optional.of(branch));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		transactionHistoryService.addTransactionHistory(transaction);
		Mockito.verify(transactionHistoryRepository, times(1)).save(any(TransactionHistory.class));
	}

	@Test
	void testAddTransactionHistory_BranchNotFound() {
		Mockito.when(vendorBranchRepository.findById(101)).thenReturn(Optional.empty());
		Assertions.assertThrows(TransactionHistoryException.class,
				() -> transactionHistoryService.addTransactionHistory(transaction));
	}

	@Test
	void testAddTransactionHistory_UserNotFound() {
		Mockito.when(vendorBranchRepository.findById(101)).thenReturn(Optional.of(branch));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(TransactionHistoryException.class,
				() -> transactionHistoryService.addTransactionHistory(transaction));
	}

	@Test
	void testGetTransactionsByUserSorted() {
		Mockito.when(transactionHistoryRepository.findByUserUserIdOrderByCreatedAtDesc(1))
				.thenReturn(List.of(transaction));
		List<TransactionHistory> result = transactionHistoryService.getTransactionsByUserSorted(1);
		Assertions.assertEquals(1, result.size());
	}
}