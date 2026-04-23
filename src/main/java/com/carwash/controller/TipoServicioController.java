package com.carwash.controller;

import com.carwash.entity.RecargoPorTipoVehiculo;
import com.carwash.entity.TipoServicio;
import com.carwash.service.TipoServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-servicio")
public class TipoServicioController {

    private final TipoServicioService tipoServicioService;

    public TipoServicioController(TipoServicioService tipoServicioService) {
        this.tipoServicioService = tipoServicioService;
    }

    @PostMapping("/")
    public ResponseEntity<TipoServicio> crearTipoServicio(@RequestBody TipoServicio tipoServicio) {
        return new ResponseEntity<>(tipoServicioService.crearTipoServicio(tipoServicio), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<TipoServicio>> listarTodosActivos() {
        return ResponseEntity.ok(tipoServicioService.listarTodosActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServicio> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoServicioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoServicio> actualizarTipoServicio(@PathVariable Long id, @RequestBody TipoServicio tipoServicio) {
        return ResponseEntity.ok(tipoServicioService.actualizarTipoServicio(id, tipoServicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarTipoServicio(@PathVariable Long id) {
        tipoServicioService.desactivarTipoServicio(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/recargosPorTipo")
    public ResponseEntity<TipoServicio> agregarRecargo(@PathVariable Long id, @RequestBody RecargoPorTipoVehiculo recargo) {
        return ResponseEntity.ok(tipoServicioService.agregarRecargo(id, recargo));
    }
}
