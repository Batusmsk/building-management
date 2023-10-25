package com.example.batuhan.project.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;

import com.example.batuhan.project.keys.ApartmentKey;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Entity
@Data
@IdClass(ApartmentKey.class)
public class Apartment {
	
	@Id
	private Integer apartmentNo;
    
	Integer floor;
	Integer baseArea;
	
	private String purchaseDate;
	@Id
	@OneToOne
    private Block block;
	
	
	private Integer personId;
}

