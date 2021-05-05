package com.salon.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.repository.SalonServiceDetailRepo;

@Service
public class SalonServiceDetailServiceImpl implements SalonServiceDetailService{
	
	@Autowired
	SalonServiceDetailRepo salonServiceDetailRepo;	

	@Override
	public List<SalonServiceDetail> findAllSalonServices() {
		return (List<SalonServiceDetail>)salonServiceDetailRepo.findAll();
	}

	@Override
	public long countAllSalonServices() {
		return salonServiceDetailRepo.count();
	}

	@Override
	public SalonServiceDetail findSalonServicesById(long id) {
		return salonServiceDetailRepo.findById(id).orElse(new SalonServiceDetail());
	}

}
