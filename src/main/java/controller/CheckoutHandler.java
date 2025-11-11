package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.CartItem;
import model.entity.Discount;
import service.auth.SessionManager;
import service.cart.Cart;
import service.discount.DiscountCalculationService;
import service.menu.Catalog;
import service.order.TotalsCalculatorService;
import service.receipt.ReceiptBuilderService;
import service.receipt.ReceiptService;
import util.CurrencyFormatter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Handles the entire multi-step checkout process.
 */
public class CheckoutHandler {

    private final Cart cartService;
    private final Catalog menuCatalogService;
    private final ReceiptService receiptService;
    private final ReceiptBuilderService receiptBuilderService;
    private final DiscountCalculationService discountCalculationService;
    private final TotalsCalculatorService totalsCalculatorService;
    private final Runnable onCheckoutComplete;

    // UI components from the main controller
    private final Button checkoutButton;
    private final ComboBox<Discount> discountComboBox;
    private final TextField otherDiscountField;
    private final CheckBox otherDiscountPercentageCheckBox;
    private final TextArea observationsTextArea;

    public CheckoutHandler(Cart cartService, Catalog menuCatalogService, ReceiptService receiptService, ReceiptBuilderService receiptBuilderService, DiscountCalculationService discountCalculationService, TotalsCalculatorService totalsCalculatorService, Runnable onCheckoutComplete, Button checkoutButton, ComboBox<Discount> discountComboBox, TextField otherDiscountField, CheckBox otherDiscountPercentageCheckBox, TextArea observationsTextArea) {
        this.cartService = cartService;
        this.menuCatalogService = menuCatalogService;
        this.receiptService = receiptService;
        this.receiptBuilderService = receiptBuilderService;
        this.discountCalculationService = discountCalculationService;
        this.totalsCalculatorService = totalsCalculatorService;
        this.onCheckoutComplete = onCheckoutComplete;
        this.checkoutButton = checkoutButton;
        this.discountComboBox = discountComboBox;
        this.otherDiscountField = otherDiscountField;
        this.otherDiscountPercentageCheckBox = otherDiscountPercentageCheckBox;
        this.observationsTextArea = observationsTextArea;
    }

    public void startCheckout() {
        if (cartService.getCartItems().isEmpty()) {
            showError("The cart is empty.");
            return;
        }
        checkoutButton.setDisable(true);

        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Customer Name");
        nameDialog.setHeaderText("Please enter the customer's name:");
        nameDialog.setContentText("Name:");
        Optional<String> nameResult = nameDialog.showAndWait();

        nameResult.ifPresentOrElse(customerName -> {
            if (customerName.trim().isEmpty()) {
                showError("Customer name cannot be empty.");
                checkoutButton.setDisable(false);
                return;
            }
            promptForPaymentMethod(customerName);
        }, () -> checkoutButton.setDisable(false));
    }

    private void promptForPaymentMethod(String customerName) {
        List<String> paymentTypes = Arrays.asList("Cash", "Credit Card", "Debit Card", "E-transfer");
        ChoiceDialog<String> paymentDialog = new ChoiceDialog<>(paymentTypes.getFirst(), paymentTypes);
        paymentDialog.setTitle("Payment Method");
        paymentDialog.setHeaderText("Select a payment method:");
        paymentDialog.setContentText("Method:");
        Optional<String> paymentResult = paymentDialog.showAndWait();

        paymentResult.ifPresentOrElse(paymentType -> {
            switch (paymentType) {
                case "Cash":
                    handleCashPayment(customerName);
                    break;
                case "E-transfer":
                    handleETransferPayment(customerName);
                    break;
                case "Credit Card":
                case "Debit Card":
                    handleCardPayment(customerName, paymentType);
                    break;
            }
        }, () -> checkoutButton.setDisable(false));
    }

