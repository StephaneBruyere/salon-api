package com.salon.domain.service;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.salon.domain.model.Payment;
import com.salon.domain.model.PaymentRequest;
import com.salon.domain.model.PaymentStatus;
import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;
import com.salon.domain.model.Ticket;
import com.salon.domain.repository.PaymentRepo;
import com.salon.exception.SalonException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Validated
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
    PaymentRepo paymentRepo;

    @Autowired
    SalonService salonService;

    @Autowired
    SlotService slotService;
    @Autowired
    TicketService ticketService;

    @Value("${stripe.secret-key}")
    String stripeApiKey;

    @PostConstruct
    public void onBeansLoaded() {
        Stripe.apiKey = stripeApiKey;
    }

	@Override
	public Payment initiate(@Valid PaymentRequest paymentRequest) {
		Payment existingPayment = hasExistingPayment(paymentRequest);

        if (null != existingPayment)
            return existingPayment;

        Slot slot = slotService.findAvailableSlotId(paymentRequest.getSlotId()).orElseThrow(() -> new SalonException("Invalid Slot ID Or Slot Not Available"));
        SalonServiceDetail serviceDetail = salonService.findById(paymentRequest.getSelectedSalonServiceDetailId()).orElseThrow(() -> new SalonException("Invalid Salon Service ID"));

        Payment payment = asPayment(paymentRequest, slot, serviceDetail);

        PaymentIntent paymentIntent = createPayment(payment);

        log.info("paymentIntent" + paymentIntent.toJson());

        payment.setClientSecret(paymentIntent.getClientSecret());
        payment.setIntentId(paymentIntent.getId());

        slotService.setToLockedWithService(slot, serviceDetail);
        paymentRepo.save(payment);
        return payment;
	}

	@Override
	public Ticket confirm(Long paymentRequestId) {
		Payment payment = paymentRepo.findById(paymentRequestId).orElseThrow(() -> new SalonException("Invalid Payment ID "));
        validatePayment(payment.getIntentId());
        slotService.setToConfirmed(payment.getSlot());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepo.save(payment);
        return ticketService.book(payment);
	}
	
	private Payment hasExistingPayment(PaymentRequest paymentRequest) {
        Slot slot = slotService.findLockedSlotId(paymentRequest.getSlotId()).orElse(null);
        if (null == slot)
            return null;
        return paymentRepo.findBySlotAndEmail(slot, paymentRequest.getEmail());
    }
	
	private PaymentIntent createPayment(Payment payment) {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setCurrency("eur")
                        .setDescription("for Slot " + payment.getSlot().getId())
                        .setReceiptEmail(payment.getEmail())
                        .setAmount((payment.getAsCents()))
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();
        try {
            PaymentIntent intent = PaymentIntent.create(params);
            return intent;
        } catch (StripeException e) {
            e.printStackTrace();
            throw new SalonException(e.getMessage());
        }
    }
	
	private void validatePayment(String intentId) {
        try {
            PaymentIntent intent = PaymentIntent.retrieve(intentId);
            if (intent.getStatus().equalsIgnoreCase("succeeded") == false)
                throw new SalonException("Payment is not succeeded,Invalid Entry");
            log.info(intent.toJson());
        } catch (StripeException e) {
            e.printStackTrace();
            throw new SalonException(e.getMessage());
        }
    }

	private Payment asPayment(PaymentRequest paymentRequest, Slot slot, SalonServiceDetail serviceDetail) {
        Payment payment = new Payment();
        payment.setAmount(serviceDetail.getPrice());
        payment.setFirstName(paymentRequest.getFirstName());
        payment.setLastName(paymentRequest.getLastName());
        payment.setEmail(paymentRequest.getEmail());
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setPhoneNumber(paymentRequest.getPhoneNumber());
        payment.setSlot(slot);
        payment.setCreated(LocalDateTime.now());
        payment.setSelectedService(serviceDetail);
        return payment;

    }
	

}
