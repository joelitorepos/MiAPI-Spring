package app.model;

import java.time.LocalDateTime;

public class Categoria {
    private int id;
    private String nombre;
    private int estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor para creación (sin ID, createdAt y updatedAt se manejan en BD)
    public Categoria(Integer selectedId, String nombre, int estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    // Constructor completo (para lecturas desde BD)
    public Categoria(int id, String nombre, int estado, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}