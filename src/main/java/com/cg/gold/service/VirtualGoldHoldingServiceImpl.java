package com.cg.gold.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.entity.User;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.entity.VirtualGoldHolding;
import com.cg.gold.exception.VirtualGoldHoldingException;
import com.cg.gold.repository.PhysicalGoldTransactionRepository;
import com.cg.gold.repository.UserRepository;
import com.cg.gold.repository.VendorBranchRepository;
import com.cg.gold.repository.VendorRepository;
import com.cg.gold.repository.VirtualGoldHoldingRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VirtualGoldHoldingServiceImpl implements VirtualGoldHoldingService {

	@Autowired
	private VirtualGoldHoldingRepository virtualGoldHoldingRepository;

	@Autowired
	private PhysicalGoldTransactionRepository physicalGoldTransactionRepository;

	@Autowired
	private VendorBranchRepository vendorBranchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public List<VirtualGoldHolding> getAllVirtualGoldHoldings() {
		return virtualGoldHoldingRepository.findAll();
	}

	@Override
	public List<VirtualGoldHolding> getAllVirtualGoldHoldingByUserId(Integer userId)
			throws VirtualGoldHoldingException {
		userRepository.findById(userId)
				.orElseThrow(() -> new VirtualGoldHoldingException("UserService.USER_NOT_FOUND"));
		List<VirtualGoldHolding> holdings = virtualGoldHoldingRepository.findByUserUserId(userId);
		if (holdings.isEmpty())
			throw new VirtualGoldHoldingException("VirtualGoldService.USER_NOT_FOUND");
		return holdings;
	}

	@Override
	public VirtualGoldHolding getVirtualGoldHoldingById(Integer holdingId) throws VirtualGoldHoldingException {
		return virtualGoldHoldingRepository.findById(holdingId)
				.orElseThrow(() -> new VirtualGoldHoldingException("VirtualGoldService.HOLDING_NOT_FOUND"));
	}

	@Override
	public List<VirtualGoldHolding> getVirtualGoldHoldingByUserAndVendor(Integer userId, Integer vendorId)
			throws VirtualGoldHoldingException {
		userRepository.findById(userId)
				.orElseThrow(() -> new VirtualGoldHoldingException("UserService.USER_NOT_FOUND"));
		vendorRepository.findById(vendorId)
				.orElseThrow(() -> new VirtualGoldHoldingException("VendorService.VENDOR_NOT_FOUND"));
		List<VirtualGoldHolding> holdings = virtualGoldHoldingRepository.findByUserUserIdAndBranchVendorVendorId(userId,
				vendorId);
		if (holdings.isEmpty())
			throw new VirtualGoldHoldingException("VirtualGoldService.USER_AND_VENDOR_NOT_FOUND");
		return holdings;
	}

	@Override
	public void addVirtualGoldHolding(VirtualGoldHolding newVirtualGoldHolding) throws VirtualGoldHoldingException {
		Integer userId = newVirtualGoldHolding.getUser().getUserId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new VirtualGoldHoldingException("UserService.USER_NOT_FOUND"));
		Integer branchId = newVirtualGoldHolding.getBranch().getBranchId();
		VendorBranch branch = vendorBranchRepository.findById(branchId)
				.orElseThrow(() -> new VirtualGoldHoldingException("VendorBranchService.BRANCH_NOT_FOUND"));
		newVirtualGoldHolding.setUser(user);
		newVirtualGoldHolding.setBranch(branch);
		newVirtualGoldHolding.setCreatedAt(LocalDateTime.now());
		virtualGoldHoldingRepository.save(newVirtualGoldHolding);
	}

	@Override
	public void convertVirtualToPhysical(Integer holdingId) throws VirtualGoldHoldingException {
		VirtualGoldHolding holding = virtualGoldHoldingRepository.findById(holdingId)
				.orElseThrow(() -> new VirtualGoldHoldingException("VirtualGoldService.HOLDING_NOT_FOUND"));
		physicalGoldTransactionRepository.save(new PhysicalGoldTransaction(holding.getUser(), holding.getBranch(),
				holding.getQuantity(), holding.getUser().getAddress(), LocalDateTime.now()));
		virtualGoldHoldingRepository.delete(holding);
	}

	@Override
	public Integer updateVirtualGoldHolding(Integer holdingId, VirtualGoldHolding updatedHolding)
			throws VirtualGoldHoldingException {
		VirtualGoldHolding existing = virtualGoldHoldingRepository.findById(holdingId)
				.orElseThrow(() -> new VirtualGoldHoldingException("VirtualGoldService.HOLDING_NOT_FOUND"));
		Integer userId = updatedHolding.getUser().getUserId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new VirtualGoldHoldingException("UserService.USER_NOT_FOUND"));
		Integer branchId = updatedHolding.getBranch().getBranchId();
		VendorBranch branch = vendorBranchRepository.findById(branchId)
				.orElseThrow(() -> new VirtualGoldHoldingException("VendorBranchService.BRANCH_NOT_FOUND"));
		existing.setUser(user);
		existing.setBranch(branch);
		existing.setQuantity(updatedHolding.getQuantity());
		virtualGoldHoldingRepository.save(existing);
		return existing.getHoldingId();
	}

	@Override
	public Double getTotalVirtualGoldByUser(Integer userId) throws VirtualGoldHoldingException {
		userRepository.findById(userId)
				.orElseThrow(() -> new VirtualGoldHoldingException("UserService.USER_NOT_FOUND"));
		return virtualGoldHoldingRepository.getTotalQuantityByUserId(userId);
	}

}
