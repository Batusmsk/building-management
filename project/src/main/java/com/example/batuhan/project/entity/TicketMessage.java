package com.example.batuhan.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class TicketMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	String message;
	
	String sendDate;
	String sendTime;
	
	@OneToOne
	Person person;
	@OneToOne
	Ticket ticket;
}
