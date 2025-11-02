package app.dao;

import app.db.DatabaseConnection;
import app.model.Libro;
import app.model.LibroConAutor;
import app.model.LibroConAutorYCategoria;
import app.model.TopLibro;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LibroDAO {
    public List<Libro> listar() throws SQLException {
        String sql = "SELECT id, nombre, isbn, idAutor, idCategoria, estado, created_at, updated_at FROM Libro ORDER BY id DESC";
        List<Libro> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapLibro(rs));
            }
        }
        return lista;
    }



    public int insertar(Libro l) throws SQLException {
        String sql = "INSERT INTO Libro (nombre, isbn, edicion, anio, idioma, editorial, descripcion, portada_img, copiasMinimas, idAutor, idCategoria, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, l.getNombre());
            ps.setString(2, l.getIsbn());
            ps.setString(3, l.getEdicion());
            ps.setInt(4, l.getAnio());
            ps.setString(5, l.getIdioma());
            ps.setString(6, l.getEditorial());
            ps.setString(7, l.getDescripcion());
            ps.setBytes(8, l.getPortadaImg());
            ps.setInt(9, l.getCopiasMinimas());
            ps.setInt(10, l.getIdAutor());
            ps.setInt(11, l.getIdCategoria());
            ps.setInt(12, l.getEstado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    l.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public boolean actualizar(Libro l) throws SQLException {
        String sql = "UPDATE Libro SET nombre=?, isbn=?, edicion=?, anio=?, idioma=?, editorial=?, descripcion=?, " +
                "portada_img=?, copiasMinimas=?, idAutor=?, idCategoria=?, estado=?, updated_at=GETDATE() WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getNombre());
            ps.setString(2, l.getIsbn());
            ps.setString(3, l.getEdicion());
            ps.setInt(4, l.getAnio());
            ps.setString(5, l.getIdioma());
            ps.setString(6, l.getEditorial());
            ps.setString(7, l.getDescripcion());
            ps.setBytes(8, l.getPortadaImg());
            ps.setInt(9, l.getCopiasMinimas());
            ps.setInt(10, l.getIdAutor());
            ps.setInt(11, l.getIdCategoria());
            ps.setInt(12, l.getEstado());
            ps.setInt(13, l.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public Libro buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, isbn, edicion, anio, idioma, editorial, descripcion, portada_img, " +
                "copiasMinimas, idAutor, idCategoria, estado, created_at, updated_at FROM Libro WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapLibro(rs);
                }
            }
        }
        return null;
    }

    // Lista con JOIN para mostrar el nombre del autor en la tabla
    public List<LibroConAutor> listarConAutor() throws SQLException {
        String sql = """
                SELECT l.id, l.nombre, l.anio, a.nombre AS autorNombre, l.estado
                FROM Libro l
                JOIN Autor a ON a.id = l.idAutor
                ORDER BY l.id DESC
                """;
        List<LibroConAutor> data = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new LibroConAutor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("anio"),
                        rs.getString("autorNombre"),
                        rs.getInt("estado")
                ));
            }
        }
        return data;
    }

    // ... dentro de LibroDAO.java

    public List<LibroConAutorYCategoria> listarConAutorYCategoria() throws SQLException {
        String sql = """
            SELECT 
                l.id, l.nombre AS libroNombre, l.isbn, l.edicion, l.anio, 
                l.idioma, l.editorial, l.descripcion, l.copiasMinimas,
                a.nombre AS autorNombre,
                c.nombre AS categoriaNombre,
                l.estado
            FROM Libro l
            JOIN Autor a ON a.id = l.idAutor
            JOIN Categoria c ON c.id = l.idCategoria
            ORDER BY l.id DESC
            """; // <- Esta consulta debe traer todos los campos listados arriba

        List<LibroConAutorYCategoria> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new LibroConAutorYCategoria(
                        rs.getInt("id"),
                        rs.getString("libroNombre"),
                        rs.getString("isbn"),
                        rs.getString("edicion"),
                        rs.getInt("anio"),
                        rs.getString("idioma"),
                        rs.getString("editorial"),
                        rs.getString("descripcion"),
                        rs.getInt("copiasMinimas"),
                        rs.getString("autorNombre"),
                        rs.getString("categoriaNombre"),
                        rs.getInt("estado")
                ));
            }
        }
        return lista;
    }

    // Helper: mapea un ResultSet a Libro
    private Libro mapLibro(ResultSet rs) throws SQLException {
        return new Libro(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("isbn"),
                rs.getString("edicion"),
                rs.getInt("anio"),
                rs.getString("idioma"),
                rs.getString("editorial"),
                rs.getString("descripcion"),
                rs.getBytes("portada_img"),
                rs.getInt("copiasMinimas"),
                rs.getInt("idAutor"),
                rs.getInt("idCategoria"),
                rs.getInt("estado"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }

    private LibroConAutorYCategoria mapRowToLibroConAutorYCategoria(ResultSet rs) throws SQLException {
        // Copia exacta de la lógica de mapeo que se ve en tu LibroDAO.java para el DTO.
        return new LibroConAutorYCategoria(
                rs.getInt("libroId"),
                rs.getString("libroNombre"),
                rs.getString("isbn"),
                rs.getString("edicion"),
                rs.getInt("anio"),
                rs.getString("idioma"),
                rs.getString("editorial"),
                rs.getString("descripcion"),
                rs.getInt("copiasMinimas"),
                rs.getString("autorNombre"),
                rs.getString("categoriaNombre"),
                rs.getInt("estado")
        );
    }

    public List<LibroConAutorYCategoria> listarPorRangoAnio(int anioInicio, int anioFin) throws SQLException {
        // Se reutiliza la lógica de buscarConFiltros con solo el filtro de año.
        // No se pasa busqueda, ni disponibilidad.
        return buscarConFiltros(null, null, anioInicio, anioFin, false);
    }

    public List<LibroConAutorYCategoria> listarPorDisponibilidad(boolean soloDisponibles) throws SQLException {
        // Se reutiliza la lógica de buscarConFiltros con solo el filtro de disponibilidad.
        return buscarConFiltros(null, null, 0, 0, soloDisponibles);
    }

    public List<LibroConAutorYCategoria> buscarConFiltros(
            String busqueda, String filtro, int anioInicio, int anioFin, boolean soloDisponibles) throws SQLException {

        List<LibroConAutorYCategoria> lista = new ArrayList<>();
        int paramIndex = 1; // Contador para el índice de parámetros del PreparedStatement

        // 1. Base Query con JOINs necesarios para el DTO
        StringBuilder sql = new StringBuilder("""
        SELECT 
            l.id AS libroId, l.nombre AS libroNombre, l.isbn, l.edicion, l.anio, l.idioma, l.editorial, l.descripcion, l.copiasMinimas, l.estado,
            a.nombre AS autorNombre,
            c.nombre AS categoriaNombre
        FROM Libro l
        JOIN Autor a ON l.idAutor = a.id
        JOIN Categoria c ON l.idCategoria = c.id
        WHERE l.estado = 1
        """);

        // 2. Lógica para Disponibilidad (JOIN de CopiaLibro y Prestamo)
        if (soloDisponibles) {
            sql.append("""
            AND EXISTS (
                SELECT 1
                FROM CopiaLibro cp
                LEFT JOIN Prestamo p ON cp.id = p.idCopia AND p.estado = 'Activo'
                WHERE cp.idLibro = l.id AND cp.estado = 1 AND p.id IS NULL
            )
            """);
        }

        // 3. Lógica para Rango de Año
        if (anioInicio > 0 && anioFin > 0 && anioFin >= anioInicio) {
            sql.append(" AND l.anio BETWEEN ? AND ?");
        }

        // 4. Lógica para Búsqueda por Texto
        boolean hasBusqueda = busqueda != null && !busqueda.trim().isEmpty();
        if (hasBusqueda) {
            String likeValue = "%" + busqueda.trim() + "%";

            sql.append(" AND (");
            if ("Título".equals(filtro)) {
                sql.append("l.nombre LIKE ?");
            } else if ("Autor".equals(filtro)) {
                sql.append("a.nombre LIKE ?");
            } else if ("Categoría".equals(filtro)) {
                sql.append("c.nombre LIKE ?");
            } else {
                // Búsqueda general si filtro no coincide (fallback)
                sql.append("l.nombre LIKE ? OR l.isbn LIKE ? OR l.editorial LIKE ?");
            }
            sql.append(")");
        }

        sql.append(" ORDER BY l.nombre ASC");

        // 5. Ejecución
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // Asignación de parámetros (sigue el orden de la cláusula WHERE)

            // a) Parámetros de Rango de Año
            if (anioInicio > 0 && anioFin > 0 && anioFin >= anioInicio) {
                ps.setInt(paramIndex++, anioInicio);
                ps.setInt(paramIndex++, anioFin);
            }

            // b) Parámetros de Búsqueda por Texto
            if (hasBusqueda) {
                String likeValue = "%" + busqueda.trim() + "%";
                if ("Título".equals(filtro) || "Autor".equals(filtro) || "Categoría".equals(filtro)) {
                    ps.setString(paramIndex++, likeValue); // Solo uno para el campo específico
                } else {
                    // General
                    ps.setString(paramIndex++, likeValue); // l.nombre
                    ps.setString(paramIndex++, likeValue); // l.isbn
                    ps.setString(paramIndex++, likeValue); // l.editorial
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRowToLibroConAutorYCategoria(rs));
                }
            }
        }
        return lista;
    }

    public List<TopLibro> getTopLibrosPrestados(int limite) throws SQLException {
        List<TopLibro> topLibros = new ArrayList<>();

        // Consulta SQL para agrupar préstamos por libro, contar, y unir con Autor
        final String SQL = """
            SELECT TOP (?)
                l.nombre AS nombre,
                a.nombre AS autorNombre,
                COUNT(p.id) AS conteoPrestamos
            FROM Prestamo p
            JOIN CopiaLibro cp ON p.idCopia = cp.id
            JOIN Libro l ON cp.idLibro = l.id
            JOIN Autor a ON l.idAutor = a.id
            GROUP BY l.nombre, a.nombre
            ORDER BY conteoPrestamos DESC;
        """; // Utilizamos TOP (?) de SQL Server

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, limite);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nombreLibro = rs.getString("nombre");
                    String nombreAutor = rs.getString("autorNombre");
                    int conteo = rs.getInt("conteoPrestamos");

                    topLibros.add(new TopLibro(nombreLibro, nombreAutor, conteo));
                }
            }
        }
        return topLibros;
    }
}