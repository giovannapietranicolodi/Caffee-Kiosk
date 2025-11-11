package service.order;

import model.entity.CartItem;

import java.util.List;

/**
 * A dedicated service for performing all order-related calculations using integers for cents
 * to avoid floating-point inaccuracies.
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
     * @param subtotalAfterDiscount The subtotal minus any applicable discounts, in cents.
     * @return The calculated tax amount in cents, rounded to the nearest cent.
     */
    public int calculateTax(int subtotalAfterDiscount) {
        return (int) Math.round(subtotalAfterDiscount * TAX_RATE);
    }

    /**
     * Calculates the final total of the order.
     * @param subtotal The initial subtotal in cents.
     * @param discountValue The value of the discount applied, in cents.
     * @return The final total amount in cents.
     */
    public int calculateFinalTotal(int subtotal, int discountValue) {
        int subtotalAfterDiscount = subtotal - discountValue;
        int tax = calculateTax(subtotalAfterDiscount);
        return subtotalAfterDiscount + tax;
    }
}