package com.carwash.repository;

import com.carwash.entity.EstadoOrden;
import com.carwash.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByClienteId(Long clienteId);
    List<Orden> findByEstado(EstadoOrden estado);
}
