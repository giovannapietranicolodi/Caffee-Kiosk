package model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a menu item available for sale.
 */
public class Item {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty price; // Price in cents
    private final IntegerProperty inventory;
    private final IntegerProperty categoryId;

    @JsonCreator
    public Item(@JsonProperty("id") int id, 
              @JsonProperty("name") String name, 
              @JsonProperty("price") int price, 
              @JsonProperty("inventory") int inventory,
              @JsonProperty("categoryId") int categoryId) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.inventory = new SimpleIntegerProperty(inventory);
        this.categoryId = new SimpleIntegerProperty(categoryId);
    }

    // Overloaded constructor for tests or other uses that don't involve categoryId
    public Item(int id, String name, int price, int inventory) {
        this(id, name, price, inventory, 0);
    }

    // --- Getters and JavaFX Property Methods ---

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }

    public int getPrice() { return price.get(); }
    public IntegerProperty priceProperty() { return price; }

    public int getInventory() { return inventory.get(); }
    public IntegerProperty inventoryProperty() { return inventory; }
    public void setInventory(int inventory) { this.inventory.set(inventory); }

    public int getCategoryId() { return categoryId.get(); }
    public IntegerProperty categoryIdProperty() { return categoryId; }
}