package com.example.batuhan.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.batuhan.project.entity.Person;
import com.example.batuhan.project.entity.TicketMessage;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessage, Integer>{
	public List<TicketMessage> findByPerson(Person person);
	public List<TicketMessage> findByTicketId(Integer id);
}
