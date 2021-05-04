package com.salon.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class SalonDetails {
	@Id
	private String name="AR Salon and Day Spa Services";
	private String address="1234 Main Street, Anytown";
	private String city="NYC";
	private String state="ND";
	private String zipcode="58201";
	private String phone="555-123-4567";
	

}
