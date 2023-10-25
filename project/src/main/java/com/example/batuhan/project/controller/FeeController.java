package com.example.batuhan.project.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.batuhan.project.dto.FeeDto;
import com.example.batuhan.project.entity.Fee;
import com.example.batuhan.project.locale.MyLocaleResolver;
import com.example.batuhan.project.request_response.FeeRequest;
import com.example.batuhan.project.service.FeeService;

@RestController
public class FeeController {
	@Autowired
	FeeService feeService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MyLocaleResolver myLocaleResolver;
	
	@PostMapping(value = "/fee/createFee")
	public String createFee(@RequestBody FeeRequest request) {
		return feeService.createFee(request);
	}
	@GetMapping(value = "/fee/findByPerson")
	public List<Fee> findByPerson(@RequestParam String email) {
		return feeService.findByPerson(email);
	}
	@GetMapping(value = "/fee/findByBlockNameAndApartmentNo")
	public List<FeeDto> findByBlockNameAndApartmentNo(@RequestParam String blockName, @RequestParam Integer apartmentNo) {
		return feeService.findByBlockNameAndApartmentNo(blockName, apartmentNo);
	}
	@GetMapping(value = "/fee/payFee", produces = "text/plain;charset=UTF-8")
	public String payFee(HttpServletRequest request, @RequestParam Integer id) {
		return messageSource.getMessage(feeService.payFee(id, 0), null, myLocaleResolver.resolveLocale(request));
		
	}
	@GetMapping(value = "fee/getFee")
	public Optional<Fee> getFee(@RequestParam Integer id) {
		return feeService.getFee(id);
	}
	@GetMapping(value = "/fee/getFees")
	public List<Fee> getFees(@RequestParam Integer status) {
		return feeService.getFees(status);
	}
	@PutMapping(value = "/fee/payWithAmount", produces = "text/plain;charset=UTF-8")
	public String amountWithPayment(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer amount) {
		return messageSource.getMessage(feeService.amountWithPayment(id, amount), null, myLocaleResolver.resolveLocale(request));
	}
}
