package com.example.batuhan.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Fee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	private Person person;
	
	private String blockNo;
	private Integer apartment;
	private String feeDate;
	private Integer feeAmount;
	private Boolean status;
	private Integer paidAmount;
	
}
