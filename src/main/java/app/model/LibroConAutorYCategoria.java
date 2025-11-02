package app.model;

// NOTA: Esta clase asume que el DAO hará un JOIN para obtener los datos.

public class LibroConAutorYCategoria {
    private int id;
    private String libroNombre; // Se usa libroNombre para evitar conflicto con AutorNombre/CategoriaNombre
    private String isbn;
    private String edicion;
    private int anio;
    private String idioma;
    private String editorial;
    private String descripcion;
    private int copiasMinimas;
    // Campos del JOIN
    private String autorNombre;
    private String categoriaNombre;
    private int estado;

    // Constructor completo para el DAO
    public LibroConAutorYCategoria(
            int id, String libroNombre, String isbn, String edicion, int anio,
            String idioma, String editorial, String descripcion, int copiasMinimas,
            String autorNombre, String categoriaNombre, int estado) {

        this.id = id;
        this.libroNombre = libroNombre;
        this.isbn = isbn;
        this.edicion = edicion;
        this.anio = anio;
        this.idioma = idioma;
        this.editorial = editorial;
        this.descripcion = descripcion;
        this.copiasMinimas = copiasMinimas;
        this.autorNombre = autorNombre;
        this.categoriaNombre = categoriaNombre;
        this.estado = estado;
    }

    public LibroConAutorYCategoria(Integer id, String libroNombre, String autorNombre, String categoriaNombre, int anio) {
        this.id = id;
        this.libroNombre = libroNombre;
        this.autorNombre = autorNombre;
        this.categoriaNombre = categoriaNombre;
        this.anio = anio;
    }

    // Getters necesarios para la función cargarTabla()
    public int getId() { return id; }
    public String getLibroNombre() { return libroNombre; }
    public String getIsbn() { return isbn; }
    public String getEdicion() { return edicion; }
    public int getAnio() { return anio; }
    public String getIdioma() { return idioma; }
    public String getEditorial() { return editorial; }
    public String getDescripcion() { return descripcion; } // Método corregido
    public int getCopiasMinimas() { return copiasMinimas; }
    public String getAutorNombre() { return autorNombre; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public int getEstado() { return estado; }

    // Si la tabla original usaba 'nombre' en lugar de 'libroNombre'
    // public String getNombre() { return libroNombre; }
}