package service.categories;

import model.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repo.repository.CategoryRepository;

import java.util.List;

/**
 * Service layer for categories. Orchestrates fetching category data
 * by using the CategoryRepository.
 */
public class DBCategory implements CategoryList {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBCategory.class);
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for dependency injection.
     * @param categoryRepository The repository for accessing category data.
     */
    public DBCategory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all product categories.
     * @return A list of {@link Category} objects.
     * @throws Exception if an error occurs during the operation.
     */
    @Override
    public List<Category> getAllCategories() throws Exception {
        try {
            return categoryRepository.getAllCategories();
        } catch (Exception e) {
            LOGGER.error("Error fetching categories.", e);
            throw new Exception("Failed to retrieve categories due to a system error.", e);
        }
    }
}