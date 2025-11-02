package service.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of AuthService that authenticates against a local JSON file.
 * This service uses plain text passwords for simplicity.
 */
public class FileAuthService implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileAuthService.class);
    private final List<User> users;

    /**
     * Loads user data from the /data/users.json file upon instantiation.
     * Throws a RuntimeException if the file cannot be loaded or parsed.
     */
    public FileAuthService() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/users.json")) {
            if (inputStream == null) {
                throw new RuntimeException("FATAL: Cannot find '/data/users.json' in classpath. Application cannot start in InternalFile mode.");
            }
            ObjectMapper mapper = new ObjectMapper();
            this.users = mapper.readValue(inputStream, new TypeReference<List<User>>() {});
            LOGGER.info("Successfully loaded {} users from users.json for file-based authentication.", this.users.size());
        } catch (Exception e) {
            LOGGER.error("FATAL: Failed to read or parse users.json.", e);
            throw new RuntimeException("Failed to initialize FileAuthService due to a data file error.", e);
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        LOGGER.debug("Attempting to authenticate user: '{}'", username);

        for (User user : users) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                LOGGER.info("Authentication successful for user: {}", username);
                SessionManager.getInstance().setLoggedInEmployeeId(user.getId());
                SessionManager.getInstance().setLoggedInEmployeeName(user.getName());
                SessionManager.getInstance().setManager(user.isManager());
                return true;
            }
        }

        LOGGER.warn("Authentication failed for user: {}. User not found or password incorrect.", username);
        return false;
    }

    /**
     * Static inner class for deserializing user data from JSON using Jackson.
     */
    private static class User {
        private int id;
        private String name;
        private String username;
        private String password;
        private boolean isManager;

        // Getters and setters with explicit Jackson property mapping
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        @JsonProperty("isManager")
        public boolean isManager() { return isManager; }
        public void setManager(boolean manager) { isManager = manager; }
    }
}