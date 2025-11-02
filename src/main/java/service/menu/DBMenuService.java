package service.menu;

import model.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repo.repository.MenuRepository;

import java.util.List;

/**
 * Service layer for the product catalog. Orchestrates fetching and updating menu items
 * by using the MenuRepository.
 */
public class DBMenuService implements Catalog {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBMenuService.class);
    private final MenuRepository menuRepository;

    /**
     * Constructor for dependency injection.
     * @param menuRepository The repository for accessing menu item data.
     */
    public DBMenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Item> getItemsByCategory(int categoryId) throws Exception {
        try {
            return menuRepository.findItemsByCategoryId(categoryId);
        } catch (Exception e) {
            LOGGER.error("Error fetching items by category.", e);
            throw new Exception("Failed to retrieve items by category.", e);
        }
    }

    @Override
    public Item getItemById(int itemId) throws Exception {
        try {
            return menuRepository.findItemById(itemId).orElse(null);
        } catch (Exception e) {
            LOGGER.error("Error fetching item by ID.", e);
            throw new Exception("Failed to retrieve item by ID.", e);
        }
    }

    @Override
    public void updateItemInventory(int itemId, int quantity) throws Exception {
        try {
            menuRepository.updateInventory(itemId, quantity);
        } catch (Exception e) {
            LOGGER.error("Error updating item inventory.", e);
            throw new Exception("Failed to update inventory.", e);
        }
    }
}