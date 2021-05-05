package com.salon.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.salon.domain.model.SalonServiceDetail;

@Repository
public interface SalonServiceDetailRepo extends CrudRepository<SalonServiceDetail, Long> {

}
