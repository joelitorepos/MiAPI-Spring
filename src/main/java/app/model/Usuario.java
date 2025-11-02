package app.model;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String username;
    private String nombre;
    private String password; // Mantén, pero no expongas en getters si no es necesario (puedes agregar si se requiere)
    private int idRol;
    private int estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Usuario() {}

    // Constructor para nuevo usuario (sin ID, con password plain para hashear en DAO, fechas en BD)
    public Usuario(String username, String nombre, String password, int idRol, int estado) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.idRol = idRol;
        this.estado = estado;
    }

    // Constructor completo (con ID y password hasheado, para lecturas desde BD)
    public Usuario(int id, String username, String nombre, String password, int idRol, int estado,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.idRol = idRol;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor para actualización (sin password, fechas en BD)
    public Usuario(int id, String username, String nombre, int idRol, int estado) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.idRol = idRol;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, username='%s', nombre='%s', idRol=%d, estado=%d, createdAt=%s, updatedAt=%s}",
                id, username, nombre, idRol, estado, createdAt, updatedAt);
    }
}