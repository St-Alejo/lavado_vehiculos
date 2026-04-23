package com.carwash.service;

import com.carwash.entity.*;
import com.carwash.repository.DetalleOrdenRepository;
import com.carwash.repository.OrdenRepository;
import com.carwash.repository.TipoServicioRepository;
import com.carwash.repository.VehiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TipoServicioRepository tipoServicioRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;

    public OrdenService(OrdenRepository ordenRepository, VehiculoRepository vehiculoRepository,
                        TipoServicioRepository tipoServicioRepository, DetalleOrdenRepository detalleOrdenRepository) {
        this.ordenRepository = ordenRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.tipoServicioRepository = tipoServicioRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
    }

    @Transactional
    public Orden crearOrden(Orden orden) {
        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear una Orden sin al menos un DetalleOrden");
        }
        if (orden.getVehiculo() == null || orden.getVehiculo().getId() == null) {
            throw new IllegalArgumentException("El vehículo es requerido para crear una orden");
        }
        Vehiculo vehiculo = vehiculoRepository.findById(orden.getVehiculo().getId())
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado en el sistema"));

        orden.setVehiculo(vehiculo);
        orden.setCliente(vehiculo.getCliente());
        orden.setEstado(EstadoOrden.REGISTRADA);
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setFechaActualizacion(LocalDateTime.now());

        for (DetalleOrden detalle : orden.getDetalles()) {
            configurarDetalle(detalle, orden, vehiculo.getTipo());
        }

        orden.recalcularTotal();
        Orden guardada = ordenRepository.save(orden);

        LocalDateTime now = guardada.getFechaCreacion();
        String numero = String.format("ORD-%04d%02d%02d-%04d",
                now.getYear(), now.getMonthValue(), now.getDayOfMonth(), guardada.getId());
        guardada.setNumero(numero);

        return ordenRepository.save(guardada);
    }

    @Transactional(readOnly = true)
    public List<Orden> listarTodas() {
        return ordenRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Orden buscarPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden con id " + id + " no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Orden> buscarPorCliente(Long clienteId) {
        return ordenRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Orden> buscarPorEstado(EstadoOrden estado) {
        return ordenRepository.findByEstado(estado);
    }

    @Transactional
    public Orden cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        Orden orden = buscarPorId(id);
        EstadoOrden actual = orden.getEstado();

        boolean valido = (actual == EstadoOrden.REGISTRADA && (nuevoEstado == EstadoOrden.EN_PROCESO || nuevoEstado == EstadoOrden.CANCELADA))
                || (actual == EstadoOrden.EN_PROCESO && (nuevoEstado == EstadoOrden.FINALIZADA || nuevoEstado == EstadoOrden.CANCELADA))
                || (actual == EstadoOrden.FINALIZADA && nuevoEstado == EstadoOrden.ENTREGADA);

        if (!valido) {
            throw new IllegalStateException("Transición de estado inválida: " + actual + " -> " + nuevoEstado);
        }

        orden.setEstado(nuevoEstado);
        orden.setFechaActualizacion(LocalDateTime.now());
        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden agregarDetalle(Long id, DetalleOrden detalle) {
        Orden orden = buscarPorId(id);
        if (orden.getEstado() == EstadoOrden.CANCELADA || orden.getEstado() == EstadoOrden.ENTREGADA) {
            throw new IllegalStateException("No se pueden agregar detalles a una orden en estado " + orden.getEstado());
        }
        configurarDetalle(detalle, orden, orden.getVehiculo().getTipo());
        orden.getDetalles().add(detalle);
        orden.recalcularTotal();
        orden.setFechaActualizacion(LocalDateTime.now());
        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden quitarDetalle(Long id, Long detalleId) {
        Orden orden = buscarPorId(id);
        if (orden.getEstado() == EstadoOrden.CANCELADA || orden.getEstado() == EstadoOrden.ENTREGADA) {
            throw new IllegalStateException("No se pueden quitar detalles a una orden en estado " + orden.getEstado());
        }
        if (orden.getDetalles().size() <= 1) {
            throw new IllegalArgumentException("No se puede quitar el detalle, la orden debe tener al menos un detalle");
        }

        DetalleOrden aRemover = orden.getDetalles().stream()
                .filter(d -> d.getId().equals(detalleId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Detalle no encontrado en la orden"));

        orden.getDetalles().remove(aRemover);
        detalleOrdenRepository.delete(aRemover);

        orden.recalcularTotal();
        orden.setFechaActualizacion(LocalDateTime.now());
        return ordenRepository.save(orden);
    }

    private void configurarDetalle(DetalleOrden detalle, Orden orden, TipoVehiculo tipoVehiculo) {
        TipoServicio ts = tipoServicioRepository.findById(detalle.getTipoServicio().getId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de servicio no encontrado"));

        detalle.setTipoServicio(ts);
        detalle.setOrden(orden);

        BigDecimal precio = ts.getPrecioBase();

        if (ts.isAplicaRecargo()) {
            for (RecargoPorTipoVehiculo recargo : ts.getRecargosPorTipo()) {
                if (recargo.getTipoVehiculo() == tipoVehiculo) {
                    if (recargo.getMontoFijo() != null && recargo.getMontoFijo().compareTo(BigDecimal.ZERO) > 0) {
                        precio = precio.add(recargo.getMontoFijo());
                    } else if (recargo.getPorcentajeAdicional() != null && recargo.getPorcentajeAdicional().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal adicional = precio.multiply(recargo.getPorcentajeAdicional()).divide(BigDecimal.valueOf(100));
                        precio = precio.add(adicional);
                    }
                    break;
                }
            }
        }
        detalle.setPrecioUnitario(precio);
        detalle.calcularSubtotal();
    }
}
