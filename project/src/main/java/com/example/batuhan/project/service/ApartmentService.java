package com.example.batuhan.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.dto.ApartmentDto;
import com.example.batuhan.project.entity.Apartment;
import com.example.batuhan.project.repository.ApartmentRepository;

@Service
public class ApartmentService {
	@Autowired
	ApartmentRepository apartmentRepository;
	@Autowired
	BlockService blockService;
	@Autowired
	PersonService personService;

	public String createApartment(ApartmentDto apartmentDto) {
		if (!blockService.getBlock(apartmentDto.getBlockName().toUpperCase()).isPresent())
			return "Blok bulunamadı";
		if ((blockService.getBlock(apartmentDto.getBlockName()).get().getNumberOfFloors()) < apartmentDto.getFloor())
			return "Bu katta bulunan dairelerin toplam metre kareyi aşıyor";
		if (getApartment(apartmentDto.getBlockName(), apartmentDto.getApartmentNo()).isPresent())
			return "Bir blokta aynı numaraya sahip iki daire olamaz";
		Integer baseArea = apartmentDto.getBaseArea();
		for (var i : aptOnTheFloor(apartmentDto.getBlockName(), apartmentDto.getFloor())) {
			baseArea += i.getBaseArea();
		}
		if (baseArea <= blockService.getBlock(apartmentDto.getBlockName()).get().getBaseArea()) {
			Apartment apartment = new Apartment();
			apartment.setApartmentNo(apartmentDto.getApartmentNo());
			apartment.setBaseArea(apartmentDto.getBaseArea());
			apartment.setFloor(apartmentDto.getFloor());
			apartment.setBlock(blockService.getBlock(apartmentDto.getBlockName().toUpperCase()).get());
			apartmentRepository.save(apartment);
			return "Daire oluşturuldu";
		}
		return "Katta yer alan dairelerin metrekareleri bloğun metrekaresini aşıyor.";
	}

	public List<ApartmentDto> aptOnTheFloor(String blockName, Integer floor) {
		List<ApartmentDto> list = new ArrayList<>();
		if (!blockService.getBlock(blockName).isPresent())
			return list;
		for (var i : apartmentRepository.findAll()) {

			if (i.getBlock().getBlockName().toLowerCase().equals(blockName.toLowerCase())
					&& i.getFloor().equals(floor)) {

				ApartmentDto apartmentDto = new ApartmentDto();
				apartmentDto.setApartmentNo(i.getApartmentNo());
				apartmentDto.setBaseArea(i.getBaseArea());
				apartmentDto.setBlockName(i.getBlock().getBlockName());
				apartmentDto.setFloor(i.getFloor());
				apartmentDto.setPurchaseDate(i.getPurchaseDate());
				list.add(apartmentDto);
			}
		}
		return list;
	}

	public List<Apartment> getApartments() {

		return apartmentRepository.findAll();
	}

	public Optional<Apartment> getApartment(String blockName, Integer apartmentId) {
		for (var i : getApartments()) {
			if (i.getApartmentNo() == apartmentId && i.getBlock().getBlockName().equals(blockName)) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

	public List<ApartmentDto> findApartmentsById(Integer id) {
		List<ApartmentDto> list = new ArrayList<>();
		for (var i : getApartments()) {
			if (i.getApartmentNo() == id) {
				ApartmentDto dto = new ApartmentDto();
				dto.setApartmentNo(i.getApartmentNo());
				dto.setBaseArea(i.getBaseArea());
				dto.setBlockName(i.getBlock().getBlockName());
				dto.setFloor(i.getFloor());
				dto.setPurchaseDate(i.getPurchaseDate());
				list.add(dto);
			}
		}
		return list;
	}

	public List<ApartmentDto> findApartmentsByBlockName(String block) {
		List<ApartmentDto> list = new ArrayList<>();
		for (var i : getApartments()) {
			if (i.getBlock().getBlockName().equals(block)) {
				ApartmentDto dto = new ApartmentDto();
				dto.setPersonId(i.getPersonId());
				dto.setApartmentNo(i.getApartmentNo());
				dto.setBaseArea(i.getBaseArea());
				dto.setBlockName(i.getBlock().getBlockName());
				dto.setFloor(i.getFloor());
				dto.setPurchaseDate(i.getPurchaseDate());
				list.add(dto);
			}
		}
		return list;
	}

	public String deleteApartment(String blockName, Integer apartmentNo) {
		if (getApartment(blockName, apartmentNo).isPresent()) {
			if (getApartment(blockName, apartmentNo).get().getPurchaseDate() != null) {
				apartmentRepository.delete(getApartment(blockName, apartmentNo).get());
				return "Daire silindi";
			} else {
				return "Bu daire'de oturanlar var bu yüzden daire silinemez";
			}

		}
		return "Daire bulunamadı";
	}

	public List<ApartmentDto> findApartmentsByPerson(String email) {
		List<ApartmentDto> apartments = new ArrayList<>();
		if (!personService.findByEmail(email).isPresent())
			return null;
		for (var i : getApartments()) {
			if (i.getPersonId() == personService.findByEmail(email).get().getId()) {
				ApartmentDto dto = new ApartmentDto();
				dto.setApartmentNo(i.getApartmentNo());
				dto.setBaseArea(i.getBaseArea());
				dto.setBlockName(i.getBlock().getBlockName());
				dto.setFloor(i.getFloor());
				dto.setPurchaseDate(i.getPurchaseDate());
				apartments.add(dto);
			}
		}
		return apartments;
	}

	public void saveOrUpdateApartment(Apartment apartment) {
		apartmentRepository.save(apartment);
	}

}
