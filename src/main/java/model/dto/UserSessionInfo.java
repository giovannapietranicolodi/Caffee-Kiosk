package model.dto;

/**
 * A Data Transfer Object used to carry user information from the repository to the service layer.
 */
public class UserSessionInfo {
    private final int id;
    private final String name;
    private final String hashedPassword;
    private final boolean isManager;

    public UserSessionInfo(int id, String name, String hashedPassword, boolean isManager) {
        this.id = id;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.isManager = isManager;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public boolean isManager() {
        return isManager;
    }
}