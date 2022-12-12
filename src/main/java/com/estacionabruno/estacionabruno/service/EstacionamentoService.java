package com.estacionabruno.estacionabruno.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.estacionabruno.estacionabruno.models.EstacionamentoModel;
import com.estacionabruno.estacionabruno.repositories.IEstacionamentoRepository;

import jakarta.transaction.Transactional;

@Service
public class EstacionamentoService {

	//@Autowired
	//IEstacionamentoRepository estacionamentoRepository;
	
	final IEstacionamentoRepository estacionamentoRepository;
	
	//injeção de dependência pelo construtor
	public EstacionamentoService(IEstacionamentoRepository estacionamentoRepository) {
		this.estacionamentoRepository = estacionamentoRepository;
		
	}
	//garante transação completa 
	 @Transactional
	    public EstacionamentoModel save(EstacionamentoModel model) {
	        return estacionamentoRepository.save(model);
	    }

	    public boolean existsByLicensePlateCar(String licensePlateCar) {
	        return estacionamentoRepository.existsByLicensePlateCar(licensePlateCar);
	    }

	    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
	        return estacionamentoRepository.existsByParkingSpotNumber(parkingSpotNumber);
	    }

	    public boolean existsByApartmentAndBlock(String apartment, String block) {
	        return estacionamentoRepository.existsByApartmentAndBlock(apartment, block);
	    }

	    public Page<EstacionamentoModel> findAll(Pageable pageable) {
	        return estacionamentoRepository.findAll(pageable);
	    }

	    public Optional<EstacionamentoModel> findById(UUID id) {
	        return estacionamentoRepository.findById(id);
	    }

	    @Transactional
	    public void delete(EstacionamentoModel parkingSpotModel) {
	        estacionamentoRepository.delete(parkingSpotModel);
	    }
	
	
}
