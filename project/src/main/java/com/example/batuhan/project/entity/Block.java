package com.example.batuhan.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Block {
	
    @Id
	String blockName;
    
	Integer numberOfFloors;
	Integer baseArea;
	//Integer totalApartmentCount;
}

