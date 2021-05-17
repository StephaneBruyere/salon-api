package com.salon.api;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.salon.config.SalonDetails;
import com.salon.domain.model.Payment;
import com.salon.domain.model.PaymentConfirmationResponse;
import com.salon.domain.model.PaymentRequest;
import com.salon.domain.model.Ticket;
import com.salon.domain.service.PaymentService;
import com.salon.domain.service.TicketService;
import com.salon.exception.SalonException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000"})
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private SalonDetails salonDetails;
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping("/payments/initiate")
	@ApiOperation(value = "InitiatePaymentAPI")
	public  ResponseEntity<Payment> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
		try {
            return ResponseEntity.ok(paymentService.initiate(paymentRequest));
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (SalonException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
	}
	
	@PutMapping("/payments/confirm/{id}")
	@ApiOperation(value = "VerifyPaymentAndConfirmSlotAPI")
	public  ResponseEntity<PaymentConfirmationResponse> verifyPaymentAndConfirmSlotAPI(@PathVariable Long id) {
        try {
            Ticket ticket = paymentService.confirm(id);
            PaymentConfirmationResponse paymentConfirmationResponse = new PaymentConfirmationResponse();
            paymentConfirmationResponse.setSalonDetails(salonDetails.clone());
            paymentConfirmationResponse.setTicket(ticket);
            return ResponseEntity.ok(paymentConfirmationResponse);
        } catch (SalonException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
	}
	
	@GetMapping("/tickets/{ticketId}")
    @ApiOperation(value = "VerifyTicketAPI")
    public ResponseEntity<Ticket> verifyTicketAPI(@PathVariable Long ticketId) {
		return ResponseEntity.ok(ticketService.findById(ticketId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Ticket ID", null)));
    }

}
