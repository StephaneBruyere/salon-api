package com.salon.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.salon.domain.model.Slot;

public interface SlotRepo extends CrudRepository<Slot, Long> {

}
