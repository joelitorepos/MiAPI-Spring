package app.db;

import org.springframework.stereotype.Component;
import javax.sql.DataSource; // Usa la interfaz estándar de Java SQL
import java.sql.Connection;
import java.sql.SQLException;

// 1. Marca la clase como componente de Spring (Singleton)
@Component
public class DatabaseConnection {

    private static DataSource dataSource;

    public DatabaseConnection(DataSource ds) {
        DatabaseConnection.dataSource = ds;
        System.out.println("DataSource inyectado y listo para el pool de conexiones.");
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("El DataSource no ha sido inicializado por Spring.");
        }
        // Devuelve una conexión "prestada" del pool
        return dataSource.getConnection();
    }
}