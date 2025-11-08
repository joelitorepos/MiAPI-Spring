package app.dao;

import app.model.CajaMovimiento;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CajaMovimientoDAO {
    private final DataSource dataSource;

    public CajaMovimientoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT: crea un movimiento y devuelve el id generado. La fecha se genera en DB si no se provee.
    public int insertar(CajaMovimiento m) throws SQLException {
        String sql = "INSERT INTO CajaMovimiento (idUsuario, tipo, concepto, monto, fecha, referencia, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getIdUsuario());
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getConcepto());
            ps.setBigDecimal(4, m.getMonto());
            if (m.getFecha() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(m.getFecha()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setString(6, m.getReferencia());
            ps.setString(7, m.getObservaciones());

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

    // UPDATE: devuelve true si actualizó al menos 1 fila (agregado para consistencia, aunque movimientos de caja podrían ser inmutables)
    public boolean actualizar(CajaMovimiento m) throws SQLException {
        String sql = "UPDATE CajaMovimiento SET idUsuario = ?, tipo = ?, concepto = ?, monto = ?, fecha = ?, " +
                "referencia = ?, observaciones = ?, updated_at = GETDATE() WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, m.getIdUsuario());
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getConcepto());
            ps.setBigDecimal(4, m.getMonto());
            if (m.getFecha() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(m.getFecha()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setString(6, m.getReferencia());
            ps.setString(7, m.getObservaciones());
            ps.setInt(8, m.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // SELECT *: lista todos los movimientos (últimos primero)
    public List<CajaMovimiento> listar() throws SQLException {
        String sql = "SELECT id, idUsuario, tipo, concepto, monto, fecha, referencia, observaciones, created_at, updated_at " +
                "FROM CajaMovimiento ORDER BY id DESC";
        List<CajaMovimiento> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapCajaMovimiento(rs));
            }
        }
        return lista;
    }

    // SELECT WHERE id = ?
    public CajaMovimiento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, idUsuario, tipo, concepto, monto, fecha, referencia, observaciones, created_at, updated_at " +
                "FROM CajaMovimiento WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCajaMovimiento(rs);
                }
            }
        }
        return null;
    }

    // --- Helper: mapea un ResultSet a CajaMovimiento
    private CajaMovimiento mapCajaMovimiento(ResultSet rs) throws SQLException {
        return new CajaMovimiento(
                rs.getInt("id"),
                rs.getInt("idUsuario"),
                rs.getString("tipo"),
                rs.getString("concepto"),
                rs.getBigDecimal("monto"),
                rs.getTimestamp("fecha") != null ? rs.getTimestamp("fecha").toLocalDateTime() : null,
                rs.getString("referencia"),
                rs.getString("observaciones"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }

    public List<CajaMovimiento> listarPorFecha(LocalDateTime fecha) throws SQLException {
        List<CajaMovimiento> movimientos = new ArrayList<>();
        // La tabla CajaMovimiento tiene una columna 'fecha' de tipo DATETIME.
        // Se usa CAST(? AS DATE) para asegurar la comparación solo por la parte de la fecha.
        final String SQL = "SELECT * FROM CajaMovimiento WHERE CAST(fecha AS DATE) = CAST(? AS DATE) ORDER BY fecha DESC";

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL)) {

            // Convertir LocalDateTime a Timestamp para el PreparedStatement
            Timestamp sqlDate = Timestamp.valueOf(fecha);
            stmt.setTimestamp(1, sqlDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(mapCajaMovimiento(rs));
                }
            }
        }
        return movimientos;
    }

    public BigDecimal calcularSaldoDiario(LocalDateTime fecha) throws SQLException {
        BigDecimal saldo = BigDecimal.ZERO;
        // Se usa una subconsulta para sumar ingresos y restar egresos en una sola query.
        // Se mantiene la lógica de comparación solo por la fecha (CAST(? AS DATE)).
        final String SQL = """
            SELECT SUM(
                CASE 
                    WHEN tipo = 'Ingreso' THEN monto 
                    WHEN tipo = 'Egreso' THEN -monto 
                    ELSE 0 
                END
            ) AS saldo_diario
            FROM CajaMovimiento 
            WHERE CAST(fecha AS DATE) = CAST(? AS DATE)
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL)) {

            // Se necesita la fecha en formato Timestamp para la DB
            Timestamp sqlDate = Timestamp.valueOf(fecha);
            stmt.setTimestamp(1, sqlDate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal result = rs.getBigDecimal("saldo_diario");
                    if (result != null) {
                        saldo = result;
                    }
                }
            }
        }
        return saldo;
    }
}