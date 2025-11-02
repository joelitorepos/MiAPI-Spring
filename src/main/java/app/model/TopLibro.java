package app.model;

public class TopLibro {
    private String nombre;
    private String autorNombre;
    private int conteoPrestamos;

    // Constructor (populate from ResultSet in DAO)
    public TopLibro(String nombre, String autorNombre, int conteoPrestamos) {
        this.nombre = nombre;
        this.autorNombre = autorNombre;
        this.conteoPrestamos = conteoPrestamos;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getAutorNombre() { return autorNombre; }
    public int getConteoPrestamos() { return conteoPrestamos; }
}