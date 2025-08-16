package com.cg.gold.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.gold.entity.Address;
import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.entity.User;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.exception.PhysicalGoldTransactionException;
import com.cg.gold.repository.AddressRepository;
import com.cg.gold.repository.PhysicalGoldTransactionRepository;
import com.cg.gold.repository.UserRepository;
import com.cg.gold.repository.VendorBranchRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PhysicalGoldTransactionServiceImpl implements PhysicalGoldTransactionService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PhysicalGoldTransactionRepository physicalGoldTransactionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VendorBranchRepository vendorBranchRepository;

	@Override
	public List<PhysicalGoldTransaction> getAllPhysicalGoldTransactions() {
		return physicalGoldTransactionRepository.findAll();
	}

	@Override
	public PhysicalGoldTransaction getPhysicalGoldTransactionById(Integer transactionId)
			throws PhysicalGoldTransactionException {
		return physicalGoldTransactionRepository.findById(transactionId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("PhysicalGoldService.TRANSACTION_NOT_FOUND"));
	}

	@Override
	public List<PhysicalGoldTransaction> getPhysicalGoldTransactionByUserId(Integer userId)
			throws PhysicalGoldTransactionException {
		userRepository.findById(userId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("UserService.USER_NOT_FOUND"));
		List<PhysicalGoldTransaction> transactions = physicalGoldTransactionRepository.findAllByUserUserId(userId);
		if (transactions.isEmpty()) {
			throw new PhysicalGoldTransactionException("PhysicalGoldService.TRANSACTION_USER_FOUND");
		}
		return transactions;
	}

	@Override
	public List<PhysicalGoldTransaction> getPhysicalGoldTransactionByBranchId(Integer branchId)
			throws PhysicalGoldTransactionException {
		vendorBranchRepository.findById(branchId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("VendorBranchService.BRANCH_NOT_FOUND"));
		List<PhysicalGoldTransaction> transactions = physicalGoldTransactionRepository
				.findAllByBranchBranchId(branchId);
		if (transactions.isEmpty()) {
			throw new PhysicalGoldTransactionException("PhysicalGoldService.NO_TRANSACTION_FOR_BRANCH");
		}
		return transactions;
	}

	@Override
	public List<PhysicalGoldTransaction> getAllPhysicalGoldTransactionByDeliveryCity(String city)
			throws PhysicalGoldTransactionException {
		List<PhysicalGoldTransaction> transactions = physicalGoldTransactionRepository.findByDeliveryAddressCity(city);
		if (transactions == null || transactions.isEmpty()) {
			throw new PhysicalGoldTransactionException("PhysicalGoldService.NO_TRANSACTION_FOR_CITY");
		}
		return transactions;
	}

	@Override
	public List<PhysicalGoldTransaction> getAllPhysicalGoldTransactionByDeliveryState(String state)
			throws PhysicalGoldTransactionException {
		List<PhysicalGoldTransaction> transactions = physicalGoldTransactionRepository
				.findByDeliveryAddressState(state);
		if (transactions == null || transactions.isEmpty()) {
			throw new PhysicalGoldTransactionException("PhysicalGoldService.NO_TRANSACTION_FOR_STATE");
		}
		return transactions;
	}

	@Override
	public void addPhysicalGoldTransaction(PhysicalGoldTransaction newPhysicalGoldTransaction)
			throws PhysicalGoldTransactionException {
		Integer branchId = newPhysicalGoldTransaction.getBranch().getBranchId();
		vendorBranchRepository.findById(branchId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("VendorBranchService.BRANCH_NOT_FOUND"));
		Integer addressId = newPhysicalGoldTransaction.getDeliveryAddress().getAddressId();
		addressRepository.findById(addressId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("AddressService.ADDRESS_NOT_FOUND"));
		Integer userId = newPhysicalGoldTransaction.getUser().getUserId();
		userRepository.findById(userId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("UserService.USER_NOT_FOUND"));
		newPhysicalGoldTransaction.setCreatedAt(LocalDateTime.now());
		physicalGoldTransactionRepository.save(newPhysicalGoldTransaction);
	}

	@Override
	public Integer updatePhysicalGoldTransaction(Integer transactionId, PhysicalGoldTransaction updatedTransaction)
			throws PhysicalGoldTransactionException {
		PhysicalGoldTransaction existing = physicalGoldTransactionRepository.findById(transactionId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("PhysicalGoldService.TRANSACTION_NOT_FOUND"));
		Integer branchId = updatedTransaction.getBranch().getBranchId();
		VendorBranch vendorBranch = vendorBranchRepository.findById(branchId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("VendorBranchService.BRANCH_NOT_FOUND"));
		Integer addressId = updatedTransaction.getDeliveryAddress().getAddressId();
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("AddressService.ADDRESS_NOT_FOUND"));
		Integer userId = updatedTransaction.getUser().getUserId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("UserService.USER_NOT_FOUND"));
		existing.setUser(user);
		existing.setBranch(vendorBranch);
		existing.setQuantity(updatedTransaction.getQuantity());
		existing.setDeliveryAddress(address);
		existing.setCreatedAt(LocalDateTime.now());

		physicalGoldTransactionRepository.save(existing);
		return existing.getTransactionId();
	}

	@Override
	public Double getTotalPhysicalGoldByUser(Integer userId) throws PhysicalGoldTransactionException {
		userRepository.findById(userId)
				.orElseThrow(() -> new PhysicalGoldTransactionException("UserService.USER_NOT_FOUND"));
		return physicalGoldTransactionRepository.getTotalQuantityByUserId(userId);
	}

}
