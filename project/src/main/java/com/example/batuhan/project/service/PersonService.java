package com.example.batuhan.project.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.auth.TokenManager;
import com.example.batuhan.project.auth.UserDetailsService;
import com.example.batuhan.project.dto.PurchaseApartmentDto;
import com.example.batuhan.project.entity.Apartment;
import com.example.batuhan.project.entity.Fee;
import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.entity.PersonRole;
import com.example.batuhan.project.repository.PersonRepository;
import com.example.batuhan.project.request_response.FindOwnerResponse;
import com.example.batuhan.project.request_response.RegisterRequest;
import com.example.batuhan.project.request_response.RemoveApartmentToPersonRequest;
import com.example.batuhan.project.request_response.UpdateProfileRequest;

@Service
public class PersonService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	BlockService blockService;
	@Autowired
	ApartmentService apartmentService;
	@Autowired
	FeeService feeService;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	TicketService ticketService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private AuthenticationManager authenticationManager;
	public String createPerson(RegisterRequest request) {
		if(personRepository.findByEmail(request.getEmail()).isPresent()) return "WARN030";
		Set<PersonRole> roles = new HashSet<>();
		roles.add(PersonRole.USER);
		
		Person person = new Person();
		person.setName(request.getName());
		person.setLastName(request.getLastName());
		person.setEmail(request.getEmail());
		//person.setPassword(passwordEncoder.encode(request.getPassword()));
		person.setRoles(roles);
		person.setPhoneNumber(request.getPhoneNumber());
		personRepository.save(person);
		
		return "WARN010";
	}
	
	public String deletePerson(Integer id) {
		Optional<Person> person = personRepository.findById(id);
		if(person.isPresent()) {
			List<Fee> fee = feeService.findByPerson(person.get().getEmail());
			if(fee.size() > 0) {
				return "WARN032";
			}
			ticketService.deleteTicketFromPerson(person.get().getEmail());
			removeRole(person.get().getEmail(), "admin");
			removeRole(person.get().getEmail(), "user");
			personRepository.delete(person.get());
			return "WARN031";
		} else {
			return "WARN001";
		}
	}
	
	public String addRole(String mail, String role) {
		Person person = findByEmail(mail).get();
		Set<PersonRole> roles = person.getRoles();
		
		if(role.toLowerCase().equals("user")) {
			roles.add(PersonRole.USER);
		} else if(role.toLowerCase().equals("admin")) {
			roles.add(PersonRole.ADMIN);
		}
		
		person.setRoles(roles);
		personRepository.save(person);
		return "WARN011";
	}
	
	public String removeRole(String mail, String role) {
		Person person = findByEmail(mail).get();
		Set<PersonRole> roles = person.getRoles();
		
		if(role.toLowerCase().equals("user")) {
			roles.remove(PersonRole.USER);
		} else if(role.toLowerCase().equals("admin")) {
			roles.remove(PersonRole.ADMIN);
		}
		
		person.setRoles(roles);
		personRepository.save(person);
		return "WARN012";
	}
	
	public Optional<Person> findByEmail(String mail) {
		if(!personRepository.findByEmail(mail).isPresent()) return Optional.empty();
		return personRepository.findByEmail(mail);
	}
	
	public List<FindOwnerResponse> getPersons() {
		List<FindOwnerResponse> list = new ArrayList<>();
		
		for(var i:personRepository.findAll()) {
			FindOwnerResponse response = new FindOwnerResponse();
			response.setEmail(i.getEmail());
			response.setLastName(i.getLastName());
			response.setName(i.getName());
			response.setPhoneNumber(i.getPhoneNumber());
			response.setRoles(i.getRoles().toString());
			list.add(response);
		}
		return list;
	}
	public String addApartmentToPerson(PurchaseApartmentDto dto) {
		if(!apartmentService.getApartment(dto.getBlockNo(), dto.getApartmentNo()).isPresent()) return "WARN002";
		if(findByEmail(dto.getEmail()).isPresent() == false) return "WARN001";
		if(apartmentService.getApartment(dto.getBlockNo(), dto.getApartmentNo()).get().getPurchaseDate() != null && apartmentService.getApartment(dto.getBlockNo(), dto.getApartmentNo()).get().getPurchaseDate().length() > 3) return "WARN013";
		Optional<Person> person = findByEmail(dto.getEmail());
		List<Apartment> list = person.get().getPersonApartments();	
		Optional<Apartment> apartment = apartmentService.getApartment(dto.getBlockNo(), dto.getApartmentNo());
		apartment.get().setPurchaseDate(dto.getPurchaseDate());
		apartment.get().setPersonId(person.get().getId());
		list.add(apartment.get());
		person.get().setPersonApartments(list);
		personRepository.save(person.get());
		apartmentService.saveOrUpdateApartment(apartment.get());
		return "WARN014";
	}
	
    public List<Apartment> findApartmentsByPerson(String email) {
    	if(findByEmail(email).isPresent() == false) return null;
    	List<Apartment> list = new ArrayList<>();
    	for(var i : apartmentService.getApartments()) {
    		if(i.getPersonId() == findByEmail(email).get().getId()) {
    			list.add(i);
    		}
    	}
    	//List<Apartment> list = findByEmail(email).get().getPersonApartments();
        return list;
    }
    
	public FindOwnerResponse findOwnerTheApartment(String blockName, Integer apartmentNo) {
		try {
		if(!apartmentService.getApartment(blockName, apartmentNo).isPresent()) return null;
		if(!findByEmail(personRepository.findById(apartmentService.getApartment(blockName, apartmentNo).get().getPersonId()).get().getEmail()).isPresent()) return null;
		
		Optional<Person> person = personRepository.findById(apartmentService.getApartment(blockName, apartmentNo).get().getPersonId());
		FindOwnerResponse response = new FindOwnerResponse();
		response.setEmail(person.get().getEmail());
		response.setLastName(person.get().getLastName());
		response.setName(person.get().getName());
		response.setPhoneNumber(person.get().getPhoneNumber());
		response.setRoles(person.get().getRoles().toString());
			return response;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String removeApartmentToPerson(RemoveApartmentToPersonRequest removeApartmentToPersonRequest) {
		if(!apartmentService.getApartment(removeApartmentToPersonRequest.getBlockNo(), removeApartmentToPersonRequest.getApartmentNo()).isPresent()) return "WARN002";
		if(findByEmail(removeApartmentToPersonRequest.getEmail()).isPresent() == false) return "WARN001";
		
		for(var i:feeService.findByPerson(removeApartmentToPersonRequest.getEmail())) {
			if(i.getStatus() == true) {
				return "WARN015";
			}
		}
		
		if(apartmentService.getApartment(removeApartmentToPersonRequest.getBlockNo(), removeApartmentToPersonRequest.getApartmentNo()).get().getPurchaseDate() == null || apartmentService.getApartment(removeApartmentToPersonRequest.getBlockNo(), removeApartmentToPersonRequest.getApartmentNo()).get().getPurchaseDate().length() < 4) return "WARN016";
		Optional<Apartment> apartment = apartmentService.getApartment(removeApartmentToPersonRequest.getBlockNo(), removeApartmentToPersonRequest.getApartmentNo());
		Optional<Person> person = findByEmail(removeApartmentToPersonRequest.getEmail());
		
		if(apartmentService.getApartment(removeApartmentToPersonRequest.getBlockNo(), removeApartmentToPersonRequest.getApartmentNo()).get().getPersonId() != findByEmail(removeApartmentToPersonRequest.getEmail()).get().getId()) {
			return "WARN031";
		}
		
		List<Apartment> list = person.get().getPersonApartments();
		for(var i:list) {
			if(i.getApartmentNo() == removeApartmentToPersonRequest.getApartmentNo() && i.getBlock().getBlockName().equals(removeApartmentToPersonRequest.getBlockNo())) {
				list.remove(i);
				break;
			} else {
				continue;
			}
		}
		person.get().setPersonApartments(list);
		apartment.get().setPurchaseDate(null);
		apartment.get().setPersonId(null);
		
		personRepository.save(person.get());
		apartmentService.saveOrUpdateApartment(apartment.get());
		
		return "WARN017";
	}
	
	public boolean resetPassword(Integer id) {
		System.out.println(id.toString());
		if(!personRepository.findById(id).isPresent()) return false;
		Person person = personRepository.findById(id).get();
		person.setPassword(null);
		personRepository.save(person);
		return true;
	}
	
	public String updateEmailAndNumber(UpdateProfileRequest request, String userEmail) {
		if(!findByEmail(userEmail).isPresent()) return "WARN001";
		Person person = findByEmail(userEmail).get();
		
		for(var i:getPersons()) {
			if(i.getEmail().toLowerCase().equals(request.getEmail())) {
				return "WARN021";
			}
			if(i.getPhoneNumber().equals(request.getPhoneNumber())) {
				return "WARN022";
			}
		}
		
		String email = (request.getEmail() == null ||  request.getEmail().equals("null")) ? person.getEmail() : request.getEmail();
		String number = (request.getPhoneNumber() == null || request.getPhoneNumber().equals("null")) ?  person.getPhoneNumber() : request.getPhoneNumber();
		person.setEmail(email);
		person.setPhoneNumber(number);
		personRepository.save(person);
		return "WARN020";
	}
	public String changePassword(String email, String newPassword, String oldPassword) {
		if(!findByEmail(email).isPresent()) return "WARN001";
		Person person = findByEmail(email).get();
		
		if(!passwordEncoder.matches(oldPassword, person.getPassword())) return "WARN018";
		person.setPassword(passwordEncoder.encode(newPassword));
		personRepository.save(person);
		return "WARN019";
		
	}
}
 