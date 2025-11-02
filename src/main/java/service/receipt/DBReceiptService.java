package service.receipt;

import model.entity.ReceiptInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repo.repository.ReceiptRepository;
import service.auth.SessionManager;

import java.util.List;

/**
 * Service layer for receipts. Orchestrates saving and retrieving receipt data
 * by using the ReceiptRepository and applying business rules based on user roles.
 */
public class DBReceiptService implements ReceiptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBReceiptService.class);
    private final ReceiptRepository receiptRepository;

    /**
     * Constructor for dependency injection.
     * @param receiptRepository The repository for accessing receipt data.
     */
    public DBReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public void saveReceipt(String customerName, int employeeId, byte[] fileData) throws Exception {
        try {
            receiptRepository.save(customerName, employeeId, fileData);
        } catch (Exception e) {
            LOGGER.error("Failed to save receipt.", e);
            throw new Exception("Failed to save receipt due to a system error.", e);
        }
    }

    @Override
    public List<ReceiptInfo> getReceiptHistory() throws Exception {
        try {
            SessionManager session = SessionManager.getInstance();
            // Business logic: decide which repository method to call based on user role.
            if (session.isManager()) {
                return receiptRepository.findRecentReceiptsForManager();
            } else {
                return receiptRepository.findRecentReceiptsForEmployee(session.getLoggedInEmployeeId());
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching receipt history.", e);
            throw new Exception("Failed to retrieve receipt history.", e);
        }
    }
}