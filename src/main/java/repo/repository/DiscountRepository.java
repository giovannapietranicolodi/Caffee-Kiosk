package repo.repository;

import model.entity.Discount;
import repo.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for handling discount-related database operations.
 */
public class DiscountRepository {

    /**
     * Fetches all active discounts from the database.
     *
     * @return A list of active Discount objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<Discount> findActiveDiscounts() throws SQLException {
        String sql = "SELECT id, name, amount, is_percentage, active FROM discounts WHERE active = TRUE";
        List<Discount> discounts = new ArrayList<>();

        try (Connection connection = DBConnection.dbConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                discounts.add(new Discount(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getBoolean("is_percentage"),
                        rs.getBoolean("active")
                ));
            }
        }
        return discounts;
    }
}