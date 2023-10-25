package com.example.batuhan.project.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mail")
public class MailController {
	
	@Autowired
	private final MailService mailService;
	
	@Autowired
	MailServiceIml iml;
	
	@Autowired
	public MailController(MailService mailService) {
		this.mailService = mailService;
	}
	
	@PostMapping("/normal") 
	public String sendNormalMail(@RequestBody SendMailRequest request) {
		return iml.sendMail(request);
	}
	@GetMapping("/multi")
	public ResponseEntity<String> sendMultiMail() {
		return ResponseEntity.ok(mailService.sendMultiMediaMail());
	}
}
