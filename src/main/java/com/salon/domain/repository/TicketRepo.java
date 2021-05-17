package com.salon.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.salon.domain.model.Ticket;

public interface TicketRepo extends CrudRepository<Ticket, Long> {

}
