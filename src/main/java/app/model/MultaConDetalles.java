package app.model;

import java.math.BigDecimal;
import java.util.Date;

public class MultaConDetalles {
    private Integer id;
    private Integer idPrestamo;
    private Integer idCliente;
    private BigDecimal monto; // Use BigDecimal for DECIMAL(10,2)
    private int diasAtraso;
    private Date fechaGenerada;
    private Date fechaPago;
    private String estadoPago;
    private String justificacionExoneracion;
    private String clienteNombre; // From join with Cliente

    // Constructor (populate from ResultSet in DAO)
    public MultaConDetalles(Integer id, Integer idPrestamo, Integer idCliente, BigDecimal monto, int diasAtraso,
                            Date fechaGenerada, Date fechaPago, String estadoPago, String justificacionExoneracion,
                            String clienteNombre) {
        this.id = id;
        this.idPrestamo = idPrestamo;
        this.idCliente = idCliente;
        this.monto = monto;
        this.diasAtraso = diasAtraso;
        this.fechaGenerada = fechaGenerada;
        this.fechaPago = fechaPago;
        this.estadoPago = estadoPago;
        this.justificacionExoneracion = justificacionExoneracion;
        this.clienteNombre = clienteNombre;
    }

    // Getters para ReportGenerator y otros usos

    public Integer getId() { return id; }
    public Integer getIdPrestamo() { return idPrestamo; }
    public Integer getIdCliente() { return idCliente; }
    public BigDecimal getMonto() { return monto; }
    public int getDiasAtraso() { return diasAtraso; }
    public Date getFechaGenerada() { return fechaGenerada; }
    public Date getFechaPago() { return fechaPago; }
    public String getEstadoPago() { return estadoPago; }
    public String getJustificacionExoneracion() { return justificacionExoneracion; }
    public String getClienteNombre() { return clienteNombre; }

    // Setters (omitiendo por simplicidad, pero se deberían añadir si se requiere lógica de negocio)
}