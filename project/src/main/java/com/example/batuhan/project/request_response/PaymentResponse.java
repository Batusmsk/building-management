package com.example.batuhan.project.request_response;

import lombok.Data;

@Data
public class PaymentResponse {
	
	private Integer id;
	private Integer feeId;
	private String personEmail;
	private Integer paymentAmount;
	private String paymentDate;
}
