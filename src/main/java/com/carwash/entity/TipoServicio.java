package com.carwash.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipos_servicio")
public class TipoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private boolean aplicaRecargo;
    private boolean activo;

    @OneToMany(mappedBy = "tipoServicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("servicio-recargo")
    private List<RecargoPorTipoVehiculo> recargosPorTipo = new ArrayList<>();

    public TipoServicio() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecioBase() { return precioBase; }
    public void setPrecioBase(BigDecimal precioBase) { this.precioBase = precioBase; }
    public boolean isAplicaRecargo() { return aplicaRecargo; }
    public void setAplicaRecargo(boolean aplicaRecargo) { this.aplicaRecargo = aplicaRecargo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public List<RecargoPorTipoVehiculo> getRecargosPorTipo() { return recargosPorTipo; }
    public void setRecargosPorTipo(List<RecargoPorTipoVehiculo> recargosPorTipo) { this.recargosPorTipo = recargosPorTipo; }
}
