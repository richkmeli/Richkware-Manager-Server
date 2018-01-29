package richk.RMS;

import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.model.Device;
import richk.RMS.model.ModelException;
import richk.RMS.model.User;

public class Session {
    protected DatabaseManager databaseManager;
    private String userID;
    private Boolean isAdmin;


    public Session() throws DatabaseException {
        databaseManager = new DatabaseManager();
        userID = null;
        isAdmin = false;

        //TODO: load data for testing
        try {
            databaseManager.addUser(new User("richk@i.it", "00000000", true));
            databaseManager.addUser(new User("er@fv.it", "00000000", false));
            databaseManager.addDevice(new Device("rick2", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it"));
            databaseManager.addDevice(new Device("rick3", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it"));
            databaseManager.addDevice(new Device("rick1", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "er@fv.it"));
            databaseManager.addUser(new User("richk@i.it", "00000000", true));
        } catch (ModelException e) {
        }

    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public String getUser() {
        return userID;
    }

    public void setUser(String userID) {
        this.userID = userID;
    }

    public void removeUser() {
        this.userID = null;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

}
