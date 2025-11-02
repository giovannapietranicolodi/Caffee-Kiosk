package service.auth;

/**
 * A Singleton class to manage the current user session, holding information
 * about the logged-in employee.
 */
public class SessionManager {

    private static SessionManager instance;
    private String loggedInEmployeeName;
    private int loggedInEmployeeId;
    private boolean isManager;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String getLoggedInEmployeeName() {
        return loggedInEmployeeName;
    }

    public void setLoggedInEmployeeName(String loggedInEmployeeName) {
        this.loggedInEmployeeName = loggedInEmployeeName;
    }

    public int getLoggedInEmployeeId() {
        return loggedInEmployeeId;
    }

    public void setLoggedInEmployeeId(int loggedInEmployeeId) {
        this.loggedInEmployeeId = loggedInEmployeeId;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public void clearSession() {
        this.loggedInEmployeeName = null;
        this.loggedInEmployeeId = 0;
        this.isManager = false;
    }
}