package model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a discount that can be applied to an order.
 * Discounts can be a fixed amount or a percentage.
 */
public class Discount {
    private final int id;
    private final String name;
    private final int amount;
    private final boolean isPercentage;
    private final boolean isActive;

    /**
     * Constructor for creating a Discount. Annotated for Jackson deserialization.
     */
    @JsonCreator
    public Discount(@JsonProperty("id") int id, 
                  @JsonProperty("name") String name, 
                  @JsonProperty("amount") int amount, 
                  @JsonProperty("isPercentage") boolean isPercentage, 
                  @JsonProperty("isActive") boolean isActive) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.isPercentage = isPercentage;
        this.isActive = isActive;
    }

    // --- Standard Getters ---

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * The string representation used in the ComboBox UI.
     * @return The name of the discount.
     */
    @Override
    public String toString() {
        return name;
    }
}