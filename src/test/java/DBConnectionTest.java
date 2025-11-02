import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import repo.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionTest {

    @Test
    @Disabled("This is an integration test and requires a running database. Enable it for full integration checks.")
    void testDatabaseConnection() {
        try (Connection connection = DBConnection.dbConnection()) {

            assertNotNull(connection, "Failed to connect to the database.");
            System.out.println("Database connected successfully!");

            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT name, price FROM items WHERE is_available = TRUE LIMIT 5";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            boolean foundItems = false;
            /*
            System.out.println("\n           ------ Items list: -----");

            while (resultSet.next()) {
                foundItems = true;
                String itemName = resultSet.getString("name");
                int price = resultSet.getInt("price");
                double priceDecimal = price / 100.0;

                System.out.printf("Item: %-25s | Price: $ %.2f%n", itemName, priceDecimal);
            }
            */

            if (!foundItems) {
                System.out.println("No items found. Is the database populated?");
            }

        } catch (SQLException e) {
            System.err.println("\n!!! ERROR: Connection failed!");
            System.err.println("Verify if the database is running and if the user and password are correct.");
            e.printStackTrace();
            fail("SQLException was thrown: " + e.getMessage());
        }
    }
}