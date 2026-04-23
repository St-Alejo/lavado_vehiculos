package com.carwash.service;

import com.carwash.entity.RecargoPorTipoVehiculo;
import com.carwash.entity.TipoServicio;
import com.carwash.repository.TipoServicioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoServicioService {

    private final TipoServicioRepository tipoServicioRepository;

    public TipoServicioService(TipoServicioRepository tipoServicioRepository) {
        this.tipoServicioRepository = tipoServicioRepository;
    }

    @Transactional
    public TipoServicio crearTipoServicio(TipoServicio tipoServicio) {
        tipoServicio.setActivo(true);
        if (tipoServicio.getRecargosPorTipo() != null) {
            tipoServicio.getRecargosPorTipo().forEach(recargo -> recargo.setTipoServicio(tipoServicio));
        }
        return tipoServicioRepository.save(tipoServicio);
    }

    @Transactional(readOnly = true)
    public List<TipoServicio> listarTodosActivos() {
        return tipoServicioRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public TipoServicio buscarPorId(Long id) {
        return tipoServicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TipoServicio con id " + id + " no encontrado"));
    }

    @Transactional
    public TipoServicio actualizarTipoServicio(Long id, TipoServicio tipoServicio) {
        TipoServicio existente = buscarPorId(id);
        existente.setNombre(tipoServicio.getNombre());
        existente.setDescripcion(tipoServicio.getDescripcion());
        existente.setPrecioBase(tipoServicio.getPrecioBase());
        existente.setAplicaRecargo(tipoServicio.isAplicaRecargo());
        return tipoServicioRepository.save(existente);
    }

    @Transactional
    public void desactivarTipoServicio(Long id) {
        TipoServicio existente = buscarPorId(id);
        existente.setActivo(false);
        tipoServicioRepository.save(existente);
    }

    @Transactional
    public TipoServicio agregarRecargo(Long id, RecargoPorTipoVehiculo recargo) {
        TipoServicio existente = buscarPorId(id);
        recargo.setTipoServicio(existente);
        existente.getRecargosPorTipo().add(recargo);
        return tipoServicioRepository.save(existente);
    }
}
