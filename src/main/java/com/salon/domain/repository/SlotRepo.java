package com.salon.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;
import com.salon.domain.model.SlotStatus;

public interface SlotRepo extends CrudRepository<Slot, Long> {
	
	// My solution
	@Query("select s from Slot s JOIN s.availableServices d WHERE d.id=?1 AND s.status=0 AND DATE(s.slotFor)=?2")
	public List<Slot> findBySelectedServiceAndSlotFor(long selectedServiceId, LocalDate slotFor);
	
	public List<Slot> findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatus
						(LocalDateTime startTime, LocalDateTime endTime, SalonServiceDetail serviceDetail, SlotStatus status);

	public Optional<Slot> findByIdAndStatus(Long slotId, SlotStatus available);

}
