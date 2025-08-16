package com.cg.gold.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.gold.entity.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
	Optional<Vendor> findByVendorNameIgnoreCase(String vendorName);
}
