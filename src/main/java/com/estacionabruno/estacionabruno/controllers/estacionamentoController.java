package com.estacionabruno.estacionabruno.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estacionabruno.estacionabruno.dtos.EstacionamentoDto;
import com.estacionabruno.estacionabruno.models.EstacionamentoModel;
import com.estacionabruno.estacionabruno.service.EstacionamentoService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/estacionamento")
public class estacionamentoController {

//	@Autowired
//	EstacionamentoService estacionamentoService;

	final EstacionamentoService estacionamentoService;

	// injeção de dependência pelo construtor
	public estacionamentoController(EstacionamentoService estacionamentoService) {
		this.estacionamentoService = estacionamentoService;
	}

	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid EstacionamentoDto estacionamentoDto) {
		
		if (estacionamentoService.existsByLicensePlateCar(estacionamentoDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
		}
		if (estacionamentoService.existsByParkingSpotNumber(estacionamentoDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		if (estacionamentoService.existsByApartmentAndBlock(estacionamentoDto.getApartment(),
				estacionamentoDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Conflict: Parking Spot already registered for this apartment/block!");
		}
		//var uma forma diferente
		var estacionamentoModel = new EstacionamentoModel();
		//maneira de tranformar o dto num model
		BeanUtils.copyProperties(estacionamentoDto, estacionamentoModel);
		estacionamentoModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(estacionamentoService.save(estacionamentoModel));
	}
	
	 @GetMapping
	    public ResponseEntity<Page<EstacionamentoModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
	        return ResponseEntity.status(HttpStatus.OK).body(estacionamentoService.findAll(pageable));
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
	        Optional<EstacionamentoModel> estacionamentoModelOptional = estacionamentoService.findById(id);
	        if (!estacionamentoModelOptional.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
	        }
	        return ResponseEntity.status(HttpStatus.OK).body(estacionamentoModelOptional.get());
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
	        Optional<EstacionamentoModel> estacionamentoModelOptional = estacionamentoService.findById(id);
	        if (!estacionamentoModelOptional.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
	        }
	        estacionamentoService.delete(estacionamentoModelOptional.get());
	        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
	                                                    @RequestBody @Valid EstacionamentoDto estacionamentoDto){
	        Optional<EstacionamentoModel> estacionamentoModelOptional = estacionamentoService.findById(id);
	        if (!estacionamentoModelOptional.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
	        }
	        var estacionamentoModel = new EstacionamentoModel();
	      //maneira de tranformar o dto num model
	        BeanUtils.copyProperties(estacionamentoDto, estacionamentoModel);
	        estacionamentoModel.setId(estacionamentoModelOptional.get().getId());
	        estacionamentoModel.setRegistrationDate(estacionamentoModelOptional.get().getRegistrationDate());
	        return ResponseEntity.status(HttpStatus.OK).body(estacionamentoService.save(estacionamentoModel));
	    }

}
