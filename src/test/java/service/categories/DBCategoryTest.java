package service.categories;

import model.entity.Category;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repo.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBCategoryTest {

    private DBCategory dbCategory;

    @Mock
    private CategoryRepository mockCategoryRepository;

    @BeforeAll
    void printHeader() {
        System.out.println("====================================================================");
        System.out.println("FILE TESTING: DBCategoryTest.java");
        System.out.println("PURPOSE: Tests the database service for fetching product categories.");
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
        // Inject the mocked repository into the service
        dbCategory = new DBCategory(mockCategoryRepository);
    }

    @Test
    @DisplayName("1. testGetAllCategories()")
    void testGetAllCategories() throws Exception {
        // Arrange: Define what the mocked repository should return
        List<Category> mockCategories = Arrays.asList(
                new Category(1, "Hot Drinks"),
                new Category(2, "Cold Drinks"),
                new Category(3, "Pastries")
        );
        when(mockCategoryRepository.getAllCategories()).thenReturn(mockCategories);

        // Act: Call the service method
        List<Category> categories = dbCategory.getAllCategories();

        // Assert: Verify the result
        assertNotNull(categories);
        assertEquals(3, categories.size());
        assertEquals("Cold Drinks", categories.get(1).getDescription());
    }
}