package com.example.batuhan.project.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.entity.Payment;
import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.repository.PaymentRepository;
import com.example.batuhan.project.repository.PersonRepository;
import com.example.batuhan.project.request_response.PaymentResponse;

@Service
public class PaymentService {
	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	PersonService personService;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	FeeService feeService;
	
	public boolean createPayment(Integer id, String email, Integer amount) {
		try {
			Date date = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
			Optional<Person> person = personService.findByEmail(email);
			Payment payment = new Payment();
			payment.setFee(feeService.getFee(id).get());
			payment.setPaymentAmount(amount);
			payment.setPaymentDate(ft.format(date));
			payment.setPerson(personService.findByEmail(email).get());
			paymentRepository.save(payment);
			
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}

	}
	public List<PaymentResponse> findByPerson(String email) {
		if(!personService.findByEmail(email).isPresent()) return null;
		List<PaymentResponse> list = new ArrayList<>();
		for(var i:getPayments()) {
			if(i.getPersonEmail().equals(email)) {
				list.add(i);
			}
		}
		return list;
	}
	
	public PaymentResponse getPayment(Integer id) {
		Payment i = paymentRepository.findById(id).get();
		PaymentResponse response = new PaymentResponse();
		response.setId(i.getId());
		response.setFeeId(i.getFee().getId());
		response.setPaymentAmount(i.getPaymentAmount());
		response.setPaymentDate(i.getPaymentDate());
		response.setPersonEmail(i.getPerson().getEmail());
		return response;
	}
	
	public List<PaymentResponse> getPayments() {
		List<PaymentResponse> list = new ArrayList<>();
		for(var i:paymentRepository.findAll()) {
			PaymentResponse response = new PaymentResponse();
			response.setId(i.getId());
			response.setFeeId(i.getFee().getId());
			response.setPaymentAmount(i.getPaymentAmount());
			response.setPaymentDate(i.getPaymentDate());
			response.setPersonEmail(i.getPerson().getEmail());
			list.add(response);	
		}
		return list;
	}
	
}
