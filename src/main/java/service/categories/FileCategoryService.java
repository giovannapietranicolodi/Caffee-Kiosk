package service.categories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of CategoryList that reads category data from a local JSON file.
 */
public class FileCategoryService implements CategoryList {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileCategoryService.class);
    private final List<Category> categories;

    public FileCategoryService() {
        this.categories = loadFromFile();
    }

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    private List<Category> loadFromFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/categories.json")) {
            if (inputStream == null) {
                throw new RuntimeException("FATAL: Cannot find '/data/categories.json' in classpath. Application cannot start in InternalFile mode.");
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Category> loadedCategories = mapper.readValue(inputStream, new TypeReference<List<Category>>() {});
            LOGGER.info("Successfully loaded {} categories from JSON.", loadedCategories.size());
            return loadedCategories;
        } catch (Exception e) {
            LOGGER.error("FATAL: Failed to read or parse categories.json.", e);
            throw new RuntimeException("Failed to initialize FileCategoryService.", e);
        }
    }
}