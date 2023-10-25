package com.example.batuhan.project.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceIml implements MailService{
	private JavaMailSender mailSender;

	@Autowired
	public MailServiceIml(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Override
	public String sendMail(SendMailRequest request) {
		if(!request.getTo().contains("@") &&  !request.getTo().contains(".com")) return "Hatalı mail adresi";
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("noreply@gmail.com");
		message.setTo(request.getTo());
		message.setText(request.getText());
		message.setSubject(request.getSubject());
		mailSender.send(message);
		return "Mail Başarılı bir şekilde gönderildi";
	}

	@Override
	public String sendMultiMediaMail() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
