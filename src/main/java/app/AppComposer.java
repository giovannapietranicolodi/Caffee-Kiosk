package app;

import controller.CaffeeController;
import controller.CheckoutHandler;
import controller.HistoryController;
import controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repo.repository.*;
import service.auth.AuthService;
import service.auth.DBAuthService;
import service.auth.FileAuthService;
import service.cart.Cart;
import service.cart.CartService;
import service.categories.CategoryList;
import service.categories.DBCategoryService;
import service.categories.FileCategoryService;
import service.discount.DBDiscountService;
import service.discount.DiscountCalculationService;
import service.discount.DiscountService;
import service.discount.FileDiscountService;
import service.menu.Catalog;
import service.menu.DBMenuService;
import service.menu.FileMenuService;
import service.order.TotalsCalculatorService;
import service.receipt.DBReceiptService;
import service.receipt.FileReceiptService;
import service.receipt.ReceiptBuilderService;
import service.receipt.ReceiptService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppComposer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppComposer.class);
    private Stage primaryStage;
    private Properties properties;
    private String dataSource;

    // Services
    private AuthService authService;
    private CategoryList categoryListService;
    private Catalog menuCatalogService;
    private ReceiptService receiptService;
    private DiscountService discountService;
    private final Cart cartService = new CartService();
    private final TotalsCalculatorService totalsCalculatorService = new TotalsCalculatorService();
    private final DiscountCalculationService discountCalculationService = new DiscountCalculationService();
    private ReceiptBuilderService receiptBuilderService;

    public AppComposer() {
        loadConfig();
        createServices();
    }

    private void loadConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/app.properties")) {
            if (input == null) {
                LOGGER.error("app.properties not found, defaulting to DBConnection.");
                dataSource = "DBConnection";
                return;
            }
            properties.load(input);
            dataSource = properties.getProperty("data.source", "DBConnection");
        } catch (IOException e) {
            LOGGER.error("Error loading app.properties, defaulting to DBConnection.", e);
            dataSource = "DBConnection";
        }
    }

    private void createServices() {
        receiptBuilderService = new ReceiptBuilderService(totalsCalculatorService);

        if ("InternalFile".equals(dataSource)) {
            authService = new FileAuthService();
            categoryListService = new FileCategoryService();
            menuCatalogService = new FileMenuService();
            receiptService = new FileReceiptService();
            discountService = new FileDiscountService();
        } else {
            authService = new DBAuthService(new AuthRepository());
            categoryListService = new DBCategoryService(new CategoryRepository());
            menuCatalogService = new DBMenuService(new MenuRepository());
            receiptService = new DBReceiptService(new ReceiptRepository());
            discountService = new DBDiscountService(new DiscountRepository());
        }
    }

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("OOP Caffee Kiosk");
        showLoginScene();
    }

    public void showLoginScene() {
        try {
            Runnable onLoginSuccess = this::showMainScene;
            LoginController loginController = new LoginController(this.authService, onLoginSuccess);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login/login.fxml"));
            loader.setControllerFactory(param -> loginController);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setWidth(400);
            primaryStage.setHeight(300);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            LOGGER.error("FATAL: Could not load login-view.fxml.", e);
            Platform.exit();
        }
    }

    public void showMainScene() {
        try {
            HistoryController historyController = new HistoryController(this.receiptService);
            CaffeeController caffeeController = new CaffeeController(
                    this.categoryListService,
                    this.menuCatalogService,
                    this.cartService,
                    this.discountService,
                    this.discountCalculationService,
                    this.totalsCalculatorService,
                    historyController
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main/main-view.fxml"));
            loader.setControllerFactory(param -> caffeeController);
            Parent root = loader.load();

            Runnable onCheckoutComplete = caffeeController::clearCartAndResetUI;
            
            CheckoutHandler checkoutHandler = new CheckoutHandler(
                    cartService, menuCatalogService, receiptService, receiptBuilderService, 
                    discountCalculationService, totalsCalculatorService, onCheckoutComplete,
                    caffeeController.getCheckoutButton(),
                    caffeeController.getDiscountComboBox(), 
                    caffeeController.getOtherDiscountField(), 
                    caffeeController.getOtherDiscountPercentageCheckBox(), 
                    caffeeController.getObservationsTextArea()
            );
            caffeeController.setCheckoutHandler(checkoutHandler);

            Runnable onLogout = this::showLoginScene;
            caffeeController.setOnLogoutListener(onLogout);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setWidth(1280);
            primaryStage.setHeight(800);
            primaryStage.centerOnScreen();

            caffeeController.start();

        } catch (IOException e) {
            LOGGER.error("FATAL: Could not load main-view.fxml.", e);
            Platform.exit();
        }
    }
}