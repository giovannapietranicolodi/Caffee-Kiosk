package service.discount;

import model.entity.Discount;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiscountCalculationServiceTest {

    private DiscountCalculationService service;

    @BeforeAll
    void printHeader() {
        System.out.println("====================================================================");
        System.out.println("FILE TESTING: DiscountCalculationServiceTest.java");
        System.out.println("PURPOSE: Tests the core logic for calculating discounts based on various rules.");
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
        service = new DiscountCalculationService();
    }

    @Test
    @DisplayName("1. testNoDiscount()")
    void testNoDiscount() {
        Discount noneDiscount = new Discount(0, "None", 0, false, true);
        int discountValue = service.calculateDiscount(1000, noneDiscount, "", false);
        assertEquals(0, discountValue);
    }

    @Test
    @DisplayName("2. testPredefinedPercentageDiscount()")
    void testPredefinedPercentageDiscount() {
        Discount tenPercentOff = new Discount(1, "10% Off", 10, true, true);
        int discountValue = service.calculateDiscount(1000, tenPercentOff, "", false);
        assertEquals(100, discountValue);
    }

    @Test
    @DisplayName("3. testPredefinedFixedDiscount()")
    void testPredefinedFixedDiscount() {
        Discount fiveDollarsOff = new Discount(2, "$5 Off", 500, false, true);
        int discountValue = service.calculateDiscount(1000, fiveDollarsOff, "", false);
        assertEquals(500, discountValue);
    }

    @Test
    @DisplayName("4. testOtherPercentageDiscount()")
    void testOtherPercentageDiscount() {
        Discount otherDiscount = new Discount(-1, "Other", 0, false, true);
        int discountValue = service.calculateDiscount(2000, otherDiscount, "15", true);
        assertEquals(300, discountValue);
    }

    @Test
    @DisplayName("5. testOtherFixedDiscount()")
    void testOtherFixedDiscount() {
        Discount otherDiscount = new Discount(-1, "Other", 0, false, true);
        int discountValue = service.calculateDiscount(2000, otherDiscount, "250", false);
        assertEquals(250, discountValue);
    }

    @Test
    @DisplayName("6. testInvalidOtherDiscount()")
    void testInvalidOtherDiscount() {
        Discount otherDiscount = new Discount(-1, "Other", 0, false, true);
        int discountValue = service.calculateDiscount(2000, otherDiscount, "abc", true);
        assertEquals(0, discountValue);
    }
}