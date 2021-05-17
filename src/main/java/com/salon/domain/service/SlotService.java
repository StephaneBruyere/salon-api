package com.salon.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;

public interface SlotService {
	
	//mine
	public List<Slot> findByAvailSlotsForService(long selectedServiceId, LocalDate slotFor);
	
	//proposed impl.
	public List<Slot> getSlotsForServiceOnDate(long slotServiceId, String formattedDate);

	public Optional<Slot> findAvailableSlotId(Long slotId);
	
	public Optional<Slot> findLockedSlotId(Long slotId);
	
	public void setToConfirmed(Slot slot);

    public void setToAvailable(Slot slot);

    public void setToLockedWithService(Slot slot, SalonServiceDetail serviceDetail);

}
