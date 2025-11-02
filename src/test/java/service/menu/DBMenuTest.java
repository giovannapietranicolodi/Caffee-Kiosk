package service.menu;

import model.entity.Item;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repo.repository.MenuRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBMenuTest {

    private DBMenu dbMenu;

    @Mock
    private MenuRepository mockMenuRepository;

    @BeforeAll
    void printHeader() {
        System.out.println("====================================================================");
        System.out.println("FILE TESTING: DBMenuTest.java");
        System.out.println("PURPOSE: Tests the database service for fetching and managing menu items.");
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
        dbMenu = new DBMenu(mockMenuRepository);
    }

    @Test
    @DisplayName("1. testGetItemsByCategory()")
    void testGetItemsByCategory() throws Exception {
        List<Item> mockItems = Arrays.asList(
                new Item(1, "Coffee", 250, 10),
                new Item(2, "Latte", 350, 5)
        );
        when(mockMenuRepository.findItemsByCategoryId(1)).thenReturn(mockItems);

        List<Item> items = dbMenu.getItemsByCategory(1);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Latte", items.get(1).getName());
    }

    @Test
    @DisplayName("2. testGetItemById()")
    void testGetItemById() throws Exception {
        Item mockItem = new Item(1, "Coffee", 250, 10);
        when(mockMenuRepository.findItemById(1)).thenReturn(Optional.of(mockItem));

        Item item = dbMenu.getItemById(1);

        assertNotNull(item);
        assertEquals(1, item.getId());
    }

    @Test
    @DisplayName("3. testUpdateItemInventory()")
    void testUpdateItemInventory() throws Exception {
        // Act: Call the service method
        dbMenu.updateItemInventory(1, 5);

        // Verify that the service correctly called the repository method
        verify(mockMenuRepository).updateInventory(1, 5);
    }
}