package repo.repository;

import model.entity.Item;
import repo.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for handling menu item-related database operations.
 */
public class MenuRepository {

    public List<Item> findItemsByCategoryId(int categoryId) throws SQLException {
        String sql = "SELECT id, name, price, inventory FROM items WHERE category_id = ? AND is_available = TRUE ORDER BY name";
        List<Item> items = new ArrayList<>();

        try (Connection connection = DBConnection.dbConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRowToItem(rs));
                }
            }
        }
        return items;
    }

    public Optional<Item> findItemById(int itemId) throws SQLException {
        String sql = "SELECT id, name, price, inventory FROM items WHERE id = ?";
        try (Connection conn = DBConnection.dbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToItem(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Atomically updates the inventory for a given item, ensuring stock does not go below zero.
     *
     * @param itemId The ID of the item to update.
     * @param quantity The quantity to subtract from the inventory.
     * @throws SQLException if a database access error occurs.
     * @throws IllegalStateException if the update fails (e.g., insufficient stock).
     */
    public void updateInventory(int itemId, int quantity) throws SQLException {
        String sql = "UPDATE items SET inventory = inventory - ? WHERE id = ? AND inventory >= ?";
        try (Connection conn = DBConnection.dbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantity); // Ensure stock is sufficient

            int rowsAffected = pstmt.executeUpdate();

            // If no rows were affected, it means the condition (inventory >= ?) failed.
            if (rowsAffected == 0) {
                throw new IllegalStateException("Update failed, likely due to insufficient stock for item ID: " + itemId);
            }
        }
    }

    private Item mapRowToItem(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getInt("inventory")
        );
    }
}