package com.cg.gold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.gold.entity.VirtualGoldHolding;

@Repository
public interface VirtualGoldHoldingRepository extends JpaRepository<VirtualGoldHolding, Integer> {

	List<VirtualGoldHolding> findByUserUserId(Integer userId);

	List<VirtualGoldHolding> findByUserUserIdAndBranchVendorVendorId(Integer userId, Integer vendorId);

	@Query("SELECT COALESCE(SUM(v.quantity), 0) FROM VirtualGoldHolding v WHERE v.user.id = :userId")
	Double getTotalQuantityByUserId(@Param("userId") Integer userId);

}
