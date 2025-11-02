package app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CajaMovimiento {
    private int id;
    private int idUsuario;
    private String tipo;
    private String concepto;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String referencia;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public CajaMovimiento() {}

    // Constructor para creación (sin ID, fechas manejadas en BD si no se proveen)
    public CajaMovimiento(int idUsuario, String tipo, String concepto, BigDecimal monto, LocalDateTime fecha,
                          String referencia, String observaciones) {
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.concepto = concepto;
        this.monto = monto;
        this.fecha = fecha;
        this.referencia = referencia;
        this.observaciones = observaciones;
    }

    // Constructor completo (para lecturas desde BD)
    public CajaMovimiento(int id, int idUsuario, String tipo, String concepto, BigDecimal monto, LocalDateTime fecha,
                          String referencia, String observaciones, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.concepto = concepto;
        this.monto = monto;
        this.fecha = fecha;
        this.referencia = referencia;
        this.observaciones = observaciones;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CajaMovimiento that = (CajaMovimiento) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}