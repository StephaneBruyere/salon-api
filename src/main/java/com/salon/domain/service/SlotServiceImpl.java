package com.salon.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;
import com.salon.domain.repository.SalonServiceDetailRepo;
import com.salon.domain.repository.SlotRepo;

@Service
public class SlotServiceImpl implements SlotService {

	@Autowired
	SlotRepo slotRepo;
	
	@Autowired
	SalonServiceDetailRepo salonServiceDetailRepo;
	
	@Override
	public List<Slot> findByAvailSlotForService(long selectedServiceId, LocalDate slotFor) {
		SalonServiceDetail selectedService = salonServiceDetailRepo.findById(selectedServiceId).orElseThrow();
		System.err.println(selectedService);
		return slotRepo.findBySelectedServiceAndSlotFor(selectedServiceId,slotFor);
	}

}
