package app.dao;

import app.db.DatabaseConnection;
import app.model.Auditoria;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuditoriaDAO {
    private final DataSource dataSource;

    public AuditoriaDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Helper: mapea un ResultSet a un objeto Auditoria
    private Auditoria mapAuditoria(ResultSet rs) throws SQLException {
        return new Auditoria(
                rs.getInt("id"),
                rs.getInt("idUsuario"),
                rs.getTimestamp("fechaHora") != null ? rs.getTimestamp("fechaHora").toLocalDateTime() : null,
                rs.getString("modulo"),
                rs.getString("accion"),
                rs.getString("detalle")
        );
    }

    public Auditoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, idUsuario, fechaHora, modulo, accion, detalle FROM Auditoria WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAuditoria(rs);
                }
            }
        }
        return null;
    }

    public List<Auditoria> listarConFiltros(Integer idUsuario, String modulo, String accion,
                                            LocalDateTime fechaInicio, LocalDateTime fechaFin) throws SQLException {
        List<Auditoria> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id, idUsuario, fechaHora, modulo, accion, detalle FROM Auditoria WHERE 1=1"
        );
        List<Object> parametros = new ArrayList<>();

        // 1. Aplicar filtro de ID de Usuario
        if (idUsuario != null) {
            sql.append(" AND idUsuario = ?");
            parametros.add(idUsuario);
        }

        // 2. Aplicar filtro de Módulo
        if (modulo != null && !modulo.trim().isEmpty()) {
            sql.append(" AND modulo = ?");
            parametros.add(modulo);
        }

        // 3. Aplicar filtro de Acción
        if (accion != null && !accion.trim().isEmpty()) {
            sql.append(" AND accion = ?");
            parametros.add(accion);
        }

        // 4. Aplicar filtro de Rango de Fechas (fechaHora)
        if (fechaInicio != null) {
            sql.append(" AND fechaHora >= ?");
            parametros.add(Timestamp.valueOf(fechaInicio));
        }
        if (fechaFin != null) {
            // Se usa el final del día (23:59:59.999) si solo se provee una fecha, pero aquí se toma el valor exacto del LocalDateTime.
            // Es mejor que el servicio de frontend ajuste fechaFin a fin de día si el usuario solo ingresa una fecha.
            sql.append(" AND fechaHora <= ?");
            parametros.add(Timestamp.valueOf(fechaFin));
        }

        // Ordenar por fecha y hora más reciente primero
        sql.append(" ORDER BY fechaHora DESC");


        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // Asignar parámetros al PreparedStatement
            int index = 1;
            for (Object param : parametros) {
                if (param instanceof Integer) {
                    ps.setInt(index++, (Integer) param);
                } else if (param instanceof String) {
                    ps.setString(index++, (String) param);
                } else if (param instanceof Timestamp) {
                    ps.setTimestamp(index++, (Timestamp) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapAuditoria(rs));
                }
            }
        }
        return lista;
    }
}