package com.cg.gold.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cg.gold.entity.VendorBranch;
import com.cg.gold.entity.VirtualGoldHolding;
import com.cg.gold.exception.VirtualGoldHoldingException;
import com.cg.gold.kafka.KafkaVirtualGoldProducer;
import com.cg.gold.service.VendorBranchService;
import com.cg.gold.service.VirtualGoldHoldingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/v1/virtual_gold_holding")
public class VirtualGoldHoldingController {

	@Autowired
	private VirtualGoldHoldingService virtualGoldHoldingService;

	@Autowired
	private VendorBranchService vendorBranchService;

	@Autowired
	private KafkaVirtualGoldProducer kafkaVirtualGoldProducer;

	@Autowired
	private Environment environment;

	@GetMapping("/home")
	public String showVirtualGoldHome(Model model) {
		model.addAttribute("holding", new VirtualGoldHolding());
		return "virtual-gold-home";
	}

//	@GetMapping
//	public ResponseEntity<List<VirtualGoldHolding>> getAllHoldings() {
//		return ResponseEntity.ok(virtualGoldHoldingService.getAllVirtualGoldHoldings());
//	}

	@GetMapping
	public String showAllHoldings(Model model) {
		List<VirtualGoldHolding> holdings = virtualGoldHoldingService.getAllVirtualGoldHoldings();
		model.addAttribute("holdings", holdings);
		return "virtual-gold-list";
	}

//	@GetMapping("/users/{user_id}")
//	public ResponseEntity<List<VirtualGoldHolding>> getHoldingsByUser(@PathVariable("user_id") Integer userId)
//			throws VirtualGoldHoldingException {
//		return ResponseEntity.ok(virtualGoldHoldingService.getAllVirtualGoldHoldingByUserId(userId));
//	}

	@GetMapping("/searchByUser")
	public String searchByUser(@RequestParam Integer userId, Model model, HttpServletRequest request) {
		try {
			List<VirtualGoldHolding> holdings = virtualGoldHoldingService.getAllVirtualGoldHoldingByUserId(userId);
			model.addAttribute("holdings", holdings);
			model.addAttribute("userId", userId);
		} catch (VirtualGoldHoldingException e) {
			return handleError(e, request, model);
		}
		return "virtual-gold-search-by-user";
	}

//	@GetMapping("/{holding_id}")
//	public ResponseEntity<List<VirtualGoldHolding>> getHoldingById(@PathVariable("holding_id") Integer holdingId)
//			throws VirtualGoldHoldingException {
//		return ResponseEntity.ok(virtualGoldHoldingService.getVirtualGoldHoldingById(holdingId));
//	}

	@GetMapping("/search")
	public String redirectToHoldingById(@RequestParam Integer holdingId) {
		return "redirect:/api/v1/virtual_gold_holding/" + holdingId;
	}

	@GetMapping("/{holding_id}")
	public String searchByHoldingId(@PathVariable("holding_id") Integer holdingId, Model model,
			HttpServletRequest request) {
		try {
			VirtualGoldHolding holding = virtualGoldHoldingService.getVirtualGoldHoldingById(holdingId);
			model.addAttribute("holding", holding);
			return "virtual-gold-by-id";
		} catch (VirtualGoldHoldingException e) {
			return handleError(e, request, model);
		}
	}

//	@GetMapping("/byUserAndVendor/{user_id}/{vendor_id}")
//	public ResponseEntity<List<VirtualGoldHolding>> getHoldingsByUserAndVendor(@PathVariable("user_id") Integer userId,
//			@PathVariable("vendor_id") Integer vendorId) throws VirtualGoldHoldingException {
//		return ResponseEntity.ok(virtualGoldHoldingService.getVirtualGoldHoldingByUserAndVendor(userId, vendorId));
//	}

	@GetMapping("/byUserAndVendor")
	public String searchByUserAndVendor(@RequestParam Integer userId, @RequestParam Integer vendorId, Model model,
			HttpServletRequest request) {
		try {
			List<VirtualGoldHolding> holdings = virtualGoldHoldingService.getVirtualGoldHoldingByUserAndVendor(userId,
					vendorId);
			model.addAttribute("holdings", holdings);
			model.addAttribute("userId", userId);
			model.addAttribute("vendorId", vendorId);
		} catch (VirtualGoldHoldingException e) {
			return handleError(e, request, model);
		}
		return "virtual-gold-search-by-user-and-vendor";
	}

//	@PostMapping("/add")
//	public ResponseEntity<String> addHolding(@RequestBody VirtualGoldHolding holding) {
//		virtualGoldHoldingService.addVirtualGoldHolding(holding);
//		return ResponseEntity.status(HttpStatus.CREATED).body("Holding added successfully");
//	}

//	@PostMapping("/add")
//	public String addHolding(@ModelAttribute VirtualGoldHolding holding, RedirectAttributes redirectAttributes) {
//		virtualGoldHoldingService.addVirtualGoldHolding(holding);
//		redirectAttributes.addFlashAttribute("message", "Virtual Gold Holding data added successfully!");
//		return "redirect:/api/v1/virtual_gold_holding/home";
//	}

//	@PostMapping("/add")
//	public String addHolding(@Valid @ModelAttribute VirtualGoldHolding holding, BindingResult result,
//			RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) {
//		if (result.hasErrors()) {
//			model.addAttribute("branches", vendorBranchService.getAllVendorBranches());
//			return "virtual-gold-home";
//		}
//		try {
//			virtualGoldHoldingService.addVirtualGoldHolding(holding);
//			redirectAttributes.addFlashAttribute("message", "Virtual Gold Holding data added successfully!");
//			return "redirect:/api/v1/virtual_gold_holding/home";
//		} catch (Exception e) {
//			return handleError(e, request, model);
//		}
//	}