    private void handleCardPayment(String customerName, String paymentType) {
        Stage loadingStage = new Stage();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setTitle("Processing Payment");
        VBox content = new VBox(20, new Label("Connecting to payment gateway..."), new ProgressIndicator());
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        loadingStage.setScene(new Scene(content));

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(3000);
                return null;
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            loadingStage.close();
            processFinalCheckout(customerName, paymentType, 0, 0);
        }));
        task.setOnFailed(e -> Platform.runLater(() -> {
            loadingStage.close();
            showError("Payment processing failed.");
            checkoutButton.setDisable(false);
        }));

        new Thread(task).start();
        loadingStage.show();
    }

    private void handleCashPayment(String customerName) {
        int subtotal = totalsCalculatorService.calculateSubtotal(cartService.getCartItems());
        int discount = discountCalculationService.calculateDiscount(subtotal, discountComboBox.getSelectionModel().getSelectedItem(), otherDiscountField.getText(), otherDiscountPercentageCheckBox.isSelected());
        int total = totalsCalculatorService.calculateFinalTotal(subtotal, discount);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cash Payment");
        dialog.setHeaderText("Total is " + CurrencyFormatter.formatCents(total) + ".\nEnter amount tendered:");
        dialog.setContentText("Amount:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresentOrElse(amountTenderedStr -> {
            try {
                int amountTendered = Integer.parseInt(amountTenderedStr.replaceAll("[^0-9]", ""));
                if (amountTendered < total) {
                    showError("Amount tendered is less than the total.");
                    checkoutButton.setDisable(false);
                    return;
                }
                int change = amountTendered - total;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Payment successful!\nChange due: " + CurrencyFormatter.formatCents(change));
                alert.setTitle("Payment Complete");
                alert.setHeaderText(null);
                alert.showAndWait();
                processFinalCheckout(customerName, "Cash", amountTendered, change);
            } catch (NumberFormatException e) {
                showError("Invalid amount entered.");
                checkoutButton.setDisable(false);
            }
        }, () -> checkoutButton.setDisable(false));
    }

    private void handleETransferPayment(String customerName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("E-transfer Payment");
        alert.setHeaderText("Please scan the QR code to complete the payment.");

        try {
            ImageView qrCodeView = new ImageView(new Image("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Example"));
            alert.setGraphic(qrCodeView);
        } catch (Exception e) {
            alert.setContentText("Could not load QR code image.");
        }

        alert.showAndWait();
        processFinalCheckout(customerName, "E-transfer", 0, 0);
    }

    private void processFinalCheckout(String customerName, String paymentType, int amountTendered, int change) {
        try {
            int subtotal = totalsCalculatorService.calculateSubtotal(cartService.getCartItems());
            Discount selectedDiscount = discountComboBox.getSelectionModel().getSelectedItem();
            int discountValue = discountCalculationService.calculateDiscount(subtotal, selectedDiscount, otherDiscountField.getText(), otherDiscountPercentageCheckBox.isSelected());
            String receiptContent = receiptBuilderService.buildReceiptContent(customerName, SessionManager.getInstance().getLoggedInEmployeeName(), cartService.getCartItems(), selectedDiscount, discountValue, observationsTextArea.getText(), paymentType, amountTendered, change);

            for (CartItem item : cartService.getCartItems()) {
                menuCatalogService.updateItemInventory(item.getItem().getId(), item.getQuantity());
            }

            receiptService.saveReceipt(customerName, SessionManager.getInstance().getLoggedInEmployeeId(), receiptContent.getBytes(StandardCharsets.UTF_8));

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Checkout Complete");
            successAlert.setHeaderText("Receipt");
            TextArea receiptArea = new TextArea(receiptContent);
            receiptArea.setEditable(false);
            receiptArea.setFont(Font.font("Monospaced", 12));
            receiptArea.setWrapText(false);
            receiptArea.setPrefSize(450, 400);
            successAlert.getDialogPane().setContent(receiptArea);
            successAlert.showAndWait();

            if (onCheckoutComplete != null) {
                onCheckoutComplete.run();
            }

        } catch (Exception e) {
            showError("Error during final checkout: " + e.getMessage() + "\n\nThe order was not completed. Please check inventory or system logs.");
        } finally {
            checkoutButton.setDisable(false);
        }
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }
}