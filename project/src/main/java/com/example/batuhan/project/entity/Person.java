package com.example.batuhan.project.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String email;
	@JsonIgnore
	private String password;
	private String phoneNumber;
	private String name;
	private String lastName;
	
    @ElementCollection(targetClass = PersonRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "person_roles", joinColumns = @JoinColumn(name = "personId"))
    @Enumerated(EnumType.STRING) // rolün enum string değerlerini veritabanında sakla.
    private Set<PersonRole> roles;
    
    
    
   @OneToMany(fetch = FetchType.EAGER)
   @JoinColumn(name = "email")
   List<Apartment> personApartments = new ArrayList<>();

}
