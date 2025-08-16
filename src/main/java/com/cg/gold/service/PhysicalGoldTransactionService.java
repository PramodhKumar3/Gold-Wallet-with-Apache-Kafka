package com.cg.gold.service;

import java.util.List;

import com.cg.gold.entity.PhysicalGoldTransaction;
import com.cg.gold.exception.PhysicalGoldTransactionException;

public interface PhysicalGoldTransactionService {

	public List<PhysicalGoldTransaction> getAllPhysicalGoldTransactions();

	public PhysicalGoldTransaction getPhysicalGoldTransactionById(Integer transactionId)
			throws PhysicalGoldTransactionException;

	public List<PhysicalGoldTransaction> getPhysicalGoldTransactionByUserId(Integer userId)
			throws PhysicalGoldTransactionException;

	public List<PhysicalGoldTransaction> getPhysicalGoldTransactionByBranchId(Integer branchId)
			throws PhysicalGoldTransactionException;

	public List<PhysicalGoldTransaction> getAllPhysicalGoldTransactionByDeliveryCity(String city)
			throws PhysicalGoldTransactionException;

	public List<PhysicalGoldTransaction> getAllPhysicalGoldTransactionByDeliveryState(String state)
			throws PhysicalGoldTransactionException;

	public void addPhysicalGoldTransaction(PhysicalGoldTransaction newPhysicalGoldTransaction)
			throws PhysicalGoldTransactionException;

	public Integer updatePhysicalGoldTransaction(Integer transactionId, PhysicalGoldTransaction updatedTransaction)
			throws PhysicalGoldTransactionException;

	public Double getTotalPhysicalGoldByUser(Integer userId) throws PhysicalGoldTransactionException;
}
