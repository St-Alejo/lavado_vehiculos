package com.carwash.controller;

import com.carwash.entity.Cliente;
import com.carwash.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.registrarCliente(cliente), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCliente(@PathVariable Long id) {
        clienteService.desactivarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
