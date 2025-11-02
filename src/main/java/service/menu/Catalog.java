package service.menu;

import model.entity.Item;

import java.util.List;

/**
 * Defines the contract for a service that manages the product catalog.
 */
public interface Catalog {

    /**
     * Fetches all available menu items for a given category.
     *
     * @param categoryId The ID of the category.
     * @return A list of {@link Item} objects.
     * @throws Exception if a database error occurs.
     */
    List<Item> getItemsByCategory(int categoryId) throws Exception;

    /**
     * Fetches a single item by its unique ID.
     *
     * @param itemId The ID of the item.
     * @return The {@link Item} object, or null if not found.
     * @throws Exception if a database error occurs.
     */
    Item getItemById(int itemId) throws Exception;

    /**
     * Updates the inventory count for a specific item.
     *
     * @param itemId The ID of the item to update.
     * @param quantity The quantity to subtract from the current inventory.
     * @throws Exception if a database error occurs.
     */
    void updateItemInventory(int itemId, int quantity) throws Exception;
}