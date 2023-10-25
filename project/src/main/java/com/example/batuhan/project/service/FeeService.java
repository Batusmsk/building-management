package com.example.batuhan.project.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.batuhan.project.dto.FeeDto;
import com.example.batuhan.project.entity.Apartment;
import com.example.batuhan.project.entity.Fee;
import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.repository.FeeRepository;
import com.example.batuhan.project.request_response.FeeRequest;

@Service
public class FeeService {

	@Value("${fee}")
	private Integer fee;

	@Autowired
	PersonService personService;
	@Autowired
	ApartmentService apartmentService;
	@Autowired
	FeeRepository feeRepository;
	@Autowired
	PaymentService paymentService;

	public String createFee(FeeRequest request) {
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");

		if (!personService.findByEmail(request.getEmail()).isPresent())
			return "WARN001";

		Optional<Person> person = personService.findByEmail(request.getEmail());

		if (!apartmentService.getApartment(request.getBlockName(), request.getApartmentNo()).isPresent())
			return "WARN001";
		if (!personService.findByEmail(
				personService.findOwnerTheApartment(request.getBlockName(), request.getApartmentNo()).getEmail())
				.isPresent())
			return "WARN003.";
		Optional<Apartment> apartment = apartmentService.getApartment(request.getBlockName(), request.getApartmentNo());
		Integer feeAmount = fee * (apartment.get().getBaseArea());
		Fee fee = new Fee();

		fee.setApartment(request.getApartmentNo());
		fee.setBlockNo(request.getBlockName());
		fee.setPerson(person.get());
		fee.setPaidAmount(0);
		fee.setFeeAmount(feeAmount);
		fee.setFeeDate(ft.format(date));
		fee.setStatus(true);
		feeRepository.save(fee);
		return "WARN004";
	}

	public List<Fee> getFees(Integer status) {

		List<Fee> list = new ArrayList<>();

		if (status == 1) {
			for (var i : feeRepository.findAll()) {
				if (i.getStatus() == false) {
					list.add(i);
				}
			}

			for (var i : feeRepository.findAll()) {
				if (i.getStatus() == true) {
					list.add(i);
				}
			}
		} else if (status == 2) {
			for (var i : feeRepository.findAll()) {
				if (i.getStatus() == true) {
					list.add(i);
				}
			}

			for (var i : feeRepository.findAll()) {
				if (i.getStatus() == false) {
					list.add(i);
				}
			}
		} else {
			list = feeRepository.findAll();
		}

		return list;
	}

	public Optional<Fee> getFee(Integer id) {
		return feeRepository.findById(id);
	}

	public List<Fee> findByPerson(String email) {
		if (!personService.findByEmail(email).isPresent())
			return null;
		List<Fee> list = new ArrayList<>();
		for (var i : getFees(0)) {
			if (i.getPerson().getEmail().equals(email)) {
				list.add(i);
			}
		}
		return list;
	}

	public List<FeeDto> findByBlockNameAndApartmentNo(String blockName, Integer ApartmentNo) {
		if (!apartmentService.getApartment(blockName, ApartmentNo).isPresent())
			return null;
		List<FeeDto> list = new ArrayList<>();
		for (var i : feeRepository.findByApartment(apartmentService.getApartment(blockName, ApartmentNo).get())) {
			FeeDto feeDto = new FeeDto();
			feeDto.setApartmentNo(i.getApartment());
			feeDto.setBlockName(i.getBlockNo());
			feeDto.setEmail(i.getPerson().getEmail());
			feeDto.setFeeDate(i.getFeeDate());
			feeDto.setStatus(i.getStatus());
			feeDto.setFeeAmount(i.getFeeAmount());
			list.add(feeDto);
		}
		return list;
	}

	public String payFee(Integer id, Integer amount) {
		if (!getFee(id).isPresent())
			return "WARN006";
		if (getFee(id).get().getStatus() == false)
			return "WARN006";

		Optional<Fee> fee = getFee(id);

		var xxx = amount == 0 ? fee.get().getFeeAmount() : amount;

		var payment = paymentService.createPayment(id, fee.get().getPerson().getEmail(), xxx);
		if (payment == false)
			return "WARN";
		fee.get().setStatus(false);
		fee.get().setPaidAmount(fee.get().getFeeAmount());
		feeRepository.save(fee.get());
		return "WARN007";
	}

	public long topla(long a, long b) {
		return a + b;
	}

	public String amountWithPayment(Integer id, Integer amount) {
		if (!getFee(id).isPresent())
			return "WARN005";
		if (getFee(id).get().getStatus() == false)
			return "WARN006";
		Fee fee = getFee(id).get();
		if (!fee.getStatus())
			return "WARN006";

		if (amount >= (fee.getFeeAmount() - fee.getPaidAmount())) {

			if (amount > (fee.getFeeAmount() - fee.getPaidAmount()))
				return "WARN009";

			return payFee(id, amount);
		}

		fee.setPaidAmount(fee.getPaidAmount() + amount);
		if (fee.getFeeAmount() <= fee.getPaidAmount()) {
			fee.setStatus(false);
		}

		var payment = paymentService.createPayment(id, fee.getPerson().getEmail(), amount);
		if (payment == false)
			return "WARN";
		feeRepository.save(fee);
		return "WARN007";

	}

	@Scheduled(cron = "0 */10 * ? * *")
	public boolean addFeeForAllPerson() {
		Integer person = 0;
		Integer apartment = 0;
		for (var p : personService.getPersons()) {
			person++;
			for (var a : personService.findApartmentsByPerson(p.getEmail())) {
				apartment++;
				FeeRequest request = new FeeRequest();
				request.setApartmentNo(a.getApartmentNo());
				request.setBlockName(a.getBlock().getBlockName());
				request.setEmail(p.getEmail());
				createFee(request);
			}
		}

		return true;
	}

}
