package com.salon.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;
import com.salon.domain.model.SlotStatus;
import com.salon.domain.repository.SalonServiceDetailRepo;
import com.salon.domain.repository.SlotRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SlotServiceImpl implements SlotService {

	@Autowired
	SlotRepo slotRepo;
	
	@Autowired
	SalonServiceDetailRepo salonServiceDetailRepo;
	
	// my solution
	@Override
	public List<Slot> findByAvailSlotsForService(long selectedServiceId, LocalDate slotFor) {
//		SalonServiceDetail selectedService = salonServiceDetailRepo.findById(selectedServiceId).orElseThrow();
		log.info("Querying for  service id " + selectedServiceId + " for date " + slotFor);
		List<Slot> results = slotRepo.findBySelectedServiceAndSlotFor(selectedServiceId,slotFor);
		log.info("returning  " + results.size() + " Slots");
	    return results;
	}

	@Override
	public List<Slot> getSlotsForServiceOnDate(long slotServiceId, String formattedDate) {
		SalonServiceDetail salonServiceDetail = salonServiceDetailRepo.findById(slotServiceId).orElseThrow();
		LocalDate localDate = getAsDate(formattedDate);
		LocalDateTime startDate = localDate.atTime(0, 1);
        LocalDateTime endDate = localDate.atTime(23, 59);
        log.info("Querying for  " + slotServiceId + " From " + startDate + " to " + endDate);
        List<Slot> results = slotRepo.findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatus(startDate, endDate, salonServiceDetail, SlotStatus.AVAILABLE);
        log.info("returning  " + results.size() + " Slots");
        return results;
	}
	
	private LocalDate getAsDate(String formattedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(formattedDate, formatter);
    }

	@Override
	public Optional<Slot> findAvailableSlotId(Long slotId) {
		return slotRepo.findByIdAndStatus(slotId, SlotStatus.AVAILABLE);
	}

	@Override
	public Optional<Slot> findLockedSlotId(Long slotId) {
		return slotRepo.findByIdAndStatus(slotId, SlotStatus.LOCKED);
	}

	@Transactional
	@Override
	public void setToConfirmed(Slot slot) {
		slot.setStatus(SlotStatus.CONFIRMED);
        slot.setConfirmedAt(LocalDateTime.now());
        slotRepo.save(slot);		
	}

	@Transactional
	@Override
	public void setToAvailable(Slot slot) {
		slot.setStatus(SlotStatus.AVAILABLE);
        slot.setSelectedService(null);
        slot.setLockedAt(null);
        slotRepo.save(slot);		
	}

	@Transactional
	@Override
	public void setToLockedWithService(Slot slot, SalonServiceDetail serviceDetail) {
		slot.setStatus(SlotStatus.LOCKED);
        slot.setLockedAt(LocalDateTime.now());
        slot.setSelectedService(serviceDetail);
        slotRepo.save(slot);	
	}

}
