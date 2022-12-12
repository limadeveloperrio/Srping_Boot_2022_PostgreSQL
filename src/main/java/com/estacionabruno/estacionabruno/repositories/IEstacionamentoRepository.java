package com.estacionabruno.estacionabruno.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estacionabruno.estacionabruno.models.EstacionamentoModel;

@Repository
public interface IEstacionamentoRepository extends JpaRepository<EstacionamentoModel, UUID>{

	boolean existsByLicensePlateCar(String licensePlateCar);

	boolean existsByParkingSpotNumber(String parkingSpotNumber);

	boolean existsByApartmentAndBlock(String apartment, String block);

}
