package com.salon.domain.service;

import java.util.List;

import com.salon.domain.model.SalonServiceDetail;

public interface  SalonServiceDetailService  {
	
	public List<SalonServiceDetail> findAllSalonServices();
	
	public SalonServiceDetail findSalonServicesById(long id);
	
	public long countAllSalonServices();

}
