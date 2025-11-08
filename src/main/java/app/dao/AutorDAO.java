package app.dao;

import app.model.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AutorDAO {
    private final DataSource dataSource;

    public AutorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT: crea un autor y devuelve el id generado
    // NOTA: No se incluyen 'created_at' ni 'updated_at' porque la BD las establece con DEFAULT GETDATE().
    public int insertar(Autor a) throws SQLException {
        String sql = "INSERT INTO autor (nombre, biografia, estado) VALUES (?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getBiografia());
            ps.setInt(3, a.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    a.setId(id);
                    return id;
                }
            }
        }
        return -1; // no se obtuvo id
    }

    // SELECT *: lista todos los autores (últimos primero)
    // MODIFICADO: Se agregaron created_at y updated_at a la consulta SELECT.
    public List<Autor> listar() throws SQLException {
        String sql = "SELECT id, nombre, biografia, estado, created_at, updated_at FROM autor ORDER BY id DESC";
        List<Autor> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapAutor(rs));
            }
        }
        return lista;
    }

    // SELECT WHERE id = ?
    // MODIFICADO: Se agregaron created_at y updated_at a la consulta SELECT.
    public Autor buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, biografia, estado, created_at, updated_at FROM autor WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAutor(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: devuelve true si actualizó al menos 1 fila
    // MODIFICADO: Se agregó 'updated_at = GETDATE()' para que la BD actualice la fecha automáticamente.
    public boolean actualizar(Autor a) throws SQLException {
        String sql = "UPDATE autor SET nombre = ?, biografia = ?, estado = ?, updated_at = GETDATE() WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getBiografia());
            ps.setInt(3, a.getEstado());
            // No se pasa un parámetro para updated_at, se usa la función de SQL Server GETDATE()
            ps.setInt(4, a.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // --- Helper: mapea un ResultSet a Autor
    // MODIFICADO: Se agregaron los campos created_at y updated_at.
    private Autor mapAutor(ResultSet rs) throws SQLException {
        return new Autor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("biografia"),
                rs.getInt("estado"),
                // Usamos getTimestamp para los campos DATETIME de SQL Server
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}