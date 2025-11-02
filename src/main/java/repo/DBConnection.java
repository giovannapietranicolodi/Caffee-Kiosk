package repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Handles the application's database connection.
 */
public class DBConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);
    private static final Properties CONFIG = new Properties();

    // Static block to load the database configuration from app.properties once.
    static {
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("app.properties")) {

            if (input == null) {
                // Use logger for critical configuration errors
                LOGGER.error("CRITICAL ERROR: 'app.properties' file not found in classpath. Database connection will fail.");
            } else {
                CONFIG.load(input);
            }

        } catch (IOException ex) {
            // Use logger for I/O errors during properties loading
            LOGGER.error("CRITICAL ERROR: Could not load app.properties.", ex);
        }
    }

    /**
     * Gets a new database connection using the settings from app.properties.
     * @return A new Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection dbConnection() throws SQLException {
        try {
            String dbUrl = CONFIG.getProperty("db.url");
            String dbUser = CONFIG.getProperty("db.user");
            String dbPassword = CONFIG.getProperty("db.password");

            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            LOGGER.error("Failed to establish database connection.", e);
            // Re-throw the exception to let the calling service handle the connection failure.
            throw e;
        }
    }
}