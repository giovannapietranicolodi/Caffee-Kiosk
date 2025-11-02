package repo.repository;

import model.entity.Category;
import repo.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for handling category-related database operations.
 */
public class CategoryRepository {

    /**
     * Fetches all categories from the database.
     *
     * @return A list of all Category objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT id, description FROM categories ORDER BY id";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DBConnection.dbConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                categories.add(new Category(id, description));
            }
        }
        return categories;
    }
}