package app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class Multa {
    private Integer id;
    private Integer idPrestamo;
    private Integer idCliente;
    private BigDecimal monto;
    private Integer diasAtraso;
    private Date fechaGenerada;
    private Date fechaPago;
    private String estadoPago;
    private String justificacionExoneracion;
    private Date createdAt;
    private Date updatedAt;

    // Constructor vacío
    public Multa() {}

    // Constructor para INSERCIÓN (sin ID ni fechas/campos de auditoría automáticos)
    public Multa(Integer idPrestamo, Integer idCliente, BigDecimal monto, Integer diasAtraso, String estadoPago, String justificacionExoneracion) {
        this(null, idPrestamo, idCliente, monto, diasAtraso, null, null, estadoPago, justificacionExoneracion, null, null);
    }

    // Constructor COMPLETO (para recuperación de DB)
    public Multa(Integer id, Integer idPrestamo, Integer idCliente, BigDecimal monto, Integer diasAtraso, Date fechaGenerada, Date fechaPago, String estadoPago, String justificacionExoneracion, Date createdAt, Date updatedAt) {
        this.id = id;
        this.idPrestamo = idPrestamo;
        this.idCliente = idCliente;
        this.monto = monto;
        this.diasAtraso = diasAtraso;
        this.fechaGenerada = (fechaGenerada == null) ? new Date() : fechaGenerada;
        this.fechaPago = fechaPago;
        this.estadoPago = estadoPago;
        this.justificacionExoneracion = justificacionExoneracion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(Integer idPrestamo) { this.idPrestamo = idPrestamo; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public Integer getDiasAtraso() { return diasAtraso; }
    public void setDiasAtraso(Integer diasAtraso) { this.diasAtraso = diasAtraso; }

    public Date getFechaGenerada() { return fechaGenerada; }
    public void setFechaGenerada(Date fechaGenerada) { this.fechaGenerada = fechaGenerada; }

    public Date getFechaPago() { return fechaPago; }
    public void setFechaPago(Date fechaPago) { this.fechaPago = fechaPago; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public String getJustificacionExoneracion() { return justificacionExoneracion; }
    public void setJustificacionExoneracion(String justificacionExoneracion) { this.justificacionExoneracion = justificacionExoneracion; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public void setFechaPago(LocalDateTime now) {
    }
}