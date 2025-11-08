package app.dao;

import app.model.Cliente;
import app.model.ClienteConMultas;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ClienteDAO {
    private final DataSource dataSource;

    public ClienteDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT: crea un cliente y devuelve el id generado
    public int insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (nombre, nit, telefono, email, direccion, historial, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getNit());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            ps.setString(6, c.getHistorial());
            ps.setInt(7, c.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    c.setId(id);
                    return id;
                }
            }
        }
        return -1; // no se obtuvo id
    }

    // SELECT *: lista todos los clientes (últimos primero)
    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT id, nombre, nit, telefono, email, direccion, fecha_registro, estado, historial, created_at, updated_at FROM Cliente ORDER BY id DESC";
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapCliente(rs));
            }
        }
        return lista;
    }

    // SELECT WHERE id = ?
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, nit, telefono, email, direccion, fecha_registro, estado, historial, created_at, updated_at FROM Cliente WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCliente(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: devuelve true si actualizó al menos 1 fila
    public boolean actualizar(Cliente c) throws SQLException {
        String sql = "UPDATE Cliente SET nombre = ?, nit = ?, telefono = ?, email = ?, direccion = ?, historial = ?, estado = ?, updated_at = GETDATE() WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getNit());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            ps.setString(6, c.getHistorial());
            ps.setInt(7, c.getEstado());
            ps.setInt(8, c.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // --- Helper: mapea un ResultSet a Cliente
    private Cliente mapCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("nit"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("direccion"),
                rs.getTimestamp("fecha_registro") != null ? rs.getTimestamp("fecha_registro").toLocalDateTime() : null,
                rs.getInt("estado"),
                rs.getString("historial"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }

    public List<ClienteConMultas> listarConMultasPendientes() throws SQLException {
        List<ClienteConMultas> lista = new ArrayList<>();

        final String SQL = """
            SELECT
                c.*,
                COUNT(m.id) AS conteoMultasPendientes,
                SUM(m.monto) AS totalAdeudado
            FROM Cliente c
            JOIN Multa m ON c.id = m.idCliente
            WHERE m.estadoPago = 'Pendiente'
            GROUP BY
                c.id, c.nombre, c.nit, c.telefono, c.email, c.direccion, c.fecha_registro,
                c.estado, c.historial, c.created_at, c.updated_at -- Todos los campos del GROUP BY
            HAVING COUNT(m.id) > 0
            ORDER BY totalAdeudado DESC;
        """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // 1. Mapear el objeto Cliente base (asumo que tienes el helper mapCliente(rs))
                Cliente clienteBase = mapCliente(rs);

                // 2. Mapear los campos calculados
                int conteo = rs.getInt("conteoMultasPendientes");
                // Usamos getBigDecimal y verificamos si es null (aunque HAVING lo previene)
                BigDecimal totalAdeudado = rs.getBigDecimal("totalAdeudado");
                if (totalAdeudado == null) totalAdeudado = BigDecimal.ZERO;

                // 3. Crear y agregar el objeto de detalles
                lista.add(new ClienteConMultas(clienteBase, conteo, totalAdeudado));
            }
        }
        return lista;
    }
}