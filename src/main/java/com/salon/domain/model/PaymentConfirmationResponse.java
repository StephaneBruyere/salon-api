package com.salon.domain.model;


import com.salon.config.SalonDetails;

import lombok.Data;

@Data
public class PaymentConfirmationResponse {
    Ticket ticket;
    SalonDetails salonDetails;
}