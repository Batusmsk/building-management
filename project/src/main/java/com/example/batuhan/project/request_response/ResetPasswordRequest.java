package com.example.batuhan.project.request_response;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	String token;
	String newPassword;
}
