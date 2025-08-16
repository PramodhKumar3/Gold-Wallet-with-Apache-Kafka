package com.cg.gold.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cg.gold.entity.Payment;
import com.cg.gold.entity.Payment.PaymentMethod;
import com.cg.gold.entity.Payment.PaymentStatus;
import com.cg.gold.service.PaymentService;
import com.cg.gold.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/v1/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserService userService;

	@Autowired
	private Environment environment;

	@GetMapping("/home")
	public String showPaymentHome(Model model) {
		model.addAttribute("payment", new Payment());
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("methods", PaymentMethod.values());
		model.addAttribute("statuses", PaymentStatus.values());
		return "payment-home";
	}

	@GetMapping
	public String getAllPayments(Model model) {
		model.addAttribute("payments", paymentService.getAllPayments());
		return "payment-list";
	}

//	@PostMapping("/add")
//	public String addPayment(@ModelAttribute Payment payment, RedirectAttributes redirectAttributes) {
//		try {
//			payment.setUser(userService.getUserById(payment.getUser().getUserId()));
//			payment.setCreatedAt(LocalDateTime.now());
//			paymentService.addPayment(payment);
//			redirectAttributes.addFlashAttribute("message", "Payment was successful!");
//			return "redirect:/api/v1/payments/home";
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
//			return "redirect:/api/v1/payments/home";
//		}
//	}

	@PostMapping("/add")
	public String addPayment(@Valid @ModelAttribute Payment payment, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("users", userService.getAllUsers());
			model.addAttribute("methods", PaymentMethod.values());
			model.addAttribute("statuses", PaymentStatus.values());
			return "payment-home";
		}

		try {
			payment.setUser(userService.getUserById(payment.getUser().getUserId()));
			payment.setCreatedAt(LocalDateTime.now());
			paymentService.addPayment(payment);
			redirectAttributes.addFlashAttribute("message", "Payment was successful!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/api/v1/payments/home";
	}

	@GetMapping("/search/byId")
	public String getPaymentById(@RequestParam Integer paymentId, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("payment", paymentService.getPaymentById(paymentId));
			return "payment-by-id";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/search/byUser")
	public String getPaymentsByUser(@RequestParam Integer userId, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("payments", paymentService.getAllPaymentByUserId(userId));
			return "payment-by-user";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	@GetMapping("/successful")
	public String getSuccessfulPayments(Model model) {
		model.addAttribute("payments", paymentService.getAllSuccessPayments());
		return "payment-successful";
	}

	@GetMapping("/failed")
	public String getFailedPayments(Model model) {
		model.addAttribute("payments", paymentService.getAllFailedPayments());
		return "payment-failed";
	}

	@GetMapping("/search/byMethod")
	public String getPaymentsByMethod(@RequestParam PaymentMethod method, Model model, HttpServletRequest request) {
		try {
			model.addAttribute("payments", paymentService.getAllPaymentsByPaymentMethod(method));
			model.addAttribute("method", method);
			return "payment-by-method";
		} catch (Exception e) {
			return handleError(e, request, model);
		}
	}

	private String handleError(Exception e, HttpServletRequest request, Model model) {
		model.addAttribute("errorMessage", environment.getProperty(e.getMessage()));
		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("timestamp", LocalDateTime.now());
		return "payment-error";
	}

}
