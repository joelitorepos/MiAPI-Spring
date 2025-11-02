package app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClienteConMultas {
    private Cliente cliente; // Wraps base Cliente
    private int conteoMultasPendientes; // Calculated COUNT
    private BigDecimal totalAdeudado; // Calculated SUM(monto)

    // Constructor (populate from ResultSet in DAO)
    public ClienteConMultas(Cliente cliente, int conteoMultasPendientes, BigDecimal totalAdeudado) {
        this.cliente = cliente;
        this.conteoMultasPendientes = conteoMultasPendientes;
        this.totalAdeudado = totalAdeudado;
    }

    // Getters (delegate to cliente for base fields)
    public Integer getId() { return cliente.getId(); }
    public String getNombre() { return cliente.getNombre(); }
    // ... other base getters
    public int getConteoMultasPendientes() { return conteoMultasPendientes; }
    public BigDecimal getTotalAdeudado() { return totalAdeudado; }
}