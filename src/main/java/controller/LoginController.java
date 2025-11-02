package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.auth.AuthService;

/**
 * JavaFX Controller for the Login screen (login.fxml).
 * Handles user authentication attempts.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final AuthService authService;
    private final Runnable onLoginSuccess;

    /**
     * Constructor for dependency injection.
     * @param authService The authentication service.
     * @param onLoginSuccess A callback to execute upon successful login.
     */
    public LoginController(AuthService authService, Runnable onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;
    }

    /**
     * Handles the login button click event. It validates input and uses the AuthService to authenticate.
     */
    @FXML
    private void handleLoginAttempt() {
        String user = usernameField.getText();
        String password = passwordField.getText();

        if (user.isEmpty() || password.isEmpty()) {
            showMessage("Username and password are required.", true);
            return;
        }

        try {
            if (authService.authenticate(user, password)) {
                if (onLoginSuccess != null) {
                    onLoginSuccess.run();
                }
            } else {
                showMessage("Invalid username or password.", true);
                passwordField.clear();
            }
        } catch (Exception e) {
            showMessage("Critical authentication error: " + e.getMessage(), true);
        }
    }

    /**
     * Displays a message dialog to the user.
     * @param message The message to display.
     * @param isError True to display an error icon, false for an information icon.
     */
    private void showMessage(String message, boolean isError) {
        Alert.AlertType type = isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}