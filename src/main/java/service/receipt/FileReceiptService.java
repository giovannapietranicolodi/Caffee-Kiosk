package service.receipt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.entity.ReceiptInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An implementation of the ReceiptService for the InternalFile data source.
 * It saves receipts to a log and reads a mock history from a JSON file.
 */
public class FileReceiptService implements ReceiptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReceiptService.class);
    private final List<ReceiptInfo> receiptHistory;

    public FileReceiptService() {
        this.receiptHistory = loadFromFile();
    }

    @Override
    public void saveReceipt(String customerName, int employeeId, byte[] fileData) {
        LOGGER.info("File-based service: Simulating receipt save for customer: {}", customerName);
        // In a file-based context, we only log the action, no persistence.
    }

    @Override
    public List<ReceiptInfo> getReceiptHistory() {
        LOGGER.info("File-based service: Returning {} receipts from JSON.", receiptHistory.size());
        return receiptHistory;
    }

    private List<ReceiptInfo> loadFromFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/receipts.json")) {
            if (inputStream == null) {
                LOGGER.warn("Could not find '/data/receipts.json'. History will be empty.");
                return Collections.emptyList();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Needed to parse LocalDateTime

            List<ReceiptData> loadedData = mapper.readValue(inputStream, new TypeReference<List<ReceiptData>>() {});
            
            // Convert DTOs to the domain model
            return loadedData.stream()
                    .map(data -> new ReceiptInfo(
                            data.getEmployeeName(),
                            data.getCustomerName(),
                            data.getUploadDate(),
                            data.getFileData().getBytes(StandardCharsets.UTF_8) // Convert String to byte[]
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LOGGER.error("Failed to read or parse receipts.json. History will be empty.", e);
            return Collections.emptyList();
        }
    }

    /**
     * A private DTO class for deserializing receipt data from JSON.
     * This is needed because the `fileData` in the JSON is a String, not a byte array.
     */
    private static class ReceiptData {
        private String employeeName;
        private String customerName;
        private LocalDateTime uploadDate;
        private String fileData;

        @JsonCreator
        public ReceiptData(@JsonProperty("employeeName") String employeeName, 
                           @JsonProperty("customerName") String customerName, 
                           @JsonProperty("uploadDate") LocalDateTime uploadDate, 
                           @JsonProperty("fileData") String fileData) {
            this.employeeName = employeeName;
            this.customerName = customerName;
            this.uploadDate = uploadDate;
            this.fileData = fileData;
        }

        // Getters
        public String getEmployeeName() { return employeeName; }
        public String getCustomerName() { return customerName; }
        public LocalDateTime getUploadDate() { return uploadDate; }
        public String getFileData() { return fileData; }
    }
}