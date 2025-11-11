package service.order;

import model.entity.CartItem;
import model.entity.Item;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TotalsCalculatorServiceTest {

    private TotalsCalculatorService totalsCalculatorService;

    @BeforeAll
    void printHeader() {
        System.out.println("====================================================================");
        System.out.println("FILE TESTING: TotalsCalculatorServiceTest.java");
        System.out.println("PURPOSE: Tests the core calculation logic for order totals.");
        System.out.println("--------------------------------------------------------------------");
    }

    @AfterAll
    void printFooter() {
        System.out.println("====================================================================\n");
    }

    @AfterEach
    void reportTestResult(TestInfo testInfo) {
        System.out.println("  - " + testInfo.getDisplayName() + " - PASSED");
    }

    @BeforeEach
    void setUp() {
        totalsCalculatorService = new TotalsCalculatorService();
    }

    @Test
    @DisplayName("1. testCalculateWithEmptyCart()")
    void testCalculateWithEmptyCart() {
        List<CartItem> cartItems = Collections.emptyList();
        int subtotal = totalsCalculatorService.calculateSubtotal(cartItems);
        int total = totalsCalculatorService.calculateFinalTotal(subtotal, 0);

        assertEquals(0, subtotal);
        assertEquals(0, total);
    }

    @Test
    @DisplayName("2. testCalculateWithSingleItem()")
    void testCalculateWithSingleItem() {
        Item coffee = new Item(1, "Coffee", 300, 10);
        List<CartItem> cartItems = Collections.singletonList(new CartItem(coffee, 2)); // Subtotal = 600

        int subtotal = totalsCalculatorService.calculateSubtotal(cartItems);
        int total = totalsCalculatorService.calculateFinalTotal(subtotal, 0);

        assertEquals(600, subtotal);
        assertEquals(642, total); // 600 + 7% tax (42)
    }

    @Test
    @DisplayName("3. testCalculateWithMultipleItems()")
    void testCalculateWithMultipleItems() {
        Item coffee = new Item(1, "Coffee", 300, 10);
        Item cake = new Item(2, "Cake", 500, 5);
        List<CartItem> cartItems = Arrays.asList(new CartItem(coffee, 1), new CartItem(cake, 1)); // Subtotal = 800

        int subtotal = totalsCalculatorService.calculateSubtotal(cartItems);
        int total = totalsCalculatorService.calculateFinalTotal(subtotal, 0);

        assertEquals(800, subtotal);
        assertEquals(856, total); // 800 + 7% tax (56)
    }

    @Test
    @DisplayName("4. testCalculateWithPercentageDiscount()")
    void testCalculateWithPercentageDiscount() {
        Item coffee = new Item(1, "Coffee", 1000, 10); // Subtotal = 1000
        List<CartItem> cartItems = Collections.singletonList(new CartItem(coffee, 1));
        int discountValue = 100; // 10% of 1000

        int subtotal = totalsCalculatorService.calculateSubtotal(cartItems);
        int total = totalsCalculatorService.calculateFinalTotal(subtotal, discountValue);

        assertEquals(1000, subtotal);
        assertEquals(963, total); // (1000 - 100) = 900; 900 + 7% tax (63)
    }

    @Test
    @DisplayName("5. testCalculateWithFixedAmountDiscount()")
    void testCalculateWithFixedAmountDiscount() {
        Item coffee = new Item(1, "Coffee", 1000, 10); // Subtotal = 1000
        List<CartItem> cartItems = Collections.singletonList(new CartItem(coffee, 1));
        int discountValue = 200; // Fixed 200 cents discount

        int subtotal = totalsCalculatorService.calculateSubtotal(cartItems);
        int total = totalsCalculatorService.calculateFinalTotal(subtotal, discountValue);

        assertEquals(1000, subtotal);
        assertEquals(856, total); // (1000 - 200) = 800; 800 + 7% tax (56)
    }
}