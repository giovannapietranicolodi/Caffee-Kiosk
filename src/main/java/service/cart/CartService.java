package service.cart;

import model.entity.CartItem;
import model.entity.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A concrete implementation of the {@link Cart} interface.
 * This class manages the state of the shopping cart in memory.
 */
public class CartService implements Cart {

    private final List<CartItem> cartItems = new ArrayList<>();

    /**
     * Adds an item to the cart. If the item already exists, its quantity is updated
     * by replacing the existing CartItem with a new one.
     * @param item The {@link Item} to add.
     * @param quantity The quantity of the item to add.
     */
    @Override
    public void addItem(Item item, int quantity) {
        // Find if the item is already in the cart.
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(cartItem -> cartItem.getItem().getId() == item.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            // If item exists, create a new CartItem with the updated quantity.
            CartItem currentCartItem = existingItem.get();
            int newQuantity = currentCartItem.getQuantity() + quantity;
            CartItem updatedCartItem = new CartItem(currentCartItem.getItem(), newQuantity);
            // Replace the old item with the updated one.
            cartItems.remove(currentCartItem);
            cartItems.add(updatedCartItem);
        } else {
            // If not found, add as a new item.
            cartItems.add(new CartItem(item, quantity));
        }
    }

    /**
     * Removes a specific {@link CartItem} from the cart.
     * @param item The cart item to remove.
     */
    @Override
    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    /**
     * Clears all items from the cart.
     */
    @Override
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Retrieves the list of all items currently in the cart.
     * @return A list of {@link CartItem} objects.
     */
    @Override
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems); // Return a copy to prevent external modification
    }
}