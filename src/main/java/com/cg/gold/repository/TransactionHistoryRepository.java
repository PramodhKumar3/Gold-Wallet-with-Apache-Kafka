package com.cg.gold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.gold.entity.TransactionHistory;
import com.cg.gold.entity.TransactionHistory.TransactionStatus;
import com.cg.gold.entity.TransactionHistory.TransactionType;
import com.cg.gold.entity.VendorBranch;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

	List<TransactionHistory> findByUserUserId(Integer userId);

	List<TransactionHistory> findByTransactionStatus(TransactionStatus status);

	List<TransactionHistory> findByTransactionType(TransactionType type);

	List<TransactionHistory> findByBranch(VendorBranch branch);

	List<TransactionHistory> findByUserUserIdOrderByCreatedAtDesc(Integer userId);

}
