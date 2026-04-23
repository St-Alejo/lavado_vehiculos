package com.carwash.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "recargos_por_tipo_vehiculo")
public class RecargoPorTipoVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipoVehiculo;

    private BigDecimal porcentajeAdicional;
    private BigDecimal montoFijo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_servicio_id")
    @JsonBackReference("servicio-recargo")
    private TipoServicio tipoServicio;

    public RecargoPorTipoVehiculo() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }
    public BigDecimal getPorcentajeAdicional() { return porcentajeAdicional; }
    public void setPorcentajeAdicional(BigDecimal porcentajeAdicional) { this.porcentajeAdicional = porcentajeAdicional; }
    public BigDecimal getMontoFijo() { return montoFijo; }
    public void setMontoFijo(BigDecimal montoFijo) { this.montoFijo = montoFijo; }
    public TipoServicio getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }
}
