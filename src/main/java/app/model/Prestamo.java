package app.model;

import java.util.Date;

public class Prestamo {
    private Integer id;
    private Integer idCliente;
    private Integer idCopia; // Corregido: Es la copia, no el libro.
    private Integer idUsuario; // Usuario que procesa el préstamo (bibliotecario).
    private Date fechaPrestamo;
    private Date fechaVencimiento;
    private Date fechaDevolucion; // Puede ser null en la DB.
    private String estado; // Tipo String para mapear 'Activo', 'Devuelto', etc.
    private String observaciones;
    private Date createdAt;
    private Date updatedAt;

    // Constructor vacío
    public Prestamo() {}

    // Constructor para INSERCIÓN (sin ID ni fechas/campos de auditoría)
    public Prestamo(Integer idCliente, Integer idCopia, Integer idUsuario, Date fechaVencimiento, String estado, String observaciones) {
        this(null, idCliente, idCopia, idUsuario, null, fechaVencimiento, null, estado, observaciones, null, null);
    }

    // Constructor COMPLETO (para recuperación de DB)
    public Prestamo(Integer id, Integer idCliente, Integer idCopia, Integer idUsuario, Date fechaPrestamo, Date fechaVencimiento, Date fechaDevolucion, String estado, String observaciones, Date createdAt, Date updatedAt) {
        this.id = id;
        this.idCliente = idCliente;
        this.idCopia = idCopia;
        this.idUsuario = idUsuario;
        this.fechaPrestamo = (fechaPrestamo == null) ? new Date() : fechaPrestamo; // Default a la fecha actual si es nulo al crear
        this.fechaVencimiento = fechaVencimiento;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public Integer getIdCopia() { return idCopia; }
    public void setIdCopia(Integer idCopia) { this.idCopia = idCopia; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}