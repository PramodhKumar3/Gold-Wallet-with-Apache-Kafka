package com.cg.gold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.gold.entity.VendorBranch;

@Repository
public interface VendorBranchRepository extends JpaRepository<VendorBranch, Integer> {

	List<VendorBranch> findByVendorVendorId(Integer vendorId);

	List<VendorBranch> findByAddressCityIgnoreCase(String city);

	List<VendorBranch> findByAddressStateIgnoreCase(String state);

	List<VendorBranch> findByAddressCountryIgnoreCase(String country);

}
