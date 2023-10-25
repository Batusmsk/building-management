package com.example.batuhan.project.mail;

public interface MailService {
	String sendMail(SendMailRequest request);
	String sendMultiMediaMail();
}
