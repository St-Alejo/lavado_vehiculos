package com.carwash.repository;

import com.carwash.entity.RecargoPorTipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecargoPorTipoVehiculoRepository extends JpaRepository<RecargoPorTipoVehiculo, Long> {
}
