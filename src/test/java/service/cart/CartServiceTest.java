package service.cart;

import model.entity.CartItem;
import model.entity.Item;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceTest {

    private Cart cartService;
    private Item item1;
    private Item item2;

    @BeforeAll
    void printHeader() {
        System.out.println("====================================================================");
        System.out.println("FILE TESTING: CartServiceTest.java");
        System.out.println("PURPOSE: Tests the core logic of the shopping cart service.");
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
        cartService = new CartService();
        item1 = new Item(1, "Coffee", 250, 10);
        item2 = new Item(2, "Cake", 450, 5);
    }

    @Test
    @DisplayName("1. testAddItem()")
    void testAddItem() {
        // Add a new item
        cartService.addItem(item1, 2);
        assertEquals(1, cartService.getCartItems().size());
        assertEquals(2, cartService.getCartItems().get(0).getQuantity());

        // Add more of the same item
        cartService.addItem(item1, 3);
        // Assert that the list still contains only one item
        assertEquals(1, cartService.getCartItems().size(), "Adding an existing item should not create a new cart entry.");
        // Assert that the quantity of that one item has been updated to 5
        assertEquals(5, cartService.getCartItems().get(0).getQuantity(), "Quantity should be summed when adding an existing item.");

        // Add a different item
        cartService.addItem(item2, 1);
        assertEquals(2, cartService.getCartItems().size(), "Adding a different item should increase the cart size.");
    }

    @Test
    @DisplayName("2. testRemoveItem()")
    void testRemoveItem() {
        cartService.addItem(item1, 5);
        CartItem cartItem = cartService.getCartItems().get(0);

        cartService.removeItem(cartItem);
        assertTrue(cartService.getCartItems().isEmpty());
    }

    @Test
    @DisplayName("3. testClearCart()")
    void testClearCart() {
        cartService.addItem(item1, 2);
        cartService.addItem(item2, 1);
        assertFalse(cartService.getCartItems().isEmpty());

        cartService.clearCart();
        assertTrue(cartService.getCartItems().isEmpty());
    }
}