	@PostMapping("/add")
	public String addHolding(@Valid @ModelAttribute VirtualGoldHolding holding, BindingResult result,
			RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			model.addAttribute("branches", vendorBranchService.getAllVendorBranches());
			return "virtual-gold-home";
		}
		try {
			virtualGoldHoldingService.addVirtualGoldHolding(holding);
			kafkaVirtualGoldProducer.sendEvent(holding);
			redirectAttributes.addFlashAttribute("message", "Virtual Gold Holding data added successfully!");
			return "redirect:/api/v1/virtual_gold_holding/home";
		} catch (Exception e) {
//			model.addAttribute("error", "An error occurred while processing the request.");
//			return "virtual-gold-home";
			return handleError(e, request, model);
		}
	}

//	@PutMapping("/update/{holding_id}")
//	public ResponseEntity<String> updateHolding(@PathVariable("holding_id") Integer holdingId,
//			@RequestBody VirtualGoldHolding updatedHolding) throws VirtualGoldHoldingException {
//		Integer id = virtualGoldHoldingService.updateVirtualGoldHolding(holdingId, updatedHolding);
//		return ResponseEntity.ok("Holding updated with ID: " + id);
//	}

	@GetMapping("/updatePage/{holding_id}")
	public String showUpdateHoldingForm(@PathVariable("holding_id") Integer holdingId, Model model,
			HttpServletRequest request) {
		try {
			VirtualGoldHolding holding = virtualGoldHoldingService.getVirtualGoldHoldingById(holdingId);
			List<VendorBranch> branches = vendorBranchService.getAllVendorBranches();
			model.addAttribute("holding", holding);
			model.addAttribute("branches", branches);
			return "virtual-gold-update";
		} catch (VirtualGoldHoldingException e) {
			return handleError(e, request, model);
		}
	}

	@PostMapping("/update")
	public String updateHoldingForm(@Valid @ModelAttribute VirtualGoldHolding holding, BindingResult result,
			Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			model.addAttribute("branches", vendorBranchService.getAllVendorBranches());
			return "virtual-gold-update";
		}
		try {
			virtualGoldHoldingService.updateVirtualGoldHolding(holding.getHoldingId(), holding);
			return "redirect:/api/v1/virtual_gold_holding/home";
		} catch (VirtualGoldHoldingException e) {
			return handleError(e, request, model);
		}
	}

//	@PostMapping("/update")
//	public String updateHoldingForm(@ModelAttribute VirtualGoldHolding holding, Model model,
//			HttpServletRequest request) {
//		try {
//			virtualGoldHoldingService.updateVirtualGoldHolding(holding.getHoldingId(), holding);
//			return "redirect:/api/v1/virtual_gold_holding/home";
//		} catch (VirtualGoldHoldingException e) {
//			return handleError(e, request, model);
//		}
//	}

//	@PostMapping("/convertToPhysical/{holding_id}")
//	public ResponseEntity<String> convertToPhysical(@PathVariable("holding_id") Integer holdingId)
//			throws VirtualGoldHoldingException {
//		virtualGoldHoldingService.convertVirtualToPhysical(holdingId);
//		return ResponseEntity.ok("Holding converted to physical and removed.");
//	}

	@GetMapping("/convertToPhysical")
	public String showConvertToPhysicalPage() {
		return "virtual-gold-convert-to-physical";
	}

	@PostMapping("/convertToPhysical")
	public String convertToPhysical(@RequestParam Integer holdingId, Model model, HttpServletRequest request) {
		try {
			virtualGoldHoldingService.convertVirtualToPhysical(holdingId);
			model.addAttribute("message", "Virtual Gold data converted successfully!");
		} catch (VirtualGoldHoldingException e) {
			return handleError(e, request, model);
		}
		return "virtual-gold-convert-to-physical";
	}

	private String handleError(Exception e, HttpServletRequest request, Model model) {
		model.addAttribute("errorMessage", environment.getProperty(e.getMessage()));
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("timestamp", LocalDateTime.now());
		return "virtual-gold-error";
	}

}
