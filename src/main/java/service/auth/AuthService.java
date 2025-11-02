package service.auth;

/**
 * Defines the contract for an authentication service.
 * Implementations of this interface are responsible for verifying user credentials.
 */
public interface AuthService {

    /**
     * Authenticates a user based on their username and password.
     * On successful authentication, it should also manage the user's session.
     *
     * @param username The user's login name.
     * @param password The user's plain-text password.
     * @return {@code true} if the credentials are valid, {@code false} otherwise.
     * @throws Exception if a critical error (e.g., database connection) occurs.
     */
    boolean authenticate(String username, String password) throws Exception;
}