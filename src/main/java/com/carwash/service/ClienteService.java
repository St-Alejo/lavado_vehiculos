package com.carwash.service;

import com.carwash.entity.Cliente;
import com.carwash.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente registrarCliente(Cliente cliente) {
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setActivo(true);
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + id + " no encontrado"));
    }

    @Transactional
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente existente = buscarPorId(id);
        existente.setNombre(cliente.getNombre());
        existente.setApellido(cliente.getApellido());
        existente.setTelefono(cliente.getTelefono());
        existente.setCorreo(cliente.getCorreo());
        existente.setDireccion(cliente.getDireccion());
        return clienteRepository.save(existente);
    }

    @Transactional
    public void desactivarCliente(Long id) {
        Cliente existente = buscarPorId(id);
        existente.setActivo(false);
        clienteRepository.save(existente);
    }
}
