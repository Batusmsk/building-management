package com.example.batuhan.project.dto;

import lombok.Data;

@Data
public class FeeDto {
	private Integer id;
	private String email;
	private String feeDate;
	private Boolean status;
	private String blockName;
	private Integer apartmentNo;
	private Integer feeAmount;
	private Integer paidAmount;
}
