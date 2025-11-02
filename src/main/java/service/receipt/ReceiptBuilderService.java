package service.receipt;

import model.entity.CartItem;
import model.entity.Discount;
import util.CurrencyFormatter;
import service.order.TotalsCalculatorService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A service dedicated to building the final, formatted string representation of a receipt.
 */
public class ReceiptBuilderService {

    private final TotalsCalculatorService totalsCalculatorService;

    public ReceiptBuilderService(TotalsCalculatorService totalsCalculatorService) {
        this.totalsCalculatorService = totalsCalculatorService;
    }

    public String buildReceiptContent(String customerName, String employeeName, List<CartItem> cartItems, Discount discount, int discountValue, String observations, String paymentMethod, int amountTendered, int change) {
        StringBuilder receipt = new StringBuilder();
        int subtotal = totalsCalculatorService.calculateSubtotal(cartItems);
        double subtotalAfterDiscount = subtotal - discountValue;
        double tax = totalsCalculatorService.calculateTax(subtotalAfterDiscount);
        double total = subtotalAfterDiscount + tax;

        receipt.append("\tOOP Caffee\n");
        receipt.append("----------------------------------------------------\n");
        receipt.append(String.format("%-25s %s\n", "Date:", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        receipt.append(String.format("%-25s %s\n", "Served by:", employeeName));
        receipt.append(String.format("%-25s %s\n", "Customer:", customerName));
        receipt.append("----------------------------------------------------\n");

        for (CartItem item : cartItems) {
            String lineItem = String.format("%d x %s", item.getQuantity(), item.getItem().getName());
            String lineTotal = CurrencyFormatter.format(item.getItem().getPrice() * item.getQuantity());
            receipt.append(String.format("%-38s %12s\n", lineItem, lineTotal));
        }

        receipt.append("----------------------------------------------------\n");
        receipt.append(String.format("%-38s %12s\n", "Subtotal:", CurrencyFormatter.format(subtotal)));

        if (discountValue > 0) {
            String discountName = (discount != null && !"Other".equalsIgnoreCase(discount.getName())) ? discount.getName() : "";
            String discountLabel = "Discount" + (!discountName.isEmpty() ? " (" + discountName + ")" : "") + ":";
            receipt.append(String.format("%-38s %12s\n", discountLabel, "-" + CurrencyFormatter.format(discountValue)));
        }

        String taxText = "Tax (" + Math.round(TotalsCalculatorService.TAX_RATE * 100) + "%):";
        receipt.append(String.format("%-38s %12s\n", taxText, CurrencyFormatter.format((int) tax)));

        receipt.append(String.format("%-38s %12s\n", "TOTAL:", CurrencyFormatter.format((int) total)));
        receipt.append("----------------------------------------------------\n");

        if (observations != null && !observations.trim().isEmpty()) {
            receipt.append("Observations: ").append(observations).append("\n");
            receipt.append("----------------------------------------------------\n");
        }

        receipt.append(String.format("%-25s %s\n", "Payment Method:", paymentMethod));
        if ("Cash".equals(paymentMethod)) {
            receipt.append(String.format("%-38s %12s\n", "Amount Tendered:", CurrencyFormatter.format(amountTendered)));
            receipt.append(String.format("%-38s %12s\n", "Change:", CurrencyFormatter.format(change)));
        }
        receipt.append("\n\tThank you for your visit!\n");
        return receipt.toString();
    }
}