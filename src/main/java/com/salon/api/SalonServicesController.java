package com.salon.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salon.domain.model.SalonServiceDetail;
import com.salon.domain.model.Slot;
import com.salon.domain.service.SalonServiceDetailService;
import com.salon.domain.service.SlotService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = {"http://localhost:3000","http://172.19.160.1:3000"})
public class SalonServicesController {
	
	@Autowired
	SalonServiceDetailService salonServiceDetailService;
	
	@Autowired
	SlotService slotService;
	
	@GetMapping("/retrieveAvailableSalonServices")
	@ApiOperation(value = "RetrieveAvailableSalonServicesAPI") // just for naming purpose in Swagger UI
	public ResponseEntity<List<SalonServiceDetail>> retrieveAvailableSalonServices () {
		return ResponseEntity.ok( salonServiceDetailService.findAllSalonServices());
	}
	
	@GetMapping("/retrieveAvailableSlots/{salonServiceld}/{formattedDate}")
	@ApiOperation(value = "RetrieveAvailableSlotsAPI")
	public ResponseEntity<List<Slot>> retrieveAvailableSlots (@PathVariable long salonServiceld, @PathVariable String formattedDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(formattedDate, formatter);
		return ResponseEntity.ok( slotService.findByAvailSlotForService(salonServiceld, date));
	}

}
