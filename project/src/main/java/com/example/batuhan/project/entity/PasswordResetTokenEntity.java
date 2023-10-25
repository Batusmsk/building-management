package com.example.batuhan.project.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class PasswordResetTokenEntity {
	
	@Id
	String email;
	
	String token;
	LocalDateTime expirationDate;
}
