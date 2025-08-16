package com.cg.gold.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private TransactionHistoryRepository transactionHistoryRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private VirtualGoldHoldingRepository virtualGoldHoldingRepository;

	@Autowired
	private PhysicalGoldTransactionRepository physicalGoldTransactionRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(Integer userId) throws UserException {
		return userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
	}

	@Override
	public User getUserByUserName(String name) throws UserException {
		return userRepository.findByName(name).orElseThrow(() -> new UserException("UserService.NAME_NOT_FOUND"));
	}

	@Override
	public List<User> getUsersByCity(String city) throws UserException {
		List<User> users = userRepository.findByAddressCity(city);
		if (users.isEmpty())
			throw new UserException("UserService.CITY_NOT_FOUND");
		return users;
	}

	@Override
	public List<User> getUsersByState(String state) throws UserException {
		List<User> users = userRepository.findByAddressState(state);
		if (users.isEmpty())
			throw new UserException("UserService.STATE_NOT_FOUND");
		return users;
	}

	@Override
	public Double getUserBalanceById(Integer userId) throws UserException {
		userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		return getUserById(userId).getBalance();
	}

	@Override
	public Double getTotalVirtualGoldHoldingsByUserId(Integer userId) throws UserException {
		userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		List<VirtualGoldHolding> holdings = virtualGoldHoldingRepository.findByUserUserId(userId);
		if (holdings.isEmpty())
			return 0.0;
		return holdings.stream().mapToDouble(VirtualGoldHolding::getQuantity).sum();
	}

	@Override
	public Double getTotalPhysicalGoldHoldingsByUserId(Integer userId) throws UserException {
		userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		List<PhysicalGoldTransaction> transactions = physicalGoldTransactionRepository.findByUserUserId(userId);
		if (transactions.isEmpty())
			return 0.0;
		return transactions.stream().mapToDouble(PhysicalGoldTransaction::getQuantity).sum();
	}

	@Override
	public List<TransactionHistory> getUserTransactionHistory(Integer userId) throws UserException {
		userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		return transactionHistoryRepository.findByUserUserId(userId);
	}

	@Override
	public List<Payment> getUserPayments(Integer userId) throws UserException {
		userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		return paymentRepository.findByUserUserId(userId);
	}

	@Override
	public void createUser(User newUser) throws UserException {
		Integer addressId = newUser.getAddress().getAddressId();
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new UserException("AddressService.ADDRESS_NOT_FOUND"));
		newUser.setAddress(address);
		if (newUser.getBalance() < 100)
			throw new UserException("UserService.MINIMUM_BALANCE");
		newUser.setCreatedAt(LocalDateTime.now());
		userRepository.save(newUser);
	}

	@Override
	public void updateUser(Integer userId, User userDetails) throws UserException {
		User existing = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		existing.setName(userDetails.getName());
		existing.setEmail(userDetails.getEmail());
		existing.setBalance(userDetails.getBalance());
		Address address = userDetails.getAddress();
		existing.setAddress(address);
//		if (address != null) {
//			addressRepository.save(address);
//			existing.setAddress(address);
//		}
		userRepository.save(existing);
	}

	@Override
	public void updateUserBalance(Integer userId, Double amount) throws UserException {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		Integer addressId = user.getAddress().getAddressId();
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new UserException("AddressService.ADDRESS_NOT_FOUND"));
		user.setAddress(address);
		user.setBalance(amount);
		userRepository.save(user);
	}

	@Override
	public void updateUserAddress(Integer userId, Integer addressId) throws UserException {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("UserService.USER_NOT_FOUND"));
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new UserException("AddressService.ADDRESS_NOT_FOUND"));
		user.setAddress(address);
		userRepository.save(user);
	}

}
