package app.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa un registro de auditoría de una operación sensible en el sistema.
 * Es un modelo de solo lectura (generalmente, la inserción la maneja un trigger
 * o una capa de servicio específica sin CRUD directo).
 */
public class Auditoria {
    private Integer id;
    private Integer idUsuario;
    private LocalDateTime fechaHora; // Para la fecha y hora del evento
    private String modulo;
    private String accion;
    private String detalle; // JSON o texto con los detalles (antes/después)

    // Constructor vacío
    public Auditoria() {}

    /**
     * Constructor para la creación (generalmente usado internamente o por un trigger).
     * @param idUsuario ID del usuario que realizó la acción.
     * @param fechaHora Fecha y hora del evento.
     * @param modulo Módulo del sistema afectado (ej: 'Prestamo', 'Multa').
     * @param accion Acción realizada (ej: 'Create', 'Update', 'Delete').
     * @param detalle JSON o texto con la información detallada del cambio.
     */
    public Auditoria(Integer idUsuario, LocalDateTime fechaHora, String modulo, String accion, String detalle) {
        this.idUsuario = idUsuario;
        this.fechaHora = fechaHora;
        this.modulo = modulo;
        this.accion = accion;
        this.detalle = detalle;
    }

    /**
     * Constructor completo (para lecturas desde BD).
     */
    public Auditoria(Integer id, Integer idUsuario, LocalDateTime fechaHora, String modulo, String accion, String detalle) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fechaHora = fechaHora;
        this.modulo = modulo;
        this.accion = accion;
        this.detalle = detalle;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auditoria that = (Auditoria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Auditoria{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", fechaHora=" + fechaHora +
                ", modulo='" + modulo + '\'' +
                ", accion='" + accion + '\'' +
                '}';
    }
}