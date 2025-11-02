package app.model;

import java.time.LocalDateTime;

public class Cliente {
    private int id;
    private String nombre;
    private String nit;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime fechaRegistro;
    private int estado;
    private String historial;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Cliente() {}

    // Constructor para creación (sin ID, fechas se manejan en BD)
    public Cliente(String nombre, String nit, String telefono, String email, String direccion, int estado, String historial) {
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = estado;
        this.historial = historial;
    }

    // Constructor completo (para lecturas desde BD)
    public Cliente(int id, String nombre, String nit, String telefono, String email, String direccion,
                   LocalDateTime fechaRegistro, int estado, String historial, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.historial = historial;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public String getHistorial() { return historial; }
    public void setHistorial(String historial) { this.historial = historial; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}