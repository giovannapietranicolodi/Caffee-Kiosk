package service.discount;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.entity.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of DiscountService that reads discount data from a local JSON file.
 */
public class FileDiscountService implements DiscountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDiscountService.class);
    private final List<Discount> discounts;

    public FileDiscountService() {
        this.discounts = loadFromFile();
    }

    @Override
    public List<Discount> getActiveDiscounts() {
        return discounts;
    }

    private List<Discount> loadFromFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/discounts.json")) {
            if (inputStream == null) {
                throw new RuntimeException("FATAL: Cannot find '/data/discounts.json' in classpath. Application cannot start in InternalFile mode.");
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Discount> loadedDiscounts = mapper.readValue(inputStream, new TypeReference<List<Discount>>() {});
            LOGGER.info("Successfully loaded {} discounts from JSON.", loadedDiscounts.size());
            return loadedDiscounts;
        } catch (Exception e) {
            LOGGER.error("FATAL: Failed to read or parse discounts.json.", e);
            throw new RuntimeException("Failed to initialize FileDiscountService.", e);
        }
    }
}