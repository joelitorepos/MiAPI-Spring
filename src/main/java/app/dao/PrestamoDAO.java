package app.dao;

import app.model.ComboItem;
import app.model.Prestamo;
import app.model.PrestamoConDetalles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PrestamoDAO {
    private final DataSource dataSource;

    public PrestamoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT: crea un prestamo y devuelve el id generado
    public int insertar(Prestamo p) throws SQLException {
        String sql = """
            INSERT INTO prestamo (idCliente, idCopia, idUsuario, fecha_prestamo, fecha_vencimiento, 
                                estado, observaciones) 
            VALUES (?, ?, ?, NOW(), ?, ?, ?)
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getIdCliente());
            ps.setInt(2, p.getIdCopia());  // ✅ Corregido: idCopia, no idLibro
            ps.setInt(3, p.getIdUsuario());
            ps.setDate(4, new java.sql.Date(p.getFechaVencimiento().getTime()));
            ps.setString(5, p.getEstado());
            ps.setString(6, p.getObservaciones());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    p.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    // SELECT *: lista todos los prestamos (últimos primero)
    public List<Prestamo> listar() throws SQLException {
        String sql = """
            SELECT id, idCliente, idCopia, idUsuario, fecha_prestamo, fecha_vencimiento, 
                   fecha_devolucion, estado, observaciones, created_at, updated_at 
            FROM prestamo 
            ORDER BY id DESC
            """;
        List<Prestamo> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapPrestamoCompleto(rs));
            }
        }
        return lista;
    }

    // SELECT por id
    public Prestamo buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT id, idCliente, idCopia, idUsuario, fecha_prestamo, fecha_vencimiento, 
                   fecha_devolucion, estado, observaciones, created_at, updated_at 
            FROM prestamo WHERE id = ?
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPrestamoCompleto(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: devuelve true si actualizó al menos 1 fila
    public boolean actualizar(Prestamo p) throws SQLException {
        String sql = """
            UPDATE prestamo 
            SET idCliente = ?, idCopia = ?, idUsuario = ?, fecha_prestamo = ?, 
                fecha_vencimiento = ?, fecha_devolucion = ?, estado = ?, observaciones = ?, 
                updated_at = NOW() 
            WHERE id = ?
            """;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdCliente());
            ps.setInt(2, p.getIdCopia());
            ps.setInt(3, p.getIdUsuario());
            ps.setTimestamp(4, p.getFechaPrestamo() != null ?
                    new Timestamp(p.getFechaPrestamo().getTime()) : null);
            ps.setTimestamp(5, p.getFechaVencimiento() != null ?
                    new Timestamp(p.getFechaVencimiento().getTime()) : null);
            ps.setTimestamp(6, p.getFechaDevolucion() != null ?
                    new Timestamp(p.getFechaDevolucion().getTime()) : null);
            ps.setString(7, p.getEstado());
            ps.setString(8, p.getObservaciones());
            ps.setInt(9, p.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // --- Helper: mapea un ResultSet COMPLETO a Prestamo
    private Prestamo mapPrestamoCompleto(ResultSet rs) throws SQLException {
        Date fechaPrestamo = rs.getTimestamp("fecha_prestamo") != null ?
                new Date(rs.getTimestamp("fecha_prestamo").getTime()) : null;
        Date fechaVencimiento = rs.getTimestamp("fecha_vencimiento") != null ?
                new Date(rs.getTimestamp("fecha_vencimiento").getTime()) : null;
        Date fechaDevolucion = rs.getTimestamp("fecha_devolucion") != null ?
                new Date(rs.getTimestamp("fecha_devolucion").getTime()) : null;

        return new Prestamo(
                rs.getInt("id"),
                rs.getInt("idCliente"),
                rs.getInt("idCopia"),
                rs.getInt("idUsuario"),
                fechaPrestamo,
                fechaVencimiento,
                fechaDevolucion,
                rs.getString("estado"),
                rs.getString("observaciones"),
                rs.getTimestamp("created_at") != null ?
                        new Date(rs.getTimestamp("created_at").getTime()) : null,
                rs.getTimestamp("updated_at") != null ?
                        new Date(rs.getTimestamp("updated_at").getTime()) : null
        );
    }

    // --- Cargar combos con solo activos (ACTUALIZADOS)
    public List<ComboItem> listarClientesActivos() throws SQLException {
        String sql = "SELECT id, nombre FROM cliente WHERE estado != 0 ORDER BY nombre";
        List<ComboItem> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ComboItem(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return lista;
    }

    // ✅ Nuevo método para copias disponibles
    public List<ComboItem> listarCopiasDisponibles() throws SQLException {
        String sql = """
            SELECT cl.id, CONCAT(l.nombre, ' - ', cl.codInventario) AS texto 
            FROM CopiaLibro cl
            INNER JOIN Libro l ON l.id = cl.idLibro
            WHERE cl.estado = 1 
            AND cl.id NOT IN (SELECT idCopia FROM prestamo WHERE estado = 'Activo')
            ORDER BY l.nombre, cl.codInventario
            """;
        List<ComboItem> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ComboItem(rs.getInt("id"), rs.getString("texto")));
            }
        }
        return lista;
    }

    public List<ComboItem> listarUsuariosActivos() throws SQLException {
        String sql = "SELECT id, CONCAT(nombre, ' ', apellido) AS nombreCompleto FROM usuario WHERE estado = 1 ORDER BY nombre";
        List<ComboItem> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ComboItem(rs.getInt("id"), rs.getString("nombreCompleto")));
            }
        }
        return lista;
    }

    public List<Prestamo> listarVencidos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        // Asumiendo que 'Activo' es el estado para un préstamo no devuelto.
        // La tabla Prestamo tiene columnas fecha_vencimiento (DATETIME) y estado (NVARCHAR).
        final String SQL = "SELECT * FROM Prestamo WHERE fecha_vencimiento < GETDATE() AND estado = 'Activo' AND estado != 'Devuelto'";

        try (Connection conn = DatabaseConnection.getConnection(); // Obtener la conexión
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                prestamos.add(mapPrestamoCompleto(rs));
            }
        }
        return prestamos;
    }

    public List<PrestamoConDetalles> listarPorPeriodo(Date fechaInicio, Date fechaFin) throws SQLException {
        List<PrestamoConDetalles> lista = new ArrayList<>();
        // Unimos Prestamo(p), Cliente(cl) y CopiaLibro(cp) para obtener el Libro(l)
        final String SQL = """
            SELECT 
                p.*,
                cl.nombre AS clienteNombre,
                l.nombre AS libroNombre
            FROM Prestamo p
            JOIN Cliente cl ON p.idCliente = cl.id
            JOIN CopiaLibro cp ON p.idCopia = cp.id
            JOIN Libro l ON cp.idLibro = l.id
            WHERE p.fecha_prestamo BETWEEN ? AND DATEADD(day, 1, ?) -- Rango de fechas
            ORDER BY p.fecha_prestamo DESC
        """; // Usamos DATEADD(day, 1, ?) en SQL Server para incluir todo el último día

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            // Convertir java.util.Date a java.sql.Timestamp para mejor precisión con DATETIME/TIMESTAMP
            ps.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fechaFin.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // 1. Mapear el Prestamo base
                    Prestamo prestamoBase = mapPrestamoCompleto(rs);

                    // 2. Mapear los detalles del JOIN
                    String clienteNombre = rs.getString("clienteNombre");
                    String libroNombre = rs.getString("libroNombre");

                    // 3. Crear y agregar el objeto de detalles
                    lista.add(new PrestamoConDetalles(prestamoBase, clienteNombre, libroNombre));
                }
            }
        }
        return lista;
    }
}