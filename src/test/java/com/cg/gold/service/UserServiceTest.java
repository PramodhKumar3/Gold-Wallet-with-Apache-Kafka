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
import com.cg.gold.entity.Payment;
import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.User;
import com.cg.gold.entity.VirtualGoldHolding;
import com.cg.gold.exception.UserException;
import com.cg.gold.repository.AddressRepository;
import com.cg.gold.repository.PaymentRepository;
import com.cg.gold.repository.PhysicalGoldTransactionRepository;
import com.cg.gold.repository.TransactionHistoryRepository;
import com.cg.gold.repository.UserRepository;
import com.cg.gold.repository.VirtualGoldHoldingRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private AddressRepository addressRepository;
	@Mock
	private TransactionHistoryRepository transactionHistoryRepository;
	@Mock
	private PaymentRepository paymentRepository;
	@Mock
	private VirtualGoldHoldingRepository virtualGoldHoldingRepository;
	@Mock
	private PhysicalGoldTransactionRepository physicalGoldTransactionRepository;

	@InjectMocks
	private UserServiceImpl userService;

	private User user;
	private Address address;

	@BeforeEach
	void setUp() {
		address = new Address();
		address.setAddressId(1);
		address.setCity("Pune");
		address.setState("Maharashtra");

		user = new User();
		user.setUserId(1);
		user.setName("John");
		user.setEmail("john@example.com");
		user.setBalance(500.0);
		user.setAddress(address);
		user.setCreatedAt(LocalDateTime.now());
	}

	@Test
	void testGetAllUsers() {
		Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
		List<User> result = userService.getAllUsers();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetUserById_Success() throws UserException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		User result = userService.getUserById(1);
		Assertions.assertEquals("John", result.getName());
	}

	@Test
	void testGetUserById_NotFound() {
		Mockito.when(userRepository.findById(99)).thenReturn(Optional.empty());
		Assertions.assertThrows(UserException.class, () -> userService.getUserById(99));
	}

	@Test
	void testGetUserByUserName_Success() throws UserException {
		Mockito.when(userRepository.findByName("John")).thenReturn(Optional.of(user));
		User result = userService.getUserByUserName("John");
		Assertions.assertEquals("john@example.com", result.getEmail());
	}

	@Test
	void testGetUserByUserName_NotFound() {
		Mockito.when(userRepository.findByName("Jane")).thenReturn(Optional.empty());
		Assertions.assertThrows(UserException.class, () -> userService.getUserByUserName("Jane"));
	}

	@Test
	void testGetUsersByCity_Success() throws UserException {
		Mockito.when(userRepository.findByAddressCity("Pune")).thenReturn(List.of(user));
		List<User> result = userService.getUsersByCity("Pune");
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetUsersByCity_NotFound() {
		Mockito.when(userRepository.findByAddressCity("Mumbai")).thenReturn(List.of());
		Assertions.assertThrows(UserException.class, () -> userService.getUsersByCity("Mumbai"));
	}

	@Test
	void testGetUsersByState_Success() throws UserException {
		Mockito.when(userRepository.findByAddressState("Maharashtra")).thenReturn(List.of(user));
		List<User> result = userService.getUsersByState("Maharashtra");
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetUsersByState_NotFound() {
		Mockito.when(userRepository.findByAddressState("Goa")).thenReturn(List.of());
		Assertions.assertThrows(UserException.class, () -> userService.getUsersByState("Goa"));
	}

	@Test
	void testGetUserBalanceById_Success() throws UserException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Double balance = userService.getUserBalanceById(1);
		Assertions.assertEquals(500.0, balance);
	}

	@Test
	void testGetTotalVirtualGoldHoldingsByUserId_Success() throws UserException {
		VirtualGoldHolding holding = new VirtualGoldHolding();
		holding.setQuantity(10.0);
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(virtualGoldHoldingRepository.findByUserUserId(1)).thenReturn(List.of(holding));
		Double total = userService.getTotalVirtualGoldHoldingsByUserId(1);
		Assertions.assertEquals(10.0, total);
	}

	@Test
	void testGetTotalPhysicalGoldHoldingsByUserId_Success() throws UserException {
		PhysicalGoldTransaction transaction = new PhysicalGoldTransaction();
		transaction.setQuantity(5.0);
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(physicalGoldTransactionRepository.findByUserUserId(1)).thenReturn(List.of(transaction));
		Double total = userService.getTotalPhysicalGoldHoldingsByUserId(1);
		Assertions.assertEquals(5.0, total);
	}

	@Test
	void testGetUserTransactionHistory_Success() throws UserException {
		TransactionHistory history = new TransactionHistory();
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(transactionHistoryRepository.findByUserUserId(1)).thenReturn(List.of(history));
		List<TransactionHistory> result = userService.getUserTransactionHistory(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetUserPayments_Success() throws UserException {
		Payment payment = new Payment();
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(paymentRepository.findByUserUserId(1)).thenReturn(List.of(payment));
		List<Payment> result = userService.getUserPayments(1);
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testCreateUser_Success() throws UserException {
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		userService.createUser(user);
		Mockito.verify(userRepository).save(Mockito.any(User.class));
	}

	@Test
	void testCreateUser_MinimumBalanceViolation() {
		user.setBalance(50.0);
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		Assertions.assertThrows(UserException.class, () -> userService.createUser(user));
	}

	@Test
	void testUpdateUser_Success() throws UserException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		userService.updateUser(1, user);
		Mockito.verify(userRepository).save(Mockito.any(User.class));
	}

	@Test
	void testUpdateUserBalance_Success() throws UserException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		userService.updateUserBalance(1, 1000.0);
		Mockito.verify(userRepository).save(Mockito.any(User.class));
	}

	@Test
	void testUpdateUserAddress_Success() throws UserException {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(addressRepository.findById(1)).thenReturn(Optional.of(address));
		userService.updateUserAddress(1, 1);
		Mockito.verify(userRepository).save(Mockito.any(User.class));
	}
}
