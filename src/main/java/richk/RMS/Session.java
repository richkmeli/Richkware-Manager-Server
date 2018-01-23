package richk.RMS;

import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.model.User;

public class Session {
    protected DatabaseManager databaseManager;
    private User user;

    public Session() throws DatabaseException {
        databaseManager = new DatabaseManager();
        user = null;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removeUser() {
        this.user = null;
    }
}
