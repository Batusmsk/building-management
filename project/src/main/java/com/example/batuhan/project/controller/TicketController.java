package com.example.batuhan.project.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.batuhan.project.auth.TokenManager;
import com.example.batuhan.project.entity.Ticket;
import com.example.batuhan.project.locale.MyLocaleResolver;
import com.example.batuhan.project.repository.TicketRepository;
import com.example.batuhan.project.request_response.MessageRequest;
import com.example.batuhan.project.request_response.TicketMessageResponse;
import com.example.batuhan.project.request_response.TicketRequest;
import com.example.batuhan.project.request_response.TicketResponse;
import com.example.batuhan.project.service.TicketService;

@RestController
public class TicketController {
	@Autowired
	TicketService ticketService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MyLocaleResolver myLocaleResolver;
	@Autowired
	TokenManager tokenManager;
	@Autowired
	TicketRepository ticketRepository;
	
	@PostMapping(value = "/ticket/createTicket", produces = "text/plain;charset=UTF-8")
	public String createTicket(HttpServletRequest request, @RequestHeader(value = "Authorization") String token, @RequestBody TicketRequest ticketRequest) {
		String userToken = token.replace("Bearer ", "");
		String email = tokenManager.getEmailToken(userToken);
		
		return messageSource.getMessage(ticketService.createTicket(ticketRequest, email), null, myLocaleResolver.resolveLocale(request));
	}
	
	@PostMapping(value = "/ticket/sendMessage")
	public Boolean sendMessage(@RequestHeader(value = "Authorization") String token, @RequestBody MessageRequest messageRequest) {
		String userToken = token.replace("Bearer ", "");
		String email = tokenManager.getEmailToken(userToken);
		
		return ticketService.sendMessage(messageRequest, email);
	}
	@GetMapping(value = "ticket/deleteTicketFromPerson")
	public boolean deleteTicketFromPerson(@RequestParam String email) {
		return ticketService.deleteTicketFromPerson(email);
	}
	@GetMapping(value = "/ticket/findByPersonTickets") 
	public List<TicketResponse> findByPersonTickets(@RequestParam String email) {
		return ticketService.getPersonTickets(email);
	}
	@GetMapping(value = "/ticket/getMessage")
	public List<TicketMessageResponse> getTicketMessages(@RequestParam Integer id, @RequestHeader(value = "Authorization") String token) {
		String userToken = token.replace("Bearer ", "");
		String email = tokenManager.getEmailToken(userToken);
		return ticketService.getTicketMessages(id, email);
	}
	@GetMapping(value = "/ticket/getTicket")
	public Ticket getTicket(@RequestParam Integer id) {
		return ticketService.getTicket(id).get();
	}
	@GetMapping(value = "/ticket/closeTicket")
	public Boolean closeTicket(@RequestParam Integer id) {
		return ticketService.closeTicket(id);
	}
	

}
