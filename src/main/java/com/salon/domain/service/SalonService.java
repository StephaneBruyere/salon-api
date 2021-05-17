package com.salon.domain.service;

import java.util.List;
import java.util.Optional;

import com.salon.domain.model.SalonServiceDetail;

public interface  SalonService  {
	
	public List<SalonServiceDetail> findAllSalonServices();
	
	public SalonServiceDetail findSalonServicesById(long id);
	
	public long countAllSalonServices();

	public Optional<SalonServiceDetail> findById(Long selectedSalonServiceDetailId);

}
