package com.cg.gold.service;

import java.util.List;

import com.cg.gold.entity.Vendor;
import com.cg.gold.exception.VendorException;

public interface VendorService {

	public List<Vendor> getAllVendors();

	public Vendor getVendorById(Integer vendorId) throws VendorException;

	public Vendor getVendorByVendorName(String vendorName) throws VendorException;

	public void addVendor(Vendor newVendor);

	public void updateVendor(Integer vendorId, Vendor updatedVendor) throws VendorException;

	public void updateVendorTotalGoldQuantityById(Integer vendorId, Double quantity) throws VendorException;

	public void updateAllVendorCurrentGoldPriceWithNewPrice(Double newPrice);
}
