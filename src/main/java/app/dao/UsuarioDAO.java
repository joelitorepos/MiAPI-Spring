package app.dao;

import app.core.PasswordUtil;
import app.model.Usuario;
import app.model.UsuarioConRol;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime; // Necesario para el modelo Usuario
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {
    private final DataSource dataSource;

    public UsuarioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // --- Helper: Mapea un ResultSet a un objeto Usuario (incluyendo fechas)
    private Usuario mapUsuario(ResultSet rs) throws SQLException {
        // SQL Server DATETIME se lee como Timestamp, que convertimos a LocalDateTime
        Timestamp createdTs = rs.getTimestamp("created_at");
        Timestamp updatedTs = rs.getTimestamp("updated_at");

        return new Usuario(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("nombre"),
                rs.getString("password"), // El alias 'password' como cadena vacía se usa en buscarPorId
                rs.getInt("idRol"),
                rs.getInt("estado"),
                createdTs != null ? createdTs.toLocalDateTime() : null,
                updatedTs != null ? updatedTs.toLocalDateTime() : null
        );
    }

    // Método para buscar un usuario por su nombre de usuario
    // MODIFICADO: Incluye created_at y updated_at.
    public Usuario findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, nombre, password, idRol, estado, created_at, updated_at FROM Usuario WHERE username = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs); // Usamos el nuevo mapeador
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
            throw e;
        }
        return null;
    }

    // INSERT: crea un usuario y devuelve el id generado
    // No necesita cambio en SQL ya que la BD establece created_at/updated_at con GETDATE()
    public int crearUsuario(String username, String plainPassword, String nombre, int idRol) throws SQLException {
        String sql = "INSERT INTO Usuario (username, password, nombre, idRol, estado) VALUES (?,?,?,?,1)";
        String hash = PasswordUtil.hash(plainPassword);

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            ps.setString(3, nombre);
            ps.setInt(4, idRol);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // Listar todos los usuarios con nombre de rol (para vista)
    // Se mantiene igual, ya que UsuarioConRol solo se usa para la vista.
    public List<UsuarioConRol> listarConRol() throws SQLException {
        String sql = """
                SELECT u.id, u.username, u.nombre, r.nombre AS rolNombre, u.estado
                FROM Usuario u
                JOIN Rol r ON r.id = u.idRol
                ORDER BY u.id DESC
                """;
        List<UsuarioConRol> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new UsuarioConRol(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nombre"),
                        rs.getString("rolNombre"),
                        rs.getInt("estado")
                ));
            }
        }
        return lista;
    }

    // Buscar por ID (sin password)
    // MODIFICADO: Incluye created_at, updated_at y un alias para el campo password.
    public Usuario buscarPorId(int id) throws SQLException {
        // Se añade 'created_at, updated_at' y se hace un alias: '' as password para poder reutilizar mapUsuario.
        String sql = "SELECT id, username, nombre, idRol, estado, created_at, updated_at, '' as password FROM Usuario WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs); // Usamos el nuevo mapeador
                }
            }
        }
        return null;
    }

    // Actualizar usuario (con opción a cambiar password si se proporciona)
    // MODIFICADO: Se agregó 'updated_at = GETDATE()' a la sentencia SQL.
    public boolean actualizar(Usuario u, String newPlainPassword) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE Usuario SET nombre = ?, idRol = ?, estado = ?, updated_at = GETDATE()");
        if (newPlainPassword != null && !newPlainPassword.isEmpty()) {
            sql.append(", password = ?");
        }
        sql.append(" WHERE id = ?");

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setString(1, u.getNombre());
            ps.setInt(2, u.getIdRol());
            ps.setInt(3, u.getEstado());

            int paramIndex = 4;
            if (newPlainPassword != null && !newPlainPassword.isEmpty()) {
                String hash = PasswordUtil.hash(newPlainPassword);
                ps.setString(paramIndex++, hash);
            }

            ps.setInt(paramIndex, u.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Método para validar el login
    // No requiere cambios, ya que llama a findByUsername.
    public Usuario validarLogin(String username, String password) throws SQLException {
        Usuario usuario = findByUsername(username);

        // El usuario debe existir y su estado debe ser 1 (activo)
        if (usuario != null && usuario.getEstado() == 1) {
            // Se utiliza PasswordUtil para verificar la contraseña encriptada
            if (PasswordUtil.verify(password, usuario.getPassword())) {
                return usuario; // Login exitoso
            }
        }

        return null; // Login fallido
    }

    // Eliminar usuario
    // No necesita cambios.
    public int eliminarUsuario(String username) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE username = ?"; // Eliminación física

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate();
        }
    }

    // En app.dao.UsuarioDAO (añadir al final de la clase)

    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id, username, nombre, idRol, estado, created_at, updated_at, '' as password " +
                "FROM Usuario ORDER BY id DESC";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapUsuario(rs));
            }
        }
        return lista;
    }
}