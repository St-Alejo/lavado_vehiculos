package com.carwash.service;

import com.carwash.entity.EstadoOrden;
import com.carwash.entity.EstadoPago;
import com.carwash.entity.Orden;
import com.carwash.entity.Pago;
import com.carwash.repository.OrdenRepository;
import com.carwash.repository.PagoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final OrdenRepository ordenRepository;

    public PagoService(PagoRepository pagoRepository, OrdenRepository ordenRepository) {
        this.pagoRepository = pagoRepository;
        this.ordenRepository = ordenRepository;
    }

    @Transactional
    public Pago registrarPago(Long ordenId, Pago pago) {
        if (pago.getMonto() == null || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("No se puede registrar un pago con monto <= 0");
        }
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));

        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new IllegalStateException("No se puede registrar un pago si la orden está cancelada");
        }

        pago.setOrden(orden);
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(EstadoPago.PAGADO);

        return pagoRepository.save(pago);
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPorOrden(Long ordenId) {
        return pagoRepository.findByOrdenId(ordenId);
    }

    @Transactional(readOnly = true)
    public Pago buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago con id " + id + " no encontrado"));
    }

    @Transactional
    public Pago reembolsarPago(Long id) {
        Pago pago = buscarPorId(id);
        if (pago.getEstado() == EstadoPago.REEMBOLSADO) {
            throw new IllegalStateException("El pago ya se encuentra reembolsado");
        }
        pago.setEstado(EstadoPago.REEMBOLSADO);
        return pagoRepository.save(pago);
    }
}
