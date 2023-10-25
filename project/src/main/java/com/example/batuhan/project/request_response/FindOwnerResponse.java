package com.example.batuhan.project.request_response;

import lombok.Data;

@Data
public class FindOwnerResponse {
	String email;
	String name;
	String lastName;
	String phoneNumber;
	String roles;
}
