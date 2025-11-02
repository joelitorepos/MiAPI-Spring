package app.model;

import java.util.Date;

public class Parametro {
    private Integer id;
    private String clave;
    private String valor;
    private String descripcion;
    private Date createdAt;
    private Date updatedAt;

    // Constructor vacío
    public Parametro() {}

    // Constructor para INSERCIÓN (sin ID ni fechas/campos de auditoría)
    public Parametro(String clave, String valor, String descripcion) {
        this(null, clave, valor, descripcion, null, null);
    }

    // Constructor COMPLETO (para recuperación de DB)
    public Parametro(Integer id, String clave, String valor, String descripcion, Date createdAt, Date updatedAt) {
        this.id = id;
        this.clave = clave;
        this.valor = valor;
        this.descripcion = descripcion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}