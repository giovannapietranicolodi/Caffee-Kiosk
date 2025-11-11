package util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting currency values.
 */
public class CurrencyFormatter {

    // Using Locale.of() which is the modern, non-deprecated way to create a locale.
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("en", "US"));

    /**
     * Formats an integer value representing cents into a currency string (e.g., 1050 -> $10.50).
     * @param cents The amount in cents.
     * @return A formatted currency string.
     */
    public static String formatCents(int cents) {
        return currencyFormatter.format(cents / 100.0);
    }
}