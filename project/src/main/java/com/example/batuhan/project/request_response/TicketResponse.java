package com.example.batuhan.project.request_response;

import lombok.Data;

@Data
public class TicketResponse {
	Integer id;
	String ticketName;
	String personEmail;
	String createDate;
	Boolean status;
}
