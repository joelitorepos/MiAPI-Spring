package app.dao;

import app.db.DatabaseConnection;
import app.model.InventarioMovimiento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class InventarioMovimientoDAO {
    private final DataSource dataSource;

    public InventarioMovimientoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT: crea un movimiento y devuelve el id generado. La fecha se genera en DB si no se provee.
    public int insertar(InventarioMovimiento m) throws SQLException {
        String sql = "INSERT INTO InventarioMovimiento (idUsuario, idCopia, idLibro, tipoMovimiento, fecha, diferencia, ubicacionAnterior, ubicacionNueva, comentarios) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getIdUsuario());
            ps.setInt(2, m.getIdCopia());
            ps.setInt(3, m.getIdLibro());
            ps.setString(4, m.getTipoMovimiento());
            if (m.getFecha() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(m.getFecha()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            if (m.getDiferencia() != null) {
                ps.setInt(6, m.getDiferencia());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setString(7, m.getUbicacionAnterior());
            ps.setString(8, m.getUbicacionNueva());
            ps.setString(9, m.getComentarios());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    m.setId(id);
                    return id;
                }
            }
        }
        return -1; // no se obtuvo id
    }

    // SELECT *: lista todos los movimientos (últimos primero)
    public List<InventarioMovimiento> listar() throws SQLException {
        String sql = "SELECT id, idUsuario, idCopia, idLibro, tipoMovimiento, fecha, diferencia, ubicacionAnterior, ubicacionNueva, comentarios, created_at, updated_at " +
                "FROM InventarioMovimiento ORDER BY id DESC";
        List<InventarioMovimiento> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapInventarioMovimiento(rs));
            }
        }
        return lista;
    }

    // SELECT WHERE id = ?
    public InventarioMovimiento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, idUsuario, idCopia, idLibro, tipoMovimiento, fecha, diferencia, ubicacionAnterior, ubicacionNueva, comentarios, created_at, updated_at " +
                "FROM InventarioMovimiento WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapInventarioMovimiento(rs);
                }
            }
        }
        return null;
    }

    // --- Helper: mapea un ResultSet a InventarioMovimiento
    private InventarioMovimiento mapInventarioMovimiento(ResultSet rs) throws SQLException {
        return new InventarioMovimiento(
                rs.getInt("id"),
                rs.getInt("idUsuario"),
                rs.getInt("idCopia"),
                rs.getInt("idLibro"),
                rs.getString("tipoMovimiento"),
                rs.getTimestamp("fecha") != null ? rs.getTimestamp("fecha").toLocalDateTime() : null,
                rs.getObject("diferencia") != null ? rs.getInt("diferencia") : null,
                rs.getString("ubicacionAnterior"),
                rs.getString("ubicacionNueva"),
                rs.getString("comentarios"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }
}