package com.example.batuhan.project.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.entity.PasswordResetTokenEntity;
import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.mail.MailService;
import com.example.batuhan.project.mail.SendMailRequest;
import com.example.batuhan.project.repository.PasswordResetTokenRepository;
import com.example.batuhan.project.repository.PersonRepository;
import com.example.batuhan.project.request_response.ResetPasswordRequest;
import com.example.batuhan.project.service.PersonService;

@Service
public class PasswordResetService {

	@Autowired
	PersonService personService;
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	PasswordReset passwordReset;
	@Autowired
	MailService mailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    PersonRepository personRepository;
	public String createToken(String mail) {
		if (!personService.findByEmail(mail).isPresent())
			return "WARN034";
		PasswordReset token = new PasswordReset();

		var x = true;
		if (passwordResetTokenRepository.findById(mail).isPresent()) {
			if (isExpired(mail) == true) {
				passwordResetTokenRepository.deleteById(mail);
				x = false;
			}
		} else {
			x = false;
		}
		
		if (x)
			return "WARN035";

		PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
		entity.setEmail(mail);
		entity.setToken(token.getToken());
		entity.setExpirationDate(token.getExpirationDate());
		passwordResetTokenRepository.save(entity);
		
		SendMailRequest request = new SendMailRequest();
		String text = "Şifre sıfırlama isteğinde bulundunuz. http://my.batuhan.com:5500/pages/reset-password.html?token="+token.getToken()+" Adresinden şifrenizi sıfırlayabilirsiniz";;
		request.setTo(mail);
		request.setText(text);
		request.setSubject("Şifre sıfırlama isteği");
		mailService.sendMail(request);
		
		return "WARN036";
	}
	
	public String resetPassword(ResetPasswordRequest request) {
		System.out.println(request.getToken()+ "  " + request.getNewPassword());
		if(!passwordResetTokenRepository.findByToken(request.getToken()).isPresent()) return "WARN037";
		PasswordResetTokenEntity token = passwordResetTokenRepository.findByToken(request.getToken()).get();
		
		if(isExpired(request.getToken())) {
			passwordResetTokenRepository.deleteById(token.getEmail());
			return "WARN038";
		}
		if(!personService.findByEmail(token.getEmail()).isPresent()) return "WARN001";
		Person person = personService.findByEmail(token.getEmail()).get();
		
		if(request.getNewPassword().length() <= 5) return "WARN039";
		if(passwordEncoder.matches(request.getNewPassword(), person.getPassword())) return "WARN040";
		
		person.setPassword(passwordEncoder.encode(request.getNewPassword()));
		
		personRepository.save(person);
		passwordResetTokenRepository.deleteById(token.getEmail());
		
		SendMailRequest mail = new SendMailRequest();
		String text = "Şifrenizi başarıyla sıfırladınız eğer bir hata olduğunu düşünüyorsanız yetkililer ile iletişime geçiniz.";
		mail.setTo(token.getEmail());
		mail.setText(text);
		mail.setSubject("Şifreniz Sıfırlandı");
		mailService.sendMail(mail);
		
		return "WARN041";
	}
	
	public boolean isExpired(String token) {
		return passwordReset.isExpired(token);
	}
}
