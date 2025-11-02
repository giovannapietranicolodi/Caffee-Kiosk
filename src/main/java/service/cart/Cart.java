package service.cart;

import model.entity.CartItem;
import model.entity.Item;

import java.util.List;

/**
 * Defines the contract for a shopping cart service.
 * Implementations manage the state of items selected for purchase.
 */
public interface Cart {

    /**
     * Adds a specified quantity of an item to the cart.
     * If the item already exists in the cart, its quantity should be updated.
     *
     * @param item The {@link Item} to add.
     * @param quantity The number of items to add.
     */
    void addItem(Item item, int quantity);

    /**
     * Removes a {@link CartItem} from the cart.
     *
     * @param item The specific cart item instance to remove.
     */
    void removeItem(CartItem item);

    /**
     * Removes all items from the cart.
     */
    void clearCart();

    /**
     * Retrieves all items currently in the cart.
     *
     * @return A list of {@link CartItem}s.
     */
    List<CartItem> getCartItems();
}