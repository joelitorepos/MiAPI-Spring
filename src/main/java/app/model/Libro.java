package app.model;

import java.time.LocalDateTime;

public class Libro {
    private int id;
    private String nombre;
    private String isbn;
    private String edicion;
    private int anio;
    private String idioma;
    private String editorial;
    private String descripcion;
    private byte[] portadaImg;
    private int copiasMinimas;
    private int idAutor;
    private int idCategoria;
    private int estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor para creación (sin ID, portada opcional, fechas manejadas en BD)
    public Libro(String nombre, String isbn, String edicion, int anio, String idioma, String editorial,
                 String descripcion, byte[] portadaImg, int copiasMinimas, int idAutor, int idCategoria, int estado) {
        this.nombre = nombre;
        this.isbn = isbn;
        this.edicion = edicion;
        this.anio = anio;
        this.idioma = idioma;
        this.editorial = editorial;
        this.descripcion = descripcion;
        this.portadaImg = portadaImg;
        this.copiasMinimas = copiasMinimas;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
        this.estado = estado;
    }

    // Constructor completo (para lecturas desde BD)
    public Libro(int id, String nombre, String isbn, String edicion, int anio, String idioma, String editorial,
                 String descripcion, byte[] portadaImg, int copiasMinimas, int idAutor, int idCategoria, int estado,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.isbn = isbn;
        this.edicion = edicion;
        this.anio = anio;
        this.idioma = idioma;
        this.editorial = editorial;
        this.descripcion = descripcion;
        this.portadaImg = portadaImg;
        this.copiasMinimas = copiasMinimas;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getEdicion() { return edicion; }
    public void setEdicion(String edicion) { this.edicion = edicion; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public byte[] getPortadaImg() { return portadaImg; }
    public void setPortadaImg(byte[] portadaImg) { this.portadaImg = portadaImg; }
    public int getCopiasMinimas() { return copiasMinimas; }
    public void setCopiasMinimas(int copiasMinimas) { this.copiasMinimas = copiasMinimas; }
    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}