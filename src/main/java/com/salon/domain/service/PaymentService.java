package com.salon.domain.service;

import javax.validation.Valid;

import com.salon.domain.model.Payment;
import com.salon.domain.model.PaymentRequest;
import com.salon.domain.model.Ticket;

public interface PaymentService {
	
	public Payment initiate(@Valid PaymentRequest paymentRequest);
	
	public Ticket confirm(Long paymentRequestId);
	

}
