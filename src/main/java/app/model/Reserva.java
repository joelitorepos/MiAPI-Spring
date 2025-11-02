package app.model;

import java.time.LocalDateTime;

public class Reserva {
    private int id;
    private int idCliente;
    private int idLibro;
    private Integer idCopia; // Opcional, puede ser null
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaVencimiento;
    private String estado;
    private int posicionCola;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Reserva() {}

    // Constructor para creación (sin ID, fechas como reserva y vencimiento se setean manualmente, created/updated en BD)
    public Reserva(int idCliente, int idLibro, Integer idCopia, LocalDateTime fechaVencimiento, String estado, int posicionCola, String observaciones) {
        this.idCliente = idCliente;
        this.idLibro = idLibro;
        this.idCopia = idCopia;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.posicionCola = posicionCola;
        this.observaciones = observaciones;
    }

    // Constructor completo (para lecturas desde BD)
    public Reserva(int id, int idCliente, int idLibro, Integer idCopia, LocalDateTime fechaReserva, LocalDateTime fechaVencimiento,
                   String estado, int posicionCola, String observaciones, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.idCliente = idCliente;
        this.idLibro = idLibro;
        this.idCopia = idCopia;
        this.fechaReserva = fechaReserva;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.posicionCola = posicionCola;
        this.observaciones = observaciones;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }
    public Integer getIdCopia() { return idCopia; }
    public void setIdCopia(Integer idCopia) { this.idCopia = idCopia; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDateTime fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getPosicionCola() { return posicionCola; }
    public void setPosicionCola(int posicionCola) { this.posicionCola = posicionCola; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}