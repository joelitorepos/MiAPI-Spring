package app.model;

import java.util.Date;

public class PrestamoConDetalles {
    private Prestamo prestamo; // Wraps the base Prestamo
    private String clienteNombre;
    private String libroNombre;

    // Constructor (populate from ResultSet in DAO)
    public PrestamoConDetalles(Prestamo prestamo, String clienteNombre, String libroNombre) {
        this.prestamo = prestamo;
        this.clienteNombre = clienteNombre;
        this.libroNombre = libroNombre;
    }

    // Getters (delegate to prestamo for base fields)
    public Integer getId() { return prestamo.getId(); }
    public String getClienteNombre() { return clienteNombre; }
    public String getLibroNombre() { return libroNombre; }

    // Delegación de Getters necesarios para ReportGenerator:
    public Date getFechaPrestamo() { return prestamo.getFechaPrestamo(); }
    public String getEstado() { return prestamo.getEstado(); }
    public Date getFechaVencimiento() { return prestamo.getFechaVencimiento(); }

    // Otros getters base (puedes añadir más según necesites del objeto Prestamo)
    public Prestamo getPrestamo() { return prestamo; }

    // Setters if needed (rare for view models)
}