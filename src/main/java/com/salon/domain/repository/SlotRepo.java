package com.salon.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.salon.domain.model.Slot;

public interface SlotRepo extends CrudRepository<Slot, Long> {
	
	@Query("select s from Slot s JOIN s.availableServices d WHERE d.id=?1 AND s.status=0 AND DATE(s.slotFor)=?2")
	List<Slot> findBySelectedServiceAndSlotFor(long selectedServiceId, LocalDate slotFor);
	
	List<Slot> findBySlotFor(LocalDateTime slotFor);

}
