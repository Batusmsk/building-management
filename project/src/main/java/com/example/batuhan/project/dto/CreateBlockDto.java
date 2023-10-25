package com.example.batuhan.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlockDto {
	String blockName;
	Integer numberOfFloors;
	Integer baseArea;
	//Integer totalApartmentCount;
}
