package com.example.batuhan.project.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.entity.Ticket;
import com.example.batuhan.project.entity.TicketMessage;
import com.example.batuhan.project.repository.TicketMessageRepository;
import com.example.batuhan.project.repository.TicketRepository;
import com.example.batuhan.project.request_response.MessageRequest;
import com.example.batuhan.project.request_response.TicketMessageResponse;
import com.example.batuhan.project.request_response.TicketRequest;
import com.example.batuhan.project.request_response.TicketResponse;

@Service
public class TicketService {
	@Autowired
	TicketMessageRepository ticketMessageRepository;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	PersonService personService;
	
	public String createTicket(TicketRequest request, String email) {
		if(!personService.findByEmail(email).isPresent()) return "WARN001";
		Person person = personService.findByEmail(email).get();
		
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
		
		Ticket ticket = new Ticket();
		ticket.setCreateDate(ft.format(date));
		ticket.setPerson(person);
		ticket.setStatus(true);
		ticket.setTicketName(request.getTicketName());
		ticketRepository.save(ticket);
		return "WARN023";
	}
	public Optional<Ticket> getTicket(Integer id) {
		return ticketRepository.findById(id);
	}
	
	public boolean deleteTicketFromPerson(String email) {
		List<TicketResponse> tickets = getPersonTickets(email);
		for(var i:tickets) {
			List<TicketMessageResponse> messages = getTicketMessages(i.getId(), email);
			for(var m:messages) {
				ticketMessageRepository.deleteById(m.getMessageId());
			}
			ticketRepository.deleteById(i.getId());
			
		}
		return true;
	}
	
	public Boolean closeTicket(Integer id) {
		if(!getTicket(id).isPresent()) return false;
		Ticket ticket = getTicket(id).get();
		if(!ticket.getStatus()) return false;
		ticket.setStatus(false);
		ticketRepository.save(ticket);
		return true;
		
	}
	public Boolean sendMessage(MessageRequest request, String email) {
		Boolean x = false;
		
		for(var i:personService.findByEmail(email).get().getRoles()) {
			if(i.toString().toLowerCase().equals("admın")) {
			 x = true;
			 break;
			} else {
				x = false;
			}
		}
		
		if(x || getTicket(request.getTicketId()).get().getPerson().getEmail().equals(email) ) {
			if(getTicket(request.getTicketId()).get().getStatus() == false) return false;
			Date date = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
			SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
			Person person = personService.findByEmail(email).get();
			Ticket ticket = getTicket(request.getTicketId()).get();
			if(request.getMessage().length() <= 1) return false;
			TicketMessage message = new TicketMessage();
			message.setMessage(request.getMessage());
			message.setPerson(person);
			message.setSendDate(ft.format(date));
			message.setSendTime(time.format(date));
			message.setTicket(ticket);
			ticketMessageRepository.save(message);
			return true;	
		}
		return false;
	}
	
	public List<TicketResponse> getPersonTickets(String email) {
		List<TicketResponse> list = new ArrayList<>();
		for(var i:ticketRepository.findByPersonEmail(email)) {
			TicketResponse response = new TicketResponse();
			response.setId(i.getId());
			response.setCreateDate(i.getCreateDate());
			response.setPersonEmail(i.getPerson().getEmail());
			response.setStatus(i.getStatus());
			response.setTicketName(i.getTicketName());
			list.add(response);
		}
		return list;
	}
	
	public List<TicketMessageResponse> getTicketMessages(Integer id, String email) {
		List<TicketMessageResponse> messages = new ArrayList<>();
		Person person = personService.findByEmail(email).get();
		
		Ticket ticket = getTicket(id).get();
		
		Boolean x = false;
		for(var i:personService.findByEmail(email).get().getRoles()) {
			System.out.print(i + " " + i.toString().toLowerCase());
			if(i.toString().toLowerCase().equals("admın")) {
			 x = true;
			 break;
			} else {
				x = false;
			} 
		}
		
		if(x || ticket.getPerson().getEmail().equals(email)) { 
			for(var m:ticketMessageRepository.findByTicketId(id)) {
				TicketMessageResponse response = new TicketMessageResponse();
				response.setMessageId(m.getId());
				response.setMessage(m.getMessage());
				response.setPersonEmail(m.getPerson().getEmail());
				response.setPersonLastName(m.getPerson().getLastName());
				response.setPersonName(m.getPerson().getName());
				response.setSendDate(m.getSendDate());
				response.setSendTime(m.getSendTime());
				response.setTicketId(m.getId());
				messages.add(response);
			}
			return messages;
		}
		
		return null;
	}
}
 