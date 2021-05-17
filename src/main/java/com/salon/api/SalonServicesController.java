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
import com.salon.domain.service.SalonService;
import com.salon.domain.service.SlotService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000","http://172.19.160.1:3000"})
public class SalonServicesController {
	
	@Autowired
	SalonService salonServiceDetailService;
	
	@Autowired
	SlotService slotService;
	
	@GetMapping("/services/retrieveAvailableSalonServices")
	@ApiOperation(value = "RetrieveAvailableSalonServicesAPI") // just for naming purpose in Swagger UI
	public ResponseEntity<List<SalonServiceDetail>> retrieveAvailableSalonServices () {
		return ResponseEntity.ok( salonServiceDetailService.findAllSalonServices());
	}
	
	@GetMapping("/slots/retrieveAvailableSlots/mine/{salonServiceld}/{formattedDate}")
	@ApiOperation(value = "RetrieveAvailableSlotsAPI")
	public ResponseEntity<List<Slot>> retrieveAvailableSlots (@PathVariable long salonServiceld, @PathVariable String formattedDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(formattedDate, formatter);
		return ResponseEntity.ok( slotService.findByAvailSlotsForService(salonServiceld, date));
	}
	
	@GetMapping("/slots/retrieveAvailableSlots/{salonServiceId}/{formattedDate}")
    @ApiOperation(value = "RetrieveAvailableSlotsAPI")
    public List<Slot> retrieveAvailableSlotsAPI(
    			@PathVariable Long salonServiceId, @ApiParam(value = "Date in yyyy-MM-dd format", required = true, defaultValue = "2020-11-21") @PathVariable String formattedDate) {
        return slotService.getSlotsForServiceOnDate(salonServiceId, formattedDate);
    }

}
