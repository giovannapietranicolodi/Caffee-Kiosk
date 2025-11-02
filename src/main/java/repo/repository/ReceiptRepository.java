package repo.repository;

import model.entity.ReceiptInfo;
import repo.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for handling receipt-related database operations.
 */
public class ReceiptRepository {

    /**
     * Saves a receipt record to the database.
     */
    public void save(String customerName, int employeeId, byte[] fileData) throws SQLException {
        String sql = "INSERT INTO receipts_files (customer_name, employee_id, upload_date, file_data) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.dbConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, customerName);
            pstmt.setInt(2, employeeId);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setBytes(4, fileData);

            pstmt.executeUpdate();
        }
    }

    /**
     * Finds all receipts within the last 30 days.
     */
    public List<ReceiptInfo> findRecentReceiptsForManager() throws SQLException {
        String sql = "SELECT e.first_name || ' ' || e.last_name AS employee_name, r.customer_name, r.upload_date, r.file_data " +
                     "FROM receipts_files r JOIN employees e ON r.employee_id = e.id " +
                     "WHERE r.upload_date >= NOW() - INTERVAL '30 days' ORDER BY r.upload_date DESC";
        return findReceipts(sql, -1);
    }

    /**
     * Finds all receipts for a specific employee within the last 7 days.
     */
    public List<ReceiptInfo> findRecentReceiptsForEmployee(int employeeId) throws SQLException {
        String sql = "SELECT e.first_name || ' ' || e.last_name AS employee_name, r.customer_name, r.upload_date, r.file_data " +
                     "FROM receipts_files r JOIN employees e ON r.employee_id = e.id " +
                     "WHERE r.employee_id = ? AND r.upload_date >= NOW() - INTERVAL '7 days' ORDER BY r.upload_date DESC";
        return findReceipts(sql, employeeId);
    }

    /**
     * Generic helper method to execute a receipt search query.
     */
    private List<ReceiptInfo> findReceipts(String sql, int employeeId) throws SQLException {
        List<ReceiptInfo> history = new ArrayList<>();
        try (Connection connection = DBConnection.dbConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            if (employeeId != -1) {
                pstmt.setInt(1, employeeId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    history.add(mapRowToReceiptInfo(rs));
                }
            }
        }
        return history;
    }

    /**
     * Helper method to map a ResultSet row to a ReceiptInfo object.
     */
    private ReceiptInfo mapRowToReceiptInfo(ResultSet rs) throws SQLException {
        String employeeName = rs.getString("employee_name");
        String customerName = rs.getString("customer_name");
        LocalDateTime uploadDate = rs.getTimestamp("upload_date").toLocalDateTime();
        byte[] fileData = rs.getBytes("file_data");
        return new ReceiptInfo(employeeName, customerName, uploadDate, fileData);
    }
}