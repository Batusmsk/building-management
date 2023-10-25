package com.example.batuhan.project.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMailRequest {
	String text;
	String subject;
	String to;
}
