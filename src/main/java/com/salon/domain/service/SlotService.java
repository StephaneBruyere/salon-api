package com.salon.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.salon.domain.model.Slot;

public interface SlotService {
	
	List<Slot> findByAvailSlotForService(long selectedServiceId, LocalDate slotFor);

}
