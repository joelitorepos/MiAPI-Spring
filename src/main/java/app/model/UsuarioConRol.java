package app.model;

public class UsuarioConRol {
    private int id;
    private String username;
    private String nombre;
    private String rolNombre;
    private int estado;

    public UsuarioConRol(int id, String username, String nombre, String rolNombre, int estado) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.rolNombre = rolNombre;
        this.estado = estado;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getRolNombre() { return rolNombre; }
    public int getEstado() { return estado; }
}