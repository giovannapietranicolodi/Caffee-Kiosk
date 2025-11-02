package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.ReceiptInfo;
import service.receipt.ReceiptService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for the Receipt History window (history-view.fxml).
 * Manages fetching and displaying past receipts.
 */
public class HistoryController {

    @FXML
    private TableView<ReceiptInfo> historyTable;
    @FXML
    private TableColumn<ReceiptInfo, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<ReceiptInfo, String> employeeColumn;
    @FXML
    private TableColumn<ReceiptInfo, String> customerColumn;
    @FXML
    private TextArea receiptContentArea;

    private final ReceiptService receiptService;

    /**
     * Constructor for dependency injection.
     * @param receiptService The service used to fetch receipt data.
     */
    public HistoryController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    /**
     * Initializes the controller, setting up table columns and listeners.
     */
    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        // Add a listener to show receipt content on selection
        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                receiptContentArea.setText(new String(newSelection.getFileData(), StandardCharsets.UTF_8));
            } else {
                receiptContentArea.clear();
            }
        });
    }

    /**
     * Fetches receipt history, loads the FXML view, and displays it in a new window.
     */
    public void showHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/history/history-view.fxml"));
            loader.setControllerFactory(param -> this);
            Parent root = loader.load();

            // Fetch data and populate the table *after* the FXML is loaded
            List<ReceiptInfo> history = receiptService.getReceiptHistory();
            historyTable.setItems(FXCollections.observableArrayList(history));

            Stage historyStage = new Stage();
            historyStage.setTitle("Receipt History");
            historyStage.setScene(new Scene(root));
            historyStage.initModality(Modality.APPLICATION_MODAL);
            historyStage.show();

        } catch (IOException e) {
            showError("Failed to load the history view: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Error fetching receipt history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}