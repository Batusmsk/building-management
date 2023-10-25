package com.example.batuhan.project.auth;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.batuhan.project.entity.PersonRole;
import com.example.batuhan.project.locale.MyLocaleResolver;
import com.example.batuhan.project.request_response.LoginRequest;
import com.example.batuhan.project.request_response.RegisterRequest;
import com.example.batuhan.project.request_response.ResetPasswordRequest;
import com.example.batuhan.project.service.PersonService;

@RestController
public class AuthConroller {

    @Autowired
    private TokenManager tokenManager;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MyLocaleResolver myLocaleResolver;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    PersonService personService;
    @Autowired
    PasswordResetService passwordResetService;
    @PostMapping(value = "/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            return ResponseEntity.ok(tokenManager.generateToken(loginRequest.getEmail()));
        } catch (Exception e) {
            throw e;
        }
    }
    
    @PostMapping(value = "/user/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
    	return ResponseEntity.ok(personService.createPerson(registerRequest));
    }
    
    @GetMapping(value = "/user/getUserRoles")
    public Set<PersonRole> getUserRoles(@RequestParam String token) {
    	return personService.findByEmail(tokenManager.getEmailToken(token)).get().getRoles();
    }
    //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6ImJhdHVoYW4iLCJyb2xlcyI6IltVU0VSXSIsImlhdCI6MTY3OTU3ODQ1NywiZXhwIjoxNjc5NTc4NzU3fQ.2VpiWc2JE74_HY-MzK_jdD7fIR60OvWHWTFcxf-7LIk
    @GetMapping(value = "/user/tokenControl")
    public Boolean tokenControl(@RequestParam String token) {
    	return tokenManager.tokenControl(token);
    }
    @GetMapping(value = "/user/getEmail", produces = "text/plain;charset=UTF-8")
    public String getEmailToken(@RequestParam String token) {
    	return tokenManager.getEmailToken(token);
    }
    
    @GetMapping(value = "/resetPassword/createToken", produces = "text/plain;charset=UTF-8")
    public String resetPassword(HttpServletRequest servlet,@RequestParam String email) {
    	return messageSource.getMessage(passwordResetService.createToken(email), null, myLocaleResolver.resolveLocale(servlet));
    }
    
    @GetMapping(value = "/resetPassword/isExpired")
    public boolean resetPasswordIsExpired(HttpServletRequest servlet,@RequestParam String request) {
    	return passwordResetService.isExpired(request);
    }
    
    @PostMapping(value = "/resetPassword/resetPassword", produces = "text/plain;charset=UTF-8")
    public String resetPassword(HttpServletRequest servlet, @RequestBody ResetPasswordRequest request) {
    	
    	return messageSource.getMessage(passwordResetService.resetPassword(request), null, myLocaleResolver.resolveLocale(servlet));
    }

}
