package com.salon.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    private SalonServiceDetail selectedService;

    @ManyToOne(fetch = FetchType.EAGER)
    private Slot slot;
    
	private long amount;
	 private LocalDateTime created;
	 private LocalDateTime updated;
	 private PaymentStatus status;
	
	private String intentId;
	
	private String clientSecret;
	
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	
	public String getName() {
        return firstName + " " + lastName;
    }

    public long getAsCents() {
        return amount * 100;
    }

}
