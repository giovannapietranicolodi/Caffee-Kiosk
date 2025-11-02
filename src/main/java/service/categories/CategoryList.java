package service.categories;

import model.entity.Category;

import java.util.List;

/**
 * Defines the contract for a service that provides a list of product categories.
 */
public interface CategoryList {

    /**
     * Fetches all available product categories.
     *
     * @return A list of {@link Category} objects.
     * @throws Exception if an error occurs during data retrieval (e.g., database error).
     */
    List<Category> getAllCategories() throws Exception;
}