package service.order;

import model.entity.CartItem;

import java.util.List;

/**
 * A dedicated service for performing all order-related calculations.
 * This includes subtotal, taxes, and final totals, ensuring that business
 * logic for calculations is centralized.
 */
public class TotalsCalculatorService {

    public static final double TAX_RATE = 0.07;

    /**
     * Calculates the subtotal of all items in the cart.
     * @param cartItems The list of items in the cart.
     * @return The calculated subtotal in cents.
     */
    public int calculateSubtotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> item.getItem().getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Calculates the tax amount based on the subtotal after discounts.
     * @param subtotalAfterDiscount The subtotal minus any applicable discounts.
     * @return The calculated tax amount in cents.
     */
    public double calculateTax(double subtotalAfterDiscount) {
        return subtotalAfterDiscount * TAX_RATE;
    }

    /**
     * Calculates the final total of the order.
     * @param subtotal The initial subtotal.
     * @param discountValue The value of the discount applied.
     * @return The final total amount.
     */
    public double calculateFinalTotal(int subtotal, int discountValue) {
        double subtotalAfterDiscount = subtotal - discountValue;
        double tax = calculateTax(subtotalAfterDiscount);
        return subtotalAfterDiscount + tax;
    }
}