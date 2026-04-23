package com.carwash.service;

import com.carwash.entity.Cliente;
import com.carwash.entity.Vehiculo;
import com.carwash.repository.ClienteRepository;
import com.carwash.repository.VehiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository, ClienteRepository clienteRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Vehiculo registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getCliente() == null || vehiculo.getCliente().getId() == null) {
            throw new IllegalArgumentException("El vehículo debe estar asociado a un cliente válido");
        }
        Cliente cliente = clienteRepository.findById(vehiculo.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado en el sistema"));
        vehiculo.setCliente(cliente);
        vehiculo.setActivo(true);
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehiculo buscarPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo con id " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorClienteId(Long clienteId) {
        return vehiculoRepository.findByClienteId(clienteId);
    }

    @Transactional
    public Vehiculo actualizarVehiculo(Long id, Vehiculo vehiculo) {
        Vehiculo existente = buscarPorId(id);
        existente.setPlaca(vehiculo.getPlaca());
        existente.setMarca(vehiculo.getMarca());
        existente.setModelo(vehiculo.getModelo());
        existente.setAnio(vehiculo.getAnio());
        existente.setColor(vehiculo.getColor());
        existente.setTipo(vehiculo.getTipo());
        return vehiculoRepository.save(existente);
    }

    @Transactional
    public void desactivarVehiculo(Long id) {
        Vehiculo existente = buscarPorId(id);
        existente.setActivo(false);
        vehiculoRepository.save(existente);
    }
}
