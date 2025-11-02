package app.dao;

import app.core.BarCodeManager;
import app.db.DatabaseConnection;
import app.model.CopiaLibro;
import app.model.CopiaLibroConLibro;
import app.model.CopiaLibroConDetalles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.springframework.stereotype.Repository;

@Repository
public class CopiaLibroDAO {
    public List<CopiaLibro> listarPorLibro(int idLibro) throws SQLException {
        String sql = """
        SELECT id, idLibro, codInventario, ubicacion, sala, estante, nivel, estadoCopia, estado, createdAt, updatedAt 
        FROM CopiaLibro 
        WHERE idLibro = ? 
        ORDER BY id ASC
        """;

        List<CopiaLibro> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapCopiaLibroCompleto(rs));
                }
            }
        }
        return lista;
    }

    public List<CopiaLibro> listarDisponiblesPorLibro(int idLibro) throws SQLException {
        String sql = """
        SELECT id, idLibro, codInventario, ubicacion, sala, estante, nivel, estadoCopia, estado, createdAt, updatedAt 
        FROM CopiaLibro 
        WHERE idLibro = ? AND estado = 1 AND estadoCopia = 'Disponible' 
        ORDER BY id ASC
        """;

        List<CopiaLibro> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapCopiaLibroCompleto(rs));
                }
            }
        }
        return lista;
    }

    public boolean isDisponible(int idCopiaLibro) throws SQLException {
        String sql = """
        SELECT COUNT(id) AS count 
        FROM CopiaLibro 
        WHERE id = ? AND estado = 1 AND estadoCopia = 'Disponible'
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCopiaLibro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    // INSERT: crea una copia de libro y devuelve el id generado. Genera codInventario si no está seteado.
    public int insertar(CopiaLibro c) throws SQLException {
        if (c.getCodInventario() == null || c.getCodInventario().isEmpty()) {
            c.setCodInventario(BarCodeManager.generateCodInventario());
        }

        String sql = """
            INSERT INTO CopiaLibro (idLibro, codInventario, ubicacion, sala, estante, nivel, estadoCopia, estado) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getIdLibro());
            ps.setString(2, c.getCodInventario());
            ps.setString(3, c.getUbicacion());
            ps.setString(4, c.getSala());
            ps.setString(5, c.getEstante());
            ps.setString(6, c.getNivel());
            ps.setString(7, c.getEstadoCopia());
            ps.setInt(8, c.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    c.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    // SELECT *: lista todas las copias de libros (incluye TODOS los campos)
    public List<CopiaLibro> listar() throws SQLException {
        String sql = """
            SELECT id, idLibro, codInventario, ubicacion, sala, estante, nivel, estadoCopia, estado, 
                   createdAt, updatedAt 
            FROM CopiaLibro 
            ORDER BY id DESC
            """;
        List<CopiaLibro> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapCopiaLibroCompleto(rs));
            }
        }
        return lista;
    }

    // SELECT WHERE id = ?
    public CopiaLibro buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT id, idLibro, codInventario, ubicacion, sala, estante, nivel, estadoCopia, estado, 
                   createdAt, updatedAt 
            FROM CopiaLibro WHERE id = ?
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCopiaLibroCompleto(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: devuelve true si actualizó al menos 1 fila
    public boolean actualizar(CopiaLibro c) throws SQLException {
        String sql = """
            UPDATE CopiaLibro 
            SET idLibro = ?, codInventario = ?, ubicacion = ?, sala = ?, estante = ?, nivel = ?, 
                estadoCopia = ?, estado = ?, updatedAt = NOW() 
            WHERE id = ?
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdLibro());
            ps.setString(2, c.getCodInventario());
            ps.setString(3, c.getUbicacion());
            ps.setString(4, c.getSala());
            ps.setString(5, c.getEstante());
            ps.setString(6, c.getNivel());
            ps.setString(7, c.getEstadoCopia());
            ps.setInt(8, c.getEstado());
            ps.setInt(9, c.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Lista con JOIN para mostrar el nombre del libro en la tabla (ACTUALIZADA)
    public List<CopiaLibroConLibro> listarConLibro() throws SQLException {
        String sql = """
                SELECT 
                    cl.id, cl.codInventario, cl.ubicacion, cl.sala, 
                    cl.estante, cl.nivel, cl.estadoCopia, cl.estado,
                    l.nombre AS libroNombre
                FROM CopiaLibro cl
                JOIN Libro l ON l.id = cl.idLibro
                WHERE cl.estado = 1 
                ORDER BY l.nombre, cl.id ASC
                """;

        List<CopiaLibroConLibro> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new CopiaLibroConLibro(
                        rs.getInt("id"),
                        rs.getString("codInventario"),
                        rs.getString("ubicacion"),
                        rs.getString("sala"),
                        rs.getString("estante"),
                        rs.getString("nivel"),
                        rs.getString("estadoCopia"),
                        rs.getInt("estado"),
                        rs.getString("libroNombre") // Mapeo del campo del JOIN
                ));
            }
        }
        return lista;
    }

    // --- Helper: mapea un ResultSet COMPLETO a CopiaLibro
    private CopiaLibro mapCopiaLibroCompleto(ResultSet rs) throws SQLException {
        return new CopiaLibro(
                rs.getInt("id"),
                rs.getInt("idLibro"),
                rs.getString("codInventario"),
                rs.getString("ubicacion"),
                rs.getString("sala"),
                rs.getString("estante"),
                rs.getString("nivel"),
                rs.getString("estadoCopia"),
                rs.getInt("estado"),
                rs.getTimestamp("createdAt") != null ? new Date(rs.getTimestamp("createdAt").getTime()) : null,
                rs.getTimestamp("updatedAt") != null ? new Date(rs.getTimestamp("updatedAt").getTime()) : null
        );
    }

    public List<CopiaLibroConDetalles> listarInventarioConDetalles() throws SQLException {
        List<CopiaLibroConDetalles> lista = new ArrayList<>();

        // Unimos CopiaLibro (cp) con Libro (l) para obtener el nombre del libro.
        final String SQL = """
            SELECT
                cp.id,
                l.nombre AS libroNombre,
                cp.sala,
                cp.estante,
                cp.estadoCopia
            FROM CopiaLibro cp
            JOIN Libro l ON cp.idLibro = l.id
            ORDER BY cp.sala, cp.estante, l.nombre ASC
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Mapeo directo a CopiaLibroConDetalles
                lista.add(new CopiaLibroConDetalles(
                        rs.getInt("id"),
                        rs.getString("libroNombre"),
                        rs.getString("sala"),
                        rs.getString("estante"),
                        rs.getString("estadoCopia")
                ));
            }
        }
        return lista;
    }
}