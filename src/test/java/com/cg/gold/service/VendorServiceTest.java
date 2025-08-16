package com.cg.gold.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.gold.entity.Vendor;
import com.cg.gold.exception.VendorException;
import com.cg.gold.repository.VendorRepository;

@ExtendWith(MockitoExtension.class)
public class VendorServiceTest {

	@Mock
	private VendorRepository vendorRepository;

	@InjectMocks
	private VendorServiceImpl vendorService;

	private Vendor vendor;

	@BeforeEach
	void setUp() {
		vendor = new Vendor();
		vendor.setVendorId(1);
		vendor.setVendorName("GoldMart");
		vendor.setDescription("Trusted gold vendor");
		vendor.setContactPersonName("Alice");
		vendor.setContactEmail("alice@goldmart.com");
		vendor.setContactPhone("9876543210");
		vendor.setWebsiteUrl("https://goldmart.com");
		vendor.setTotalGoldQuantity(1000.0);
		vendor.setCurrentGoldPrice(6000.0);
		vendor.setCreatedAt(LocalDateTime.now());
	}

	@Test
	void testGetAllVendors() {
		Mockito.when(vendorRepository.findAll()).thenReturn(List.of(vendor));
		List<Vendor> result = vendorService.getAllVendors();
		Assertions.assertEquals(1, result.size());
	}

	@Test
	void testGetVendorById_Success() throws VendorException {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Vendor result = vendorService.getVendorById(1);
		Assertions.assertEquals("GoldMart", result.getVendorName());
	}

	@Test
	void testGetVendorById_NotFound() {
		Mockito.when(vendorRepository.findById(99)).thenReturn(Optional.empty());
		Assertions.assertThrows(VendorException.class, () -> vendorService.getVendorById(99));
	}

	@Test
	void testGetVendorByVendorName_Success() throws VendorException {
		Mockito.when(vendorRepository.findByVendorNameIgnoreCase("GoldMart")).thenReturn(Optional.of(vendor));
		Vendor result = vendorService.getVendorByVendorName("GoldMart");
		Assertions.assertEquals("Alice", result.getContactPersonName());
	}

	@Test
	void testGetVendorByVendorName_NotFound() {
		Mockito.when(vendorRepository.findByVendorNameIgnoreCase("SilverMart")).thenReturn(Optional.empty());
		Assertions.assertThrows(VendorException.class, () -> vendorService.getVendorByVendorName("SilverMart"));
	}

	@Test
	void testAddVendor() {
		vendorService.addVendor(vendor);
		Mockito.verify(vendorRepository).save(Mockito.any(Vendor.class));
	}

	@Test
	void testUpdateVendor_Success() throws VendorException {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		Vendor updated = new Vendor();
		updated.setVendorName("GoldMart Updated");
		updated.setDescription("Updated description");
		updated.setContactPersonName("Bob");
		updated.setContactEmail("bob@goldmart.com");
		updated.setContactPhone("1234567890");
		updated.setWebsiteUrl("https://goldmart-updated.com");
		updated.setTotalGoldQuantity(1200.0);
		updated.setCurrentGoldPrice(6100.0);

		vendorService.updateVendor(1, updated);

		Mockito.verify(vendorRepository).save(Mockito.any(Vendor.class));
	}

	@Test
	void testUpdateVendorTotalGoldQuantityById_Success() throws VendorException {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.of(vendor));
		vendorService.updateVendorTotalGoldQuantityById(1, 1500.0);
		Assertions.assertEquals(1500.0, vendor.getTotalGoldQuantity());
		Mockito.verify(vendorRepository).save(vendor);
	}

	@Test
	void testUpdateVendorTotalGoldQuantityById_NotFound() {
		Mockito.when(vendorRepository.findById(1)).thenReturn(Optional.empty());
		Assertions.assertThrows(VendorException.class,
				() -> vendorService.updateVendorTotalGoldQuantityById(1, 1500.0));
	}

	@Test
	void testUpdateAllVendorCurrentGoldPriceWithNewPrice() {
		Mockito.when(vendorRepository.findAll()).thenReturn(List.of(vendor));
		vendorService.updateAllVendorCurrentGoldPriceWithNewPrice(6200.0);
		Assertions.assertEquals(6200.0, vendor.getCurrentGoldPrice());
		Mockito.verify(vendorRepository).saveAll(List.of(vendor));
	}
}
