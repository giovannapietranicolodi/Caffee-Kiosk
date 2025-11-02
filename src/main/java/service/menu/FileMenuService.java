package service.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An implementation of Catalog that reads menu item data from a local JSON file.
 */
public class FileMenuService implements Catalog {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMenuService.class);
    private final List<Item> items;

    public FileMenuService() {
        this.items = loadFromFile();
    }

    @Override
    public List<Item> getItemsByCategory(int categoryId) {
        return items.stream()
                .filter(item -> item.getCategoryId() == categoryId)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(int itemId) {
        return items.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateItemInventory(int itemId, int quantity) {
        Item item = getItemById(itemId);
        if (item != null) {
            item.setInventory(item.getInventory() - quantity);
        }
    }

    private List<Item> loadFromFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/menu.json")) {
            if (inputStream == null) {
                throw new RuntimeException("FATAL: Cannot find '/data/menu.json' in classpath. Application cannot start in InternalFile mode.");
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Item> loadedItems = mapper.readValue(inputStream, new TypeReference<List<Item>>() {});
            LOGGER.info("Successfully loaded {} menu items from JSON.", loadedItems.size());
            return loadedItems;
        } catch (Exception e) {
            LOGGER.error("FATAL: Failed to read or parse menu.json.", e);
            throw new RuntimeException("Failed to initialize FileMenuService.", e);
        }
    }
}