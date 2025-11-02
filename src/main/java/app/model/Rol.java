package app.model;

public class Rol {
    private int id;
    private String nombre;
    private String descripcion;
    private int estado;

    public Rol(String administrador, String s) {}

    public Rol(int id, String nombre, String descripcion, int estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}