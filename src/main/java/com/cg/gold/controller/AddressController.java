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

import com.cg.gold.entity.Address;
import com.cg.gold.exception.AddressException;
import com.cg.gold.service.AddressService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/v1/address")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private Environment environment;

	@GetMapping("/home")
	public String showAdderessHome(Model model) {
		model.addAttribute("address", new Address());
		return "address-home";
	}

//	@PostMapping("/add")
//	public String addAddress(@ModelAttribute Address address, RedirectAttributes redirectAttributes) {
//		addressService.addAddress(address);
//		redirectAttributes.addFlashAttribute("message", "Address added successfully!");
//		return "redirect:/api/v1/address/home";
//	}

	@PostMapping("/add")
	public String addAddress(@Valid @ModelAttribute Address address, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			return "address-home";
		}
		addressService.addAddress(address);
		redirectAttributes.addFlashAttribute("message", "Address added successfully!");
		return "redirect:/api/v1/address/home";
	}

	@GetMapping
	public String getAllAddresses(Model model) {
		List<Address> addresses = addressService.getAllAddresses();
		model.addAttribute("address", new Address());
		model.addAttribute("addresses", addresses);
		return "address-list";
	}

	@GetMapping("/search")
	public String redirectToAddressById(@RequestParam Integer addressId) {
		return "redirect:/api/v1/address/" + addressId;
	}

	@GetMapping("/{address_id}")
	public String searchById(@PathVariable("address_id") Integer addressId, Model model, HttpServletRequest request) {
		try {
			Address address = addressService.getAddressById(addressId);
			model.addAttribute("address", address);
			return "address-by-id";
		} catch (AddressException e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/updatePage/{address_id}")
	public String showUpdateAddressForm(@PathVariable("address_id") Integer addressId, Model model,
			HttpServletRequest request) {
		try {
			Address address = addressService.getAddressById(addressId);
			model.addAttribute("address", address);
			return "address-update";
		} catch (AddressException e) {
			return handleError(e, request, model);
		}
	}

//	@PostMapping("/update")
//	public String updateAddressForm(@ModelAttribute Address address, Model model, HttpServletRequest request) {
//		try {
//			addressService.updateAddressById(address.getAddressId(), address);
//			return "redirect:/api/v1/address/home";
//		} catch (AddressException e) {
//			return handleError(e, request, model);
//		}
//	}

	@PostMapping("/update")
	public String updateAddressForm(@Valid @ModelAttribute Address address, BindingResult result, Model model,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			return "address-update";
		}
		try {
			addressService.updateAddressById(address.getAddressId(), address);
			return "redirect:/api/v1/address/home";
		} catch (AddressException e) {
			return handleError(e, request, model);
		}
	}

	private String handleError(Exception e, HttpServletRequest request, Model model) {
		model.addAttribute("errorMessage", environment.getProperty(e.getMessage()));
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("timestamp", LocalDateTime.now());
		return "address-error";
	}

}
