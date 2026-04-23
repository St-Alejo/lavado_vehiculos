package com.carwash.controller;

import com.carwash.entity.DetalleOrden;
import com.carwash.entity.EstadoOrden;
import com.carwash.entity.Orden;
import com.carwash.service.OrdenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @PostMapping("/")
    public ResponseEntity<Orden> crearOrden(@RequestBody Orden orden) {
        return new ResponseEntity<>(ordenService.crearOrden(orden), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Orden>> listarTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Orden>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ordenService.buscarPorCliente(clienteId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Orden>> buscarPorEstado(@PathVariable EstadoOrden estado) {
        return ResponseEntity.ok(ordenService.buscarPorEstado(estado));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Orden> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> request) {
        EstadoOrden nuevoEstado = EstadoOrden.valueOf(request.get("estado"));
        return ResponseEntity.ok(ordenService.cambiarEstado(id, nuevoEstado));
    }

    @PostMapping("/{id}/detalles")
    public ResponseEntity<Orden> agregarDetalle(@PathVariable Long id, @RequestBody DetalleOrden detalle) {
        return ResponseEntity.ok(ordenService.agregarDetalle(id, detalle));
    }

    @DeleteMapping("/{id}/detalles/{detalleId}")
    public ResponseEntity<Orden> quitarDetalle(@PathVariable Long id, @PathVariable Long detalleId) {
        return ResponseEntity.ok(ordenService.quitarDetalle(id, detalleId));
    }
}
