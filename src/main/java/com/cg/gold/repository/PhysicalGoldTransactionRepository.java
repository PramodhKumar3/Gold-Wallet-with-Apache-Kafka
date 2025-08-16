package com.cg.gold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.gold.entity.PhysicalGoldTransaction;

@Repository
public interface PhysicalGoldTransactionRepository extends JpaRepository<PhysicalGoldTransaction, Integer> {

	List<PhysicalGoldTransaction> findByUserUserId(Integer userId);

	List<PhysicalGoldTransaction> findAllByUserUserId(Integer userId);

	List<PhysicalGoldTransaction> findAllByBranchBranchId(Integer branchId);

	List<PhysicalGoldTransaction> findByDeliveryAddressCity(String city);

	List<PhysicalGoldTransaction> findByDeliveryAddressState(String state);

	@Query("SELECT COALESCE(SUM(p.quantity), 0) FROM PhysicalGoldTransaction p WHERE p.user.id = :userId")
	Double getTotalQuantityByUserId(@Param("userId") Integer userId);

}
