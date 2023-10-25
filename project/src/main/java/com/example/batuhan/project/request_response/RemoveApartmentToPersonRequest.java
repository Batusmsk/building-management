package com.example.batuhan.project.request_response;

import lombok.Data;

@Data
public class RemoveApartmentToPersonRequest {
	private String email;
	private Integer apartmentNo;
	private String blockNo;
}
