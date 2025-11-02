package service.receipt;

import model.entity.ReceiptInfo;

import java.util.List;

/**
 * Defines the contract for a service that handles receipt persistence and retrieval.
 */
public interface ReceiptService {

    /**
     * Saves a generated receipt to the data store.
     *
     * @param customerName The name of the customer.
     * @param employeeId The ID of the employee who created the receipt.
     * @param fileData The byte array representation of the receipt content.
     * @throws Exception if there is an error during the save operation.
     */
    void saveReceipt(String customerName, int employeeId, byte[] fileData) throws Exception;

    /**
     * Fetches receipt history from the data store based on the current user's role.
     *
     * @return A list of {@link ReceiptInfo} objects.
     * @throws Exception if a database error occurs.
     */
    List<ReceiptInfo> getReceiptHistory() throws Exception;
}