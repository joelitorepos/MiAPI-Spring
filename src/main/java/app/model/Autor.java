package app.model;

import java.util.Date; // Necesario para los campos DATETIME de la base de datos

public class Autor {
    private Integer id;
    private String nombre;
    private String biografia;
    private Integer estado;
    // Campos añadidos
    private Date createdAt;
    private Date updatedAt;

    // Constructor vacío
    public Autor() {}

    // Constructor con todos los campos (incluyendo fechas)
    public Autor(Integer id, String nombre, String biografia, Integer estado, Date createdAt, Date updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.biografia = biografia;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor sin ID ni fechas (para cuando se va a insertar)
    public Autor(String nombre, String biografia, Integer estado) {
        // En un DAO, estas fechas se ignorarán si la base de datos las genera automáticamente (DEFAULT GETDATE())
        this(null, nombre, biografia, estado, null, null);
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + (estado != null && estado == 1 ? "Activo" : "Inactivo") +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}