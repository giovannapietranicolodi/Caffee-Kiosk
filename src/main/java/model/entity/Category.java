package model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a product category (e.g., "Hot Drinks", "Pastries").
 */
public class Category {
    private final int id;
    private final String description;

    /**
     * Constructor for creating a Category. Annotated for Jackson deserialization.
     * @param id The unique identifier for the category.
     * @param description The display name of the category.
     */
    @JsonCreator
    public Category(@JsonProperty("id") int id, @JsonProperty("description") String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}