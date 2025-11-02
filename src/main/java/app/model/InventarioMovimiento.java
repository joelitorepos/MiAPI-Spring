package app.model;

import java.time.LocalDateTime;

public class InventarioMovimiento {
    private int id;
    private int idUsuario;
    private int idCopia;
    private int idLibro;
    private String tipoMovimiento;
    private LocalDateTime fecha;
    private Integer diferencia;
    private String ubicacionAnterior;
    private String ubicacionNueva;
    private String comentarios;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public InventarioMovimiento() {}

    // Constructor para creación (sin ID, fechas manejadas en BD si no se proveen)
    public InventarioMovimiento(int idUsuario, int idCopia, int idLibro, String tipoMovimiento, LocalDateTime fecha,
                                Integer diferencia, String ubicacionAnterior, String ubicacionNueva, String comentarios) {
        this.idUsuario = idUsuario;
        this.idCopia = idCopia;
        this.idLibro = idLibro;
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.diferencia = diferencia;
        this.ubicacionAnterior = ubicacionAnterior;
        this.ubicacionNueva = ubicacionNueva;
        this.comentarios = comentarios;
    }

    // Constructor completo (para lecturas desde BD)
    public InventarioMovimiento(int id, int idUsuario, int idCopia, int idLibro, String tipoMovimiento, LocalDateTime fecha,
                                Integer diferencia, String ubicacionAnterior, String ubicacionNueva, String comentarios,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idCopia = idCopia;
        this.idLibro = idLibro;
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.diferencia = diferencia;
        this.ubicacionAnterior = ubicacionAnterior;
        this.ubicacionNueva = ubicacionNueva;
        this.comentarios = comentarios;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdCopia() { return idCopia; }
    public void setIdCopia(int idCopia) { this.idCopia = idCopia; }
    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Integer getDiferencia() { return diferencia; }
    public void setDiferencia(Integer diferencia) { this.diferencia = diferencia; }
    public String getUbicacionAnterior() { return ubicacionAnterior; }
    public void setUbicacionAnterior(String ubicacionAnterior) { this.ubicacionAnterior = ubicacionAnterior; }
    public String getUbicacionNueva() { return ubicacionNueva; }
    public void setUbicacionNueva(String ubicacionNueva) { this.ubicacionNueva = ubicacionNueva; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}