package com.example.batuhan.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.batuhan.project.entity.Apartment;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer>{

}
