package com.example.batuhan.project.auth;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.entity.PasswordResetTokenEntity;
import com.example.batuhan.project.repository.PasswordResetTokenRepository;

@Service
public class PasswordReset {
	private static final int EXPIRATION_TIME_IN_MINUTES = 10;

    private String token;
    private LocalDateTime expirationDate;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    
    public PasswordReset() {
        this.token = UUID.randomUUID().toString();
        this.expirationDate = calculateExpirationDate();
    }

    private LocalDateTime calculateExpirationDate() {
        return LocalDateTime.now().plus(EXPIRATION_TIME_IN_MINUTES, ChronoUnit.MINUTES);
    }
    
    public void updateToken() {
        this.token = UUID.randomUUID().toString();
        this.expirationDate = calculateExpirationDate();
    }

    public boolean isExpired(String request) {
    	PasswordResetTokenEntity tokenEntity = null;
    	
    	if(request.contains("@")) {
    		tokenEntity = passwordResetTokenRepository.findById(request).get();
    	} else {
    		
    		tokenEntity = passwordResetTokenRepository.findByToken(request).get();
    		System.out.println(request + tokenEntity.toString());
    	}
    	
    	LocalDateTime expDate = tokenEntity.getExpirationDate();
    	boolean hasExpired = expDate.isBefore(LocalDateTime.now());

        return hasExpired;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}

