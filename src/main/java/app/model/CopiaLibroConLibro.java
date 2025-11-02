package app.model;

public class CopiaLibroConLibro {

    // Campos propios de CopiaLibro (sin fechas)
    private int id;
    private String codInventario;
    private String ubicacion;
    private String sala; // Nuevo campo
    private String estante; // Nuevo campo
    private String nivel; // Nuevo campo
    private String estadoCopia;
    private int estado;

    // Campo del JOIN (Nombre del Libro)
    private String libroNombre;

    // Constructor completo para el mapeo desde el DAO
    public CopiaLibroConLibro(
            int id, String codInventario, String ubicacion, String sala,
            String estante, String nivel, String estadoCopia, int estado,
            String libroNombre) {

        this.id = id;
        this.codInventario = codInventario;
        this.ubicacion = ubicacion;
        this.sala = sala;
        this.estante = estante;
        this.nivel = nivel;
        this.estadoCopia = estadoCopia;
        this.estado = estado;
        this.libroNombre = libroNombre;
    }

    // Getters necesarios para la tabla/vista
    public int getId() { return id; }
    public String getCodInventario() { return codInventario; }
    public String getUbicacion() { return ubicacion; }
    public String getSala() { return sala; }
    public String getEstante() { return estante; }
    public String getNivel() { return nivel; }
    public String getEstadoCopia() { return estadoCopia; }
    public int getEstado() { return estado; }
    public String getLibroNombre() { return libroNombre; }

    // Nota: Puedes agregar un constructor sin argumentos o más lógica si la necesitas.
}