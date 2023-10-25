package com.example.batuhan.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.batuhan.project.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
	public Optional<Person> findByEmail(String email);
}
