package repo.repository;

import model.dto.UserSessionInfo;
import repo.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Repository for handling authentication-related database operations.
 */
public class AuthRepository {

    /**
     * Finds a user by their login name (case-insensitively) and retrieves their session information.
     *
     * @param username The login name to search for.
     * @return An Optional containing UserSessionInfo if found, otherwise empty.
     * @throws SQLException if a database access error occurs.
     */
    public Optional<UserSessionInfo> findUserByUsername(String username) throws SQLException {
        // The query uses LOWER() to ensure case-insensitive comparison for the username.
        String sql = "SELECT id, first_name || ' ' || last_name AS name, password AS hashedPassword, is_manager FROM employees WHERE LOWER(login) = ?";

        try (Connection connection = DBConnection.dbConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Normalize the input username to lowercase before querying.
            pstmt.setString(1, username.toLowerCase());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String hashedPassword = rs.getString("hashedPassword");
                    boolean isManager = rs.getBoolean("is_manager");
                    return Optional.of(new UserSessionInfo(id, name, hashedPassword, isManager));
                }
            }
        }
        return Optional.empty();
    }
}