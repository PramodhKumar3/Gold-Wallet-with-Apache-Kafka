package com.cg.gold.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.gold.entity.Vendor;
import com.cg.gold.exception.VendorException;
import com.cg.gold.repository.VendorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public List<Vendor> getAllVendors() {
		return vendorRepository.findAll();
	}

	@Override
	public Vendor getVendorById(Integer vendorId) throws VendorException {
		return vendorRepository.findById(vendorId)
				.orElseThrow(() -> new VendorException("VendorService.VENDOR_NOT_FOUND"));
	}

	@Override
	public Vendor getVendorByVendorName(String vendorName) throws VendorException {
		return vendorRepository.findByVendorNameIgnoreCase(vendorName)
				.orElseThrow(() -> new VendorException("VendorService.VENDOR_NAME_NOT_FOUND"));
	}

	@Override
	public void addVendor(Vendor newVendor) {
		newVendor.setCreatedAt(LocalDateTime.now());
		vendorRepository.save(newVendor);
	}

	@Override
	public void updateVendor(Integer vendorId, Vendor updatedVendor) throws VendorException {
		Vendor existing = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new VendorException("VendorService.VENDOR_NOT_FOUND"));

		existing.setVendorName(updatedVendor.getVendorName());
		existing.setDescription(updatedVendor.getDescription());
		existing.setContactPersonName(updatedVendor.getContactPersonName());
		existing.setContactEmail(updatedVendor.getContactEmail());
		existing.setContactPhone(updatedVendor.getContactPhone());
		existing.setWebsiteUrl(updatedVendor.getWebsiteUrl());
		existing.setTotalGoldQuantity(updatedVendor.getTotalGoldQuantity());
		existing.setCurrentGoldPrice(updatedVendor.getCurrentGoldPrice());

		vendorRepository.save(existing);
	}

	@Override
	public void updateVendorTotalGoldQuantityById(Integer vendorId, Double quantity) throws VendorException {
		Vendor vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new VendorException("VendorService.VENDOR_NOT_FOUND"));
		vendor.setTotalGoldQuantity(quantity);
		vendorRepository.save(vendor);
	}

	@Override
	public void updateAllVendorCurrentGoldPriceWithNewPrice(Double newPrice) {
		List<Vendor> vendors = vendorRepository.findAll();
		for (Vendor vendor : vendors)
			vendor.setCurrentGoldPrice(newPrice);
		vendorRepository.saveAll(vendors);
	}

}
