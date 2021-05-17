package com.salon.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.salon.domain.model.Payment;
import com.salon.domain.model.Slot;

public interface PaymentRepo extends CrudRepository<Payment, Long> {
	
	public Payment findBySlotAndEmail(Slot slot, String email);	

}
