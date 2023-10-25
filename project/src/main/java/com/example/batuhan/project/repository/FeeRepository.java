package com.example.batuhan.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.batuhan.project.entity.Apartment;
import com.example.batuhan.project.entity.Fee;
import com.example.batuhan.project.entity.Person;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Integer>{
	public List<Fee> findByPerson(Person person);
	public List<Fee> findByApartment(Apartment apartment);
}
