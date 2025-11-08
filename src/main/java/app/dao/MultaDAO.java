package app.dao;

import app.model.Multa;
import app.model.MultaConDetalles;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MultaDAO {
    private final DataSource dataSource;

    public MultaDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT: crea una multa y devuelve el id generado
    public int insertar(Multa m) throws SQLException {
        String sql = """
            INSERT INTO Multa (idPrestamo, idCliente, monto, diasAtraso, 
                                estadoPago, justificacionExoneracion) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getIdPrestamo());
            ps.setInt(2, m.getIdCliente());
            ps.setBigDecimal(3, m.getMonto());
            ps.setInt(4, m.getDiasAtraso());
            ps.setString(5, m.getEstadoPago());
            ps.setString(6, m.getJustificacionExoneracion());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    m.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    // SELECT *: lista todas las multas (últimas primero)
    public List<Multa> listar() throws SQLException {
        String sql = """
            SELECT id, idPrestamo, idCliente, monto, diasAtraso, 
                   fecha_generada, fecha_pago, estadoPago, justificacionExoneracion, 
                   created_at, updated_at 
            FROM Multa 
            ORDER BY id DESC
            """;
        List<Multa> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapMultaCompleta(rs));
            }
        }
        return lista;
    }

    // SELECT por id
    public Multa buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT id, idPrestamo, idCliente, monto, diasAtraso, 
                   fecha_generada, fecha_pago, estadoPago, justificacionExoneracion, 
                   created_at, updated_at 
            FROM Multa WHERE id = ?
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMultaCompleta(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: devuelve true si actualizó al menos 1 fila
    public boolean actualizar(Multa m) throws SQLException {
        String sql = """
            UPDATE Multa 
            SET idPrestamo = ?, idCliente = ?, monto = ?, diasAtraso = ?, 
                fecha_generada = ?, fecha_pago = ?, estadoPago = ?, 
                justificacionExoneracion = ?, updated_at = GETDATE() 
            WHERE id = ?
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, m.getIdPrestamo());
            ps.setInt(2, m.getIdCliente());
            ps.setBigDecimal(3, m.getMonto());
            ps.setInt(4, m.getDiasAtraso());
            ps.setTimestamp(5, m.getFechaGenerada() != null ?
                    new Timestamp(m.getFechaGenerada().getTime()) : null);
            ps.setTimestamp(6, m.getFechaPago() != null ?
                    new Timestamp(m.getFechaPago().getTime()) : null);
            ps.setString(7, m.getEstadoPago());
            ps.setString(8, m.getJustificacionExoneracion());
            ps.setInt(9, m.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // --- Helper: mapea un ResultSet COMPLETO a Multa
    private Multa mapMultaCompleta(ResultSet rs) throws SQLException {
        Date fechaGenerada = rs.getTimestamp("fecha_generada") != null ?
                new Date(rs.getTimestamp("fecha_generada").getTime()) : null;
        Date fechaPago = rs.getTimestamp("fecha_pago") != null ?
                new Date(rs.getTimestamp("fecha_pago").getTime()) : null;

        return new Multa(
                rs.getInt("id"),
                rs.getInt("idPrestamo"),
                rs.getInt("idCliente"),
                rs.getBigDecimal("monto"),
                rs.getInt("diasAtraso"),
                fechaGenerada,
                fechaPago,
                rs.getString("estadoPago"),
                rs.getString("justificacionExoneracion"),
                rs.getTimestamp("created_at") != null ?
                        new Date(rs.getTimestamp("created_at").getTime()) : null,
                rs.getTimestamp("updated_at") != null ?
                        new Date(rs.getTimestamp("updated_at").getTime()) : null
        );
    }

    public BigDecimal getTotalPendientePorCliente(int idCliente) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        // Asumo que la columna de monto es 'monto' y el estado de pago es 'estadoPago' en la tabla Multa.
        final String SQL = "SELECT SUM(monto) AS total_pendiente FROM Multa WHERE idCliente = ? AND estadoPago = 'Pendiente'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Usamos getBigDecimal y verificamos si es null (cuando SUM no encuentra filas)
                    BigDecimal result = rs.getBigDecimal("total_pendiente");
                    if (result != null) {
                        total = result;
                    }
                }
            }
        }
        return total;
    }

    public BigDecimal calcularTotalRecaudado(Date fechaInicio, Date fechaFin) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;

        // El estado debe ser 'Pagada' para considerarse recaudado.
        final String SQL = """
            SELECT SUM(monto) AS totalRecaudado
            FROM Multa 
            WHERE estadoPago = 'Pagada' 
            AND fechaPago BETWEEN ? AND DATEADD(day, 1, ?);
        """; // Usamos DATEADD(day, 1, ?) en SQL Server para incluir todo el último día

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            // Convertir java.util.Date a java.sql.Timestamp
            ps.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fechaFin.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Usamos getBigDecimal y verificamos si es null (cuando SUM no encuentra filas)
                    BigDecimal result = rs.getBigDecimal("totalRecaudado");
                    if (result != null) {
                        total = result;
                    }
                }
            }
        }
        return total;
    }

    public List<MultaConDetalles> listarPagadasPorPeriodo(Date fechaInicio, Date fechaFin) throws SQLException {
        List<MultaConDetalles> lista = new ArrayList<>();

        // Unimos Multa (m) con Cliente (cl) para obtener el nombre.
        final String SQL = """
            SELECT 
                m.*,
                cl.nombre AS clienteNombre
            FROM Multa m
            JOIN Cliente cl ON m.idCliente = cl.id
            WHERE m.estadoPago = 'Pagada' 
            AND m.fechaPago BETWEEN ? AND DATEADD(day, 1, ?)
            ORDER BY m.fechaPago DESC;
        """; // Filtramos por estado 'Pagada' y fechaPago en el rango

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            // Convertir java.util.Date a java.sql.Timestamp
            ps.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fechaFin.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // 1. Mapear el Multa base
                    Multa multaBase = mapMultaCompleta(rs); // Asumo que este helper existe

                    // 2. Mapear los detalles del JOIN
                    String clienteNombre = rs.getString("clienteNombre");

                    // 3. Crear el objeto MultaConDetalles
                    // El constructor de MultaConDetalles necesita varios campos, se los extraemos de Multa base
                    MultaConDetalles multaDetalle = new MultaConDetalles(
                            multaBase.getId(),
                            multaBase.getIdPrestamo(),
                            multaBase.getIdCliente(),
                            multaBase.getMonto(),
                            multaBase.getDiasAtraso(),
                            multaBase.getFechaGenerada(),
                            multaBase.getFechaPago(),
                            multaBase.getEstadoPago(),
                            multaBase.getJustificacionExoneracion(),
                            clienteNombre // Agregamos el nombre del cliente
                    );

                    lista.add(multaDetalle);
                }
            }
        }
        return lista;
    }
}
