package service.discount;

import model.entity.Discount;

import java.util.List;

/**
 * Defines the contract for a service that provides available discounts.
 */
public interface DiscountService {

    /**
     * Fetches all currently active discounts.
     *
     * @return A list of active {@link Discount} objects.
     * @throws Exception if an error occurs during data retrieval.
     */
    List<Discount> getActiveDiscounts() throws Exception;
}