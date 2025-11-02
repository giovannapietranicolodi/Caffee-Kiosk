package service.discount;

import model.entity.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repo.repository.DiscountRepository;

import java.util.List;

/**
 * Service layer for discounts. Orchestrates fetching discount data
 * by using the DiscountRepository.
 */
public class DBDiscountService implements DiscountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBDiscountService.class);
    private final DiscountRepository discountRepository;

    /**
     * Constructor for dependency injection.
     * @param discountRepository The repository for accessing discount data.
     */
    public DBDiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    /**
     * Retrieves all active discounts.
     * @return A list of active {@link Discount} objects.
     * @throws Exception if an error occurs during the operation.
     */
    @Override
    public List<Discount> getActiveDiscounts() throws Exception {
        try {
            return discountRepository.findActiveDiscounts();
        } catch (Exception e) {
            LOGGER.error("Error fetching active discounts.", e);
            throw new Exception("Failed to retrieve active discounts.", e);
        }
    }
}