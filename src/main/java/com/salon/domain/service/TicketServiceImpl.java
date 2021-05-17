package com.salon.domain.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.domain.model.Payment;
import com.salon.domain.model.Ticket;
import com.salon.domain.repository.TicketRepo;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	TicketRepo ticketRepo;

	@Autowired
	SalonService salonService;

	@Transactional
	public Ticket book(Payment payment) {
		Ticket ticket = new Ticket();
		ticket.setPayment(payment);
		return ticketRepo.save(ticket);
	}

	public Optional<Ticket> findById(Long id) {
		return ticketRepo.findById(id);
	}

}
