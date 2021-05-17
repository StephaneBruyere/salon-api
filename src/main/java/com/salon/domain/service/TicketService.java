package com.salon.domain.service;

import java.util.Optional;

import com.salon.domain.model.Payment;
import com.salon.domain.model.Ticket;

public interface TicketService {
	
	public Ticket book(Payment payment);
	public Optional<Ticket> findById(Long id);	

}
