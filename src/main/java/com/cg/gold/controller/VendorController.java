package com.cg.gold.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import com.cg.gold.entity.Vendor;
import com.cg.gold.exception.VendorException;
import com.cg.gold.service.VendorService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/vendor")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@Autowired
	private Environment environment;

	@GetMapping("/home")
	public String showVendorHome(Model model) {
		model.addAttribute("vendor", new Vendor());
		return "vendor-home";
	}

//	@GetMapping
//	public ResponseEntity<List<Vendor>> getAllVendors() {
//		return ResponseEntity.ok(vendorService.getAllVendors());
//	}

	@GetMapping
	public String getAllVendors(Model model) {
		model.addAttribute("vendors", vendorService.getAllVendors());
		return "vendor-list";
	}

//	@PostMapping("/add")
//	public ResponseEntity<String> addVendor(@RequestBody Vendor vendor) {
//		vendorService.addVendor(vendor);
//		return ResponseEntity.status(HttpStatus.CREATED).body("Vendor added successfully");
//	}

	@PostMapping("/add")
	public String addVendor(@ModelAttribute Vendor vendor, RedirectAttributes redirectAttributes) {
		vendorService.addVendor(vendor);
		redirectAttributes.addFlashAttribute("message", "Vendor added successfully!");
		return "redirect:/api/v1/vendor/home";
	}

//	@GetMapping("/{vendor_id}")
//	public ResponseEntity<Vendor> getVendorById(@PathVariable("vendor_id") Integer vendorId) throws VendorException {
//		return ResponseEntity.ok(vendorService.getVendorById(vendorId));
//	}

	@GetMapping("/search")
	public String searchVendorById(@RequestParam Integer vendorId) {
		return "redirect:/api/v1/vendor/" + vendorId;
	}

	@GetMapping("/{vendor_id}")
	public String getVendorDetails(@PathVariable("vendor_id") Integer vendorId, Model model, HttpServletRequest request)
			throws VendorException {
		try {
			Vendor vendor = vendorService.getVendorById(vendorId);
			model.addAttribute("vendor", vendor);
			return "vendor-by-id";
		} catch (VendorException e) {
			return handleError(e, request, model);
		}

	}

//	@GetMapping("/name/{vendor_name}")
//	public ResponseEntity<Vendor> getVendorByName(@PathVariable("vendor_name") String vendorName)
//			throws VendorException {
//		return ResponseEntity.ok(vendorService.getVendorByVendorName(vendorName));
//	}

	@GetMapping("/searchByName")
	public String redirectToVendorByName(@RequestParam String vendorName) {
		return "redirect:/api/v1/vendor/name/" + UriUtils.encodePath(vendorName, StandardCharsets.UTF_8);
	}

	@GetMapping("/name/{vendor_name}")
	public String getVendorByName(@PathVariable("vendor_name") String vendorName, Model model,
			HttpServletRequest request) {
		try {
			Vendor vendor = vendorService.getVendorByVendorName(vendorName);
			model.addAttribute("vendor", vendor);
			return "vendor-by-name";
		} catch (VendorException e) {
			return handleError(e, request, model);
		}
	}

//	@PutMapping("/update/{vendor_id}")
//	public ResponseEntity<String> updateVendor(@PathVariable("vendor_id") Integer vendorId,
//			@RequestBody Vendor updatedVendor) throws VendorException {
//		vendorService.updateVendor(vendorId, updatedVendor);
//		return ResponseEntity.ok("Vendor updated successfully");
//	}

	@GetMapping("/updatePage/{vendor_id}")
	public String showUpdateForm(@PathVariable("vendor_id") Integer vendorId, Model model) throws VendorException {
		Vendor vendor = vendorService.getVendorById(vendorId);
		model.addAttribute("vendor", vendor);
		return "vendor-update";
	}

	@PostMapping("/update")
	public String updateVendor(@ModelAttribute Vendor vendor, RedirectAttributes redirectAttributes)
			throws VendorException {
		vendorService.updateVendor(vendor.getVendorId(), vendor);
		redirectAttributes.addFlashAttribute("message", "Vendor data updated successfully!");
		return "redirect:/api/v1/vendor/home";
	}

//	@PatchMapping("/{vendor_id}/total_gold_quantity/{quantity}")
//	public ResponseEntity<String> updateVendorQuantity(@PathVariable("vendor_id") Integer vendorId,
//			@PathVariable Double quantity) throws VendorException {
//		vendorService.updateVendorTotalGoldQuantityById(vendorId, quantity);
//		return ResponseEntity.ok("Vendor gold quantity updated");
//	}

	@GetMapping("/updateGoldQuantityPage")
	public String showUpdateGoldQuantityPage() {
		return "vendor-update-gold-quantity";
	}

	@PostMapping("/updateGoldQuantityPage")
	public String handleUpdateGoldQuantityForm(@RequestParam Integer vendorId, @RequestParam Double quantity,
			Model model, HttpServletRequest request) {
		try {
			vendorService.updateVendorTotalGoldQuantityById(vendorId, quantity);
			model.addAttribute("message", "Vendor total gold quantity updated successfully!");
		} catch (Exception e) {
			return handleError(e, request, model);
		}
		return "vendor-update-gold-quantity";
	}

//	@PatchMapping("/new_current_gold_price/{new_price}")
//	public ResponseEntity<String> updateAllVendorsPrice(@PathVariable("new_price") Double newPrice) {
//		vendorService.updateAllVendorCurrentGoldPriceWithNewPrice(newPrice);
//		return ResponseEntity.ok("All vendors' gold price updated");
//	}

	@GetMapping("/updateGoldPricePage")
	public String showUpdateGoldPricePage() {
		return "vendor-update-gold-price";
	}

	@PostMapping("/updateGoldPricePage")
	public String handleUpdateGoldPriceForm(@RequestParam Double newPrice, Model model, HttpServletRequest request) {
		try {
			vendorService.updateAllVendorCurrentGoldPriceWithNewPrice(newPrice);
			model.addAttribute("message", "All Vendor Current gold Price was updated successfully!");
		} catch (Exception e) {
			model.addAttribute("error", "Error: " + e.getMessage());
		}
		return "vendor-update-gold-price";
	}

	private String handleError(Exception e, HttpServletRequest request, Model model) {
		model.addAttribute("errorMessage", environment.getProperty(e.getMessage()));
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("timestamp", LocalDateTime.now());
		return "vendor-error";
	}

}
