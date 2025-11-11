package service.discount;

import model.entity.Discount;

/**
 * A service class dedicated to the business logic of calculating discounts.
 * This decouples the calculation logic from the controller.
 */
public class DiscountCalculationService {

    /**
     * Calculates the total discount amount based on the selected discount type and subtotal.
     * It ensures the discount never exceeds the subtotal.
     *
     * @param subtotal The order subtotal before any discounts.
     * @param selectedDiscount The {@link Discount} object selected by the user.
     * @param otherAmountStr The manually entered discount amount as a string.
     * @param isOtherPercentage True if the manual amount is a percentage, false if it is a fixed value.
     * @return The calculated and validated discount value in cents.
     */
    public int calculateDiscount(int subtotal, Discount selectedDiscount, String otherAmountStr, boolean isOtherPercentage) {
        int discountValue = 0;

        if (selectedDiscount == null || "None".equalsIgnoreCase(selectedDiscount.getName())) {
            return 0;
        }

        if ("Other".equalsIgnoreCase(selectedDiscount.getName())) {
            if (otherAmountStr != null && !otherAmountStr.trim().isEmpty()) {
                try {
                    int otherAmount = Integer.parseInt(otherAmountStr);
                    if (isOtherPercentage) {
                        discountValue = (subtotal * otherAmount) / 100;
                    } else {
                        discountValue = otherAmount;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid numbers, discount remains 0
                }
            }
        } else { // Pre-defined discount
            if (selectedDiscount.isPercentage()) {
                discountValue = (subtotal * selectedDiscount.getAmount()) / 100;
            } else {
                discountValue = selectedDiscount.getAmount();
            }
        }

        // Add validation to ensure discount is not negative and not greater than the subtotal
        discountValue = Math.max(0, discountValue);
        discountValue = Math.min(discountValue, subtotal);

        return discountValue;
    }
}