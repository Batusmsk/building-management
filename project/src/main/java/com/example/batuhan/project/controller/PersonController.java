package com.example.batuhan.project.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.batuhan.project.auth.TokenManager;
import com.example.batuhan.project.dto.PurchaseApartmentDto;
import com.example.batuhan.project.entity.Apartment;
import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.locale.MyLocaleResolver;
import com.example.batuhan.project.repository.PersonRepository;
import com.example.batuhan.project.request_response.FindOwnerResponse;
import com.example.batuhan.project.request_response.RegisterRequest;
import com.example.batuhan.project.request_response.RemoveApartmentToPersonRequest;
import com.example.batuhan.project.request_response.UpdateProfileRequest;
import com.example.batuhan.project.service.PersonService;

@RestController
public class PersonController {
	@Autowired
	PersonService personService;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MyLocaleResolver myLocaleResolver;
	@Autowired
	TokenManager tokenManager;
	
	@PostMapping(value = "/person/createPerson", produces = "text/plain;charset=UTF-8")
	public String createPerson(HttpServletRequest request, @RequestBody RegisterRequest registerRequest) {
		return messageSource.getMessage(personService.createPerson(registerRequest), null, myLocaleResolver.resolveLocale(request));
	}
	
	@GetMapping(value = "/person/getPerson/{mail}")
	//@PreAuthorize("isAuthenticated()")
	public Optional<Person> getPerson(@PathVariable("mail") String mail) {
		return personService.findByEmail(mail);
	}
	@GetMapping(value = "/person/getPersons")
	public List<Person> getPersons() {
		return personRepository.findAll();
	}
	@PostMapping(value = "/person/addRole/{mail}/{role}", produces = "text/plain;charset=UTF-8")
	public String addRole(HttpServletRequest request, @PathVariable("mail") String mail, @PathVariable("role") String role) {
		return messageSource.getMessage(personService.addRole(mail, role), null, myLocaleResolver.resolveLocale(request));
	}
	@GetMapping(value ="/person/resetPassword")
	public boolean resetPassword(@RequestParam Integer id) {
		return personService.resetPassword(id);
	}
	@GetMapping(value="/person/deletePerson", produces = "text/plain;charset=UTF-8")
	public String deletePerson(HttpServletRequest request, @RequestParam Integer id) {
		return messageSource.getMessage(personService.deletePerson(id), null, myLocaleResolver.resolveLocale(request)); 
	}
	@PostMapping(value = "/person/removeRole/{mail}/{role}", produces = "text/plain;charset=UTF-8")
	public String removeRole(HttpServletRequest request, @PathVariable("mail") String mail, @PathVariable("role") String role) {
		return messageSource.getMessage(personService.removeRole(mail, role), null, myLocaleResolver.resolveLocale(request));
	}
	@PostMapping(value = "/person/addApartmentToPerson", produces = "text/plain;charset=UTF-8")
	public String addApartmentToPerson(HttpServletRequest request, @RequestBody PurchaseApartmentDto purchaseApartmentDto) {
		return messageSource.getMessage(personService.addApartmentToPerson(purchaseApartmentDto), null, myLocaleResolver.resolveLocale(request));
	}
	@PostMapping(value = "/person/removeApartmentToPerson", produces = "text/plain;charset=UTF-8")
	public String removeApartmentToPerson(HttpServletRequest request, @RequestBody RemoveApartmentToPersonRequest removeApartmentToPersonRequest) {
		return messageSource.getMessage(personService.removeApartmentToPerson(removeApartmentToPersonRequest), null, myLocaleResolver.resolveLocale(request));
	}
	@GetMapping(value = "/person/findOwnerTheApartment/{blockName}/{apartmentNo}")
	public FindOwnerResponse findOwnerTheApartment(@PathVariable("blockName") String blockName, @PathVariable("apartmentNo") Integer apartmentNo) {
		return personService.findOwnerTheApartment(blockName, apartmentNo);
	}
	
	@GetMapping(value = "/person/findApartmentsByPerson/{email}")
	public List<Apartment> findApartmentsByPerson(@PathVariable("email") String email) {
		return personService.findApartmentsByPerson(email);	
	}
	@GetMapping(value = "/person/changePassword", produces = "text/plain;charset=UTF-8")
	public String changePassword(HttpServletRequest request, @RequestHeader(value = "Authorization") String token, @RequestParam String newPassword, @RequestParam String oldPassword) {
		String userToken = token.replace("Bearer ", "");
		String email = tokenManager.getEmailToken(userToken);

		return messageSource.getMessage(personService.changePassword(email, newPassword, oldPassword), null, myLocaleResolver.resolveLocale(request));
	}
	
	@GetMapping(value = "/person/findById")
	public Person findById(@RequestParam Integer id) {
		return personRepository.findById(id).get();
	}
	
	@PostMapping(value = "/person/updateEmailAndNumber", produces = "text/plain;charset=UTF-8")
	public String updateEmailAndNumber(HttpServletRequest request, @RequestHeader(value = "Authorization") String token, @RequestBody UpdateProfileRequest updateProfileRequest) {
		
		String userToken = token.replace("Bearer ", "");
		String email = tokenManager.getEmailToken(userToken);
		
		return messageSource.getMessage(personService.updateEmailAndNumber(updateProfileRequest, email), null, myLocaleResolver.resolveLocale(request));
	}
}
 