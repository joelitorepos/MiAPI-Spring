package app.model;

import java.util.Objects;
import java.util.Date; // Necesario para los campos DATETIME de la base de datos

public class CopiaLibro {
    private Integer id; // Usar Integer para consistencia con Autor.java, aunque int funciona para PKs no null
    private Integer idLibro; // Usar Integer para consistencia
    private String codInventario;
    private String ubicacion;
    private String sala; // Nuevo campo de DB
    private String estante; // Nuevo campo de DB
    private String nivel; // Nuevo campo de DB
    private String estadoCopia; // 'Excelente', 'Bueno', 'Deteriorado', 'Extraviado'
    private Integer estado; // 1=Activa, 0=Dada de Baja
    private Date createdAt; // Nuevo campo de DB
    private Date updatedAt; // Nuevo campo de DB

    public CopiaLibro() {}

    // Constructor completo para recuperación de DB
    public CopiaLibro(Integer id, Integer idLibro, String codInventario, String ubicacion, String sala, String estante, String nivel, String estadoCopia, Integer estado, Date createdAt, Date updatedAt) {
        this.id = id;
        this.idLibro = idLibro;
        this.codInventario = codInventario;
        this.ubicacion = ubicacion;
        this.sala = sala;
        this.estante = estante;
        this.nivel = nivel;
        this.estadoCopia = estadoCopia;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor para inserción (sin ID ni fechas de auditoría)
    public CopiaLibro(Integer idLibro, String codInventario, String ubicacion, String sala, String estante, String nivel, String estadoCopia, Integer estado) {
        this(null, idLibro, codInventario, ubicacion, sala, estante, nivel, estadoCopia, estado, null, null);
    }

    // El constructor original para inserción (sin los nuevos campos de ubicación) podría ser útil pero ahora está incompleto
    // public CopiaLibro(int idLibro, String codInventario, String ubicacion, String estadoCopia, int estado) { ... }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getIdLibro() { return idLibro; }
    public void setIdLibro(Integer idLibro) { this.idLibro = idLibro; }
    public String getCodInventario() { return codInventario; }
    public void setCodInventario(String codInventario) { this.codInventario = codInventario; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    // Nuevos Getters y Setters
    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }
    public String getEstante() { return estante; }
    public void setEstante(String estante) { this.estante = estante; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getEstadoCopia() { return estadoCopia; }
    public void setEstadoCopia(String estadoCopia) { this.estadoCopia = estadoCopia; }
    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // Se asume que solo el ID es la clave de igualdad
        CopiaLibro that = (CopiaLibro) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}