package app.dao;

import app.db.DatabaseConnection;
import app.model.Rol;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RolDAO {
    private final DataSource dataSource;

    public RolDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Obtener todos los roles activos
    public List<Rol> listar() throws SQLException {
        String sql = "SELECT id, nombre, descripcion, estado FROM Rol WHERE estado = 1 ORDER BY id";
        List<Rol> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Rol(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("estado")
                ));
            }
        }
        return lista;
    }

    public int insertar(Rol rol) throws SQLException {
        String sql = "INSERT INTO Rol (nombre, descripcion, estado) VALUES (?, ?, ?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setInt(3, rol.getEstado()); // Siempre 1 (Activo) al insertar

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Retorna el ID generado
                    }
                }
            }
        }
        return -1;
    }
}