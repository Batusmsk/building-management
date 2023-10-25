package com.example.batuhan.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.batuhan.project.dto.ApartmentDto;
import com.example.batuhan.project.entity.Apartment;
import com.example.batuhan.project.service.ApartmentService;

@RestController
public class ApartmentController {
	@Autowired
	
	ApartmentService apartmentService;
	@PostMapping(value = "/apartment/createApartment")
	public String createApartment(@RequestBody ApartmentDto apartment) {
		return apartmentService.createApartment(apartment);
	}
	//aptOnTheFloor
	@GetMapping(value = "/apartment/aptOnTheFloor")
	public ResponseEntity<List<ApartmentDto>> getAptOnTheFloor(@RequestParam String block, @RequestParam Integer floor) {
		return new ResponseEntity<List<ApartmentDto>>(apartmentService.aptOnTheFloor(block.toUpperCase(), floor), HttpStatus.OK);
	}
	
	@GetMapping(value = "/apartment/getApartment")
	public Optional<Apartment> getApartment(@RequestParam String blockName, @RequestParam Integer apartmentId) {
		return apartmentService.getApartment(blockName.toUpperCase(), apartmentId);
	}
	@GetMapping(value = "/apartment/findApartmentsById")
	public List<ApartmentDto> getApartment(@RequestParam Integer apartmentId) {
		return apartmentService.findApartmentsById(apartmentId);
	}
	@GetMapping(value = "/apartment/getApartments")
	public List<Apartment> getApartments() {
		return apartmentService.getApartments();
	}
	@GetMapping(value = "/apartment/findApartmentsByBlockName")
	public List<ApartmentDto> findApartmentsByBlockName(@RequestParam String blockName) {
		return apartmentService.findApartmentsByBlockName(blockName.toUpperCase());
	}
	@GetMapping(value = "/apartment/delete")
	public String deleteApartment(@RequestParam String blockName, @RequestParam Integer apartmentId) {
		return apartmentService.deleteApartment(blockName.toUpperCase(), apartmentId);
	}
	@GetMapping(value = "/apartment/findApartmentsByPerson")
	public List<ApartmentDto> findApartmentsByPerson(@RequestParam String email) {
		return apartmentService.findApartmentsByPerson(email);
	}
}
