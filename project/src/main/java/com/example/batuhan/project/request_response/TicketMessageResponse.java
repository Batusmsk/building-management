package com.example.batuhan.project.request_response;

import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.entity.Ticket;

import lombok.Data;

@Data
public class TicketMessageResponse {
	
	Integer messageId;
	String message;
	
	String sendDate;
	String sendTime;
	
	String personEmail;
	String personName;
	String personLastName;
	
	Integer ticketId;
}
