package com.example.batuhan.project.dto;

import lombok.Data;

@Data
public class CreatePersonDto {
	private String email;
	private String phoneNumber;
	private String name;
	private String lastName;
}
