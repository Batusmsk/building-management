package com.example.batuhan.project.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.repository.PersonRepository;
import com.example.batuhan.project.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{
	
	  private Map<String, String> users = new HashMap<>();

	    @Autowired
	    private BCryptPasswordEncoder passwordEncoder;
	    @Autowired
	    PersonRepository personRepository;
	    @Autowired
	    PersonService personService;

	  
	    @Override
	    public UserDetails loadUserByUsername(String email)  {
	    	
	    	try {
		        Person person = personService.findByEmail(email).get();
		        
		        var user= User.builder()
		          .username(person.getEmail())
		          .password(person.getPassword())
		          .roles(person.getRoles().stream().map(Enum::name).toArray(String[]::new))
		          .build();
		        
		        ObjectMapper mapper=new ObjectMapper();
		        
		        System.out.println(mapper.writeValueAsString(user));
		        return user;
		        
	    	} catch (Exception e) {
	    		System.err.println(e);
	    		return null;
	    	}
   
	    	/*
	    	users.clear();
	    	for(var i:personRepository.findAll()) {
	    		users.put(i.getEmail(), i.getPassword());
	    	}
	    	
	        if (users.containsKey(email)) {
	            return new User(email, users.get(email), new ArrayList<>());
	        }

	        throw new UsernameNotFoundException(email);
	        */
	    }
	}