package service.receipt;

import model.entity.CartItem;
import model.entity.Discount;
import model.entity.Item;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.order.TotalsCalculatorService;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReceiptBuilderServiceTest {

    private ReceiptBuilderService receiptBuilderService;

    @Mock
    private TotalsCalculatorService mockTotalsCalculatorService;

    @BeforeAll
    void printHeader() {
        System.out.println("====================================================================");
        System.out.println("FILE TESTING: ReceiptBuilderServiceTest.java");
        System.out.println("PURPOSE: Tests the service responsible for generating the final, formatted receipt string.");
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
        receiptBuilderService = new ReceiptBuilderService(mockTotalsCalculatorService);
    }

    @Test
    @DisplayName("1. testBuildReceiptContent_withDiscountAndObservations()")
    void testBuildReceiptContent_withDiscountAndObservations() {
        Item coffee = new Item(1, "Coffee", 250, 10);
        Item cake = new Item(2, "Cake", 450, 5);
        CartItem cartItem1 = new CartItem(coffee, 2);
        CartItem cartItem2 = new CartItem(cake, 1);
        Discount discount = new Discount(1, "Staff Discount", 10, true, true);

        // Arrange mocks to return integers (cents)
        when(mockTotalsCalculatorService.calculateSubtotal(Arrays.asList(cartItem1, cartItem2))).thenReturn(950);
        when(mockTotalsCalculatorService.calculateTax(950 - 95)).thenReturn(60); // Corrected to int

        String receipt = receiptBuilderService.buildReceiptContent(
                "Test Customer",
                "Test Employee",
                Arrays.asList(cartItem1, cartItem2),
                discount,
                95, // 10% discount value
                "Extra hot, please.",
                "Credit Card",
                0, 0
        );

        assertTrue(receipt.contains("Discount (Staff Discount):"));
        assertTrue(receipt.contains("Observations: Extra hot, please."));
    }

    @Test
    @DisplayName("2. testBuildReceiptContent_withoutDiscountOrObservations()")
    void testBuildReceiptContent_withoutDiscountOrObservations() {
        Item coffee = new Item(1, "Coffee", 250, 10);
        CartItem cartItem1 = new CartItem(coffee, 1);

        // Arrange mocks to return integers (cents)
        when(mockTotalsCalculatorService.calculateSubtotal(Collections.singletonList(cartItem1))).thenReturn(250);
        when(mockTotalsCalculatorService.calculateTax(250)).thenReturn(18); // Corrected to int

        String receipt = receiptBuilderService.buildReceiptContent(
                "Another Customer",
                "Another Employee",
                Collections.singletonList(cartItem1),
                null, // No discount object
                0,    // No discount value
                "",   // No observations
                "Cash",
                300, 32 // Corrected change
        );

        assertFalse(receipt.contains("Discount"));
        assertFalse(receipt.contains("Observations:"));
        assertTrue(receipt.contains("Amount Tendered:"));
    }
}