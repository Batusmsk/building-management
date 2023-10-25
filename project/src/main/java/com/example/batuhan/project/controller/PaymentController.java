package com.example.batuhan.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.batuhan.project.request_response.PaymentResponse;
import com.example.batuhan.project.service.PaymentService;

@RestController
public class PaymentController {

	@Autowired
	PaymentService paymentService;
	
	@GetMapping(value = "/payment/findByPerson")
	public List<PaymentResponse> findByPerson(@RequestParam String email) {
		return paymentService.findByPerson(email);
	}
	@GetMapping(value = "/payment/getPayment")
	public PaymentResponse getPayment(@RequestParam Integer id) {
		return paymentService.getPayment(id);
	}
	
	@GetMapping(value = "/payment/getPayments")
	public List<PaymentResponse> getPayment() {
		return paymentService.getPayments();
	}
}
