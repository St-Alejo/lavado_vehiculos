package com.carwash.controller;

import com.carwash.entity.Vehiculo;
import com.carwash.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping("/")
    public ResponseEntity<Vehiculo> registrarVehiculo(@RequestBody Vehiculo vehiculo) {
        return new ResponseEntity<>(vehiculoService.registrarVehiculo(vehiculo), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Vehiculo>> listarTodos() {
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Vehiculo>> buscarPorClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vehiculoService.buscarPorClienteId(clienteId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.actualizarVehiculo(id, vehiculo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarVehiculo(@PathVariable Long id) {
        vehiculoService.desactivarVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}
