package com.salon.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.service.SalonServiceDetailService;

@RestController
public class SalonServicesController {
	
	@Autowired
	SalonServiceDetailService salonServiceDetailService;
	
	@GetMapping("/api/services/retrieveAvailableSalonServices")
	public ResponseEntity<List<SalonServiceDetail>> retrieveAvailableSalonServices () {
		return ResponseEntity.ok( salonServiceDetailService.findAllSalonServices());
	}

}
