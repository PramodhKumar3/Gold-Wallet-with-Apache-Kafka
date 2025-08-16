package com.cg.gold.service;

import java.util.List;

import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.TransactionHistory.TransactionType;
import com.cg.gold.exception.TransactionHistoryException;

public interface TransactionHistoryService {

	public List<TransactionHistory> getAllTransactionHistory();

	public TransactionHistory getTransactionHistoryById(Integer transactionId) throws TransactionHistoryException;

	public List<TransactionHistory> getAllTransactionHistoryByUserId(Integer userId) throws TransactionHistoryException;

	public List<TransactionHistory> getAllSuccessTransactionHistory();

	public List<TransactionHistory> getAllFailedTransactionHistory();

	public List<TransactionHistory> getAllTransactionHistoryByTransactionType(TransactionType transactionType)
			throws TransactionHistoryException;

	public void addTransactionHistory(TransactionHistory newTransactionHistory) throws TransactionHistoryException;

	public List<TransactionHistory> getTransactionsByUserSorted(Integer userId) throws TransactionHistoryException;
}
