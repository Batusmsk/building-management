package com.example.batuhan.project.request_response;

import lombok.Data;

@Data
public class RegisterRequest {
	private String email;
	private String password;
	
	private String phoneNumber;
	private String name;
	private String lastName;
}
