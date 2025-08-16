package com.cg.gold.service;

import java.util.List;

import com.cg.gold.entity.VirtualGoldHolding;
import com.cg.gold.exception.VirtualGoldHoldingException;

public interface VirtualGoldHoldingService {

	public List<VirtualGoldHolding> getAllVirtualGoldHoldings();

	public List<VirtualGoldHolding> getAllVirtualGoldHoldingByUserId(Integer userId) throws VirtualGoldHoldingException;

	public VirtualGoldHolding getVirtualGoldHoldingById(Integer holdingId) throws VirtualGoldHoldingException;

	public List<VirtualGoldHolding> getVirtualGoldHoldingByUserAndVendor(Integer userId, Integer vendorId)
			throws VirtualGoldHoldingException;

	public void addVirtualGoldHolding(VirtualGoldHolding newVirtualGoldHolding) throws VirtualGoldHoldingException;

	public void convertVirtualToPhysical(Integer holdingId) throws VirtualGoldHoldingException;

	public Integer updateVirtualGoldHolding(Integer holdingId, VirtualGoldHolding updatedHolding)
			throws VirtualGoldHoldingException;

	public Double getTotalVirtualGoldByUser(Integer userId) throws VirtualGoldHoldingException;
}
