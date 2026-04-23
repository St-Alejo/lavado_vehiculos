package com.carwash.controller;

import com.carwash.entity.Pago;
import com.carwash.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping("/orden/{ordenId}")
    public ResponseEntity<Pago> registrarPago(@PathVariable Long ordenId, @RequestBody Pago pago) {
        return new ResponseEntity<>(pagoService.registrarPago(ordenId, pago), HttpStatus.CREATED);
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<Pago>> listarPorOrden(@PathVariable Long ordenId) {
        return ResponseEntity.ok(pagoService.listarPorOrden(ordenId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @PutMapping("/{id}/reembolsar")
    public ResponseEntity<Pago> reembolsarPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.reembolsarPago(id));
    }
}
