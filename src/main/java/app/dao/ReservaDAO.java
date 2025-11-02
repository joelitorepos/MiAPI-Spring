package app.dao;

import app.db.DatabaseConnection;
import app.model.Reserva;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaDAO {

    // INSERT: crea una reserva y devuelve el id generado
    public int insertar(Reserva r) throws SQLException {
        String sql = "INSERT INTO Reserva (idCliente, idLibro, idCopia, fecha_vencimiento, estado, posicionCola, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdLibro());
            if (r.getIdCopia() != null) {
                ps.setInt(3, r.getIdCopia());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setTimestamp(4, Timestamp.valueOf(r.getFechaVencimiento()));
            ps.setString(5, r.getEstado());
            ps.setInt(6, r.getPosicionCola());
            ps.setString(7, r.getObservaciones());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    r.setId(id);
                    return id;
                }
            }
        }
        return -1; // no se obtuvo id
    }

    // SELECT *: lista todas las reservas (últimas primero)
    public List<Reserva> listar() throws SQLException {
        String sql = "SELECT id, idCliente, idLibro, idCopia, fecha_reserva, fecha_vencimiento, estado, posicionCola, observaciones, created_at, updated_at FROM Reserva ORDER BY id DESC";
        List<Reserva> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapReserva(rs));
            }
        }
        return lista;
    }

    // SELECT WHERE id = ?
    public Reserva buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, idCliente, idLibro, idCopia, fecha_reserva, fecha_vencimiento, estado, posicionCola, observaciones, created_at, updated_at FROM Reserva WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapReserva(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: devuelve true si actualizó al menos 1 fila
    public boolean actualizar(Reserva r) throws SQLException {
        String sql = "UPDATE Reserva SET idCliente = ?, idLibro = ?, idCopia = ?, fecha_vencimiento = ?, estado = ?, posicionCola = ?, observaciones = ?, updated_at = GETDATE() WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdLibro());
            if (r.getIdCopia() != null) {
                ps.setInt(3, r.getIdCopia());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setTimestamp(4, Timestamp.valueOf(r.getFechaVencimiento()));
            ps.setString(5, r.getEstado());
            ps.setInt(6, r.getPosicionCola());
            ps.setString(7, r.getObservaciones());
            ps.setInt(8, r.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // --- Helper: mapea un ResultSet a Reserva
    private Reserva mapReserva(ResultSet rs) throws SQLException {
        Integer idCopia = rs.getObject("idCopia") != null ? rs.getInt("idCopia") : null;
        return new Reserva(
                rs.getInt("id"),
                rs.getInt("idCliente"),
                rs.getInt("idLibro"),
                idCopia,
                rs.getTimestamp("fecha_reserva") != null ? rs.getTimestamp("fecha_reserva").toLocalDateTime() : null,
                rs.getTimestamp("fecha_vencimiento") != null ? rs.getTimestamp("fecha_vencimiento").toLocalDateTime() : null,
                rs.getString("estado"),
                rs.getInt("posicionCola"),
                rs.getString("observaciones"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }
}