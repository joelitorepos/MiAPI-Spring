package app.dao;

import app.model.Parametro;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ParametroDAO {
    private final DataSource dataSource;

    public ParametroDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT: crea un parámetro y devuelve el id generado
    public int insertar(Parametro p) throws SQLException {
        // Uso de comillas triples para SQL multilínea (Java 15+)
        String sql = """
            INSERT INTO Parametro (clave, valor, descripcion) 
            VALUES (?, ?, ?)
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getClave());
            ps.setString(2, p.getValor());
            ps.setString(3, p.getDescripcion());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    p.setId(id);
                    return id;
                }
            }
        }
        return -1; // no se obtuvo id
    }

    // SELECT *: lista todos los parámetros (últimos primero)
    public List<Parametro> listar() throws SQLException {
        String sql = """
            SELECT id, clave, valor, descripcion, created_at, updated_at 
            FROM Parametro 
            ORDER BY id DESC
            """;
        List<Parametro> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapParametro(rs));
            }
        }
        return lista;
    }

    // SELECT WHERE id = ?
    public Parametro buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT id, clave, valor, descripcion, created_at, updated_at 
            FROM Parametro WHERE id = ?
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapParametro(rs);
                }
            }
        }
        return null;
    }

    // SELECT WHERE clave = ?: Obtiene un parámetro por su clave (necesario en Prestamos.java)
    public Parametro buscarPorClave(String clave) throws SQLException {
        String sql = """
            SELECT id, clave, valor, descripcion, created_at, updated_at 
            FROM Parametro WHERE clave = ?
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapParametro(rs);
                }
            }
        }
        return null;
    }

    // Obtiene el VALOR de un parámetro por su clave (para uso directo en la lógica de negocio)
    public String getValor(String clave) throws SQLException {
        Parametro p = buscarPorClave(clave);
        return p != null ? p.getValor() : null;
    }

    // UPDATE: devuelve true si actualizó al menos 1 fila
    public boolean actualizar(Parametro p) throws SQLException {
        String sql = """
            UPDATE Parametro 
            SET clave = ?, valor = ?, descripcion = ?, updated_at = GETDATE() 
            WHERE id = ?
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getClave());
            ps.setString(2, p.getValor());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // --- Helper: mapea un ResultSet a Parametro
    private Parametro mapParametro(ResultSet rs) throws SQLException {
        Date createdAt = rs.getTimestamp("created_at") != null ?
                new Date(rs.getTimestamp("created_at").getTime()) : null;
        Date updatedAt = rs.getTimestamp("updated_at") != null ?
                new Date(rs.getTimestamp("updated_at").getTime()) : null;

        return new Parametro(
                rs.getInt("id"),
                rs.getString("clave"),
                rs.getString("valor"),
                rs.getString("descripcion"),
                createdAt,
                updatedAt
        );
    }
}