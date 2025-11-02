package app.model;

public class CopiaLibroConDetalles {
    private Integer id;
    private String libroNombre;
    private String sala;
    private String estante;
    private String estadoCopia;

    // Constructor (populate from ResultSet in DAO)
    public CopiaLibroConDetalles(Integer id, String libroNombre, String sala, String estante, String estadoCopia) {
        this.id = id;
        this.libroNombre = libroNombre;
        this.sala = sala;
        this.estante = estante;
        this.estadoCopia = estadoCopia;
    }

    // Getters
    public Integer getId() { return id; }
    public String getLibroNombre() { return libroNombre; }
    public String getSala() { return sala; }
    public String getEstante() { return estante; }
    public String getEstadoCopia() { return estadoCopia; }
}