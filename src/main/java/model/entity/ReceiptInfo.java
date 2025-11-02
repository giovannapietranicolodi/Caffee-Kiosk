package model.entity;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) representing a historical receipt record.
 * This class is used to display receipt information in the history view.
 */
public class ReceiptInfo {
    private final String employeeName;
    private final String customerName;
    private final LocalDateTime uploadDate;
    private final byte[] fileData;

    /**
     * Constructor for creating a ReceiptInfo object.
     * @param employeeName The full name of the employee who processed the sale.
     * @param customerName The name of the customer.
     * @param uploadDate The date and time the receipt was saved.
     * @param fileData The raw byte data of the receipt content.
     */
    public ReceiptInfo(String employeeName, String customerName, LocalDateTime uploadDate, byte[] fileData) {
        this.employeeName = employeeName;
        this.customerName = customerName;
        this.uploadDate = uploadDate;
        this.fileData = fileData;
    }

    // --- Standard Getters ---

    public String getEmployeeName() {
        return employeeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public byte[] getFileData() {
        return fileData;
    }
}