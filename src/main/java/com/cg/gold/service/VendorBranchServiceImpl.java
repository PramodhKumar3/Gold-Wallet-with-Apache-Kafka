package com.cg.gold.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.gold.entity.Address;
import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.Vendor;
import com.cg.gold.entity.VendorBranch;
import com.cg.gold.exception.VendorBranchException;
import com.cg.gold.repository.AddressRepository;
import com.cg.gold.repository.TransactionHistoryRepository;
import com.cg.gold.repository.VendorBranchRepository;
import com.cg.gold.repository.VendorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VendorBranchServiceImpl implements VendorBranchService {

	@Autowired
	private VendorBranchRepository vendorBranchRepository;

	@Autowired
	private TransactionHistoryRepository transactionHistoryRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public List<VendorBranch> getAllVendorBranches() {
		return vendorBranchRepository.findAll();
	}

	@Override
	public VendorBranch getVendorBranchByBranchId(Integer branchId) throws VendorBranchException {
		return vendorBranchRepository.findById(branchId)
				.orElseThrow(() -> new VendorBranchException("VendorBranchService.BRANCH_NOT_FOUND"));
	}

	@Override
	public List<VendorBranch> getVendorBranchesByVendorId(Integer vendorId) throws VendorBranchException {
		vendorRepository.findById(vendorId)
				.orElseThrow(() -> new VendorBranchException("VendorService.VENDOR_NOT_FOUND"));
		List<VendorBranch> branches = vendorBranchRepository.findByVendorVendorId(vendorId);
		if (branches.isEmpty())
			throw new VendorBranchException("VendorBranchService.VENDOR_BRANCH_NOT_FOUND");
		return branches;
	}

	@Override
	public List<VendorBranch> getVendorBranchesByCity(String city) throws VendorBranchException {
		List<VendorBranch> branches = vendorBranchRepository.findByAddressCityIgnoreCase(city);
		if (branches.isEmpty())
			throw new VendorBranchException("VendorBranchService.CITY_NOT_FOUND");
		return branches;
	}

	@Override
	public List<VendorBranch> getVendorBranchesByState(String state) throws VendorBranchException {
		List<VendorBranch> branches = vendorBranchRepository.findByAddressStateIgnoreCase(state);
		if (branches.isEmpty())
			throw new VendorBranchException("VendorBranchService.STATE_NOT_FOUND");
		return branches;
	}

	@Override
	public List<VendorBranch> getVendorBranchesByCountry(String country) throws VendorBranchException {
		List<VendorBranch> branches = vendorBranchRepository.findByAddressCountryIgnoreCase(country);
		if (branches.isEmpty())
			throw new VendorBranchException("VendorBranchService.COUNTRY_NOT_FOUND");
		return branches;
	}

	@Override
	public List<TransactionHistory> getVendorBranchTransactionsByBranchId(Integer branchId)
			throws VendorBranchException {
		VendorBranch branch = getVendorBranchByBranchId(branchId);
		if (branch == null)
			throw new VendorBranchException("VendorBranchService.BRANCH_NOT_FOUND");
		return transactionHistoryRepository.findByBranch(branch);
	}

	@Override
	public void addBranch(VendorBranch newVendorBranch) throws VendorBranchException {
		Integer vendorId = newVendorBranch.getVendor().getVendorId();
		Vendor vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new VendorBranchException("VendorService.VENDOR_NOT_FOUND"));
		Integer addressId = newVendorBranch.getAddress().getAddressId();
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new VendorBranchException("AddressService.ADDRESS_NOT_FOUND"));
		newVendorBranch.setVendor(vendor);
		newVendorBranch.setAddress(address);
		vendorBranchRepository.save(newVendorBranch);
	}

	@Override
	public void updateBranch(Integer branchId, VendorBranch updatedBranch) throws VendorBranchException {
		VendorBranch existing = vendorBranchRepository.findById(branchId)
				.orElseThrow(() -> new VendorBranchException("VendorBranchService.BRANCH_NOT_FOUND"));
		Integer vendorId = updatedBranch.getVendor().getVendorId();
		vendorRepository.findById(vendorId)
				.orElseThrow(() -> new VendorBranchException("VendorService.VENDOR_NOT_FOUND"));
		Integer addressId = updatedBranch.getAddress().getAddressId();
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new VendorBranchException("AddressService.ADDRESS_NOT_FOUND"));
		existing.setAddress(address);
		existing.setQuantity(updatedBranch.getQuantity());
		vendorBranchRepository.save(existing);
	}

	@Override
	public void transferGold(Integer sourceBranchId, Integer destinationBranchId, Double quantity)
			throws VendorBranchException {
		VendorBranch source = vendorBranchRepository.findById(sourceBranchId)
				.orElseThrow(() -> new VendorBranchException("VendorBranchService.SOURCE_NOT_FOUND"));
		VendorBranch destination = vendorBranchRepository.findById(destinationBranchId)
				.orElseThrow(() -> new VendorBranchException("VendorBranchService.DESTINATION_NOT_FOUND"));

		if (source.getQuantity() < quantity)
			throw new VendorBranchException("VendorBranchService.INSUFFICIENT_BALANCE");

		source.setQuantity(source.getQuantity() - quantity);
		destination.setQuantity(destination.getQuantity() + quantity);

		vendorBranchRepository.save(source);
		vendorBranchRepository.save(destination);
	}

}
