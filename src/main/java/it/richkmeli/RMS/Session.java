package it.richkmeli.RMS;

import it.richkmeli.RMS.data.device.DeviceDatabaseManager;
import it.richkmeli.RMS.data.device.model.Device;
import it.richkmeli.jframework.auth.AuthDatabaseManager;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;

public class Session {
    private DeviceDatabaseManager deviceDatabaseManager;
    private AuthDatabaseManager authDatabaseManager;
    private String userID;
    private Boolean isAdmin;


    public Session() throws DatabaseException {
        deviceDatabaseManager = new DeviceDatabaseManager();
        authDatabaseManager = new AuthDatabaseManager();
        userID = null;
        isAdmin = false;

        //TODO: load data for testing
        try {
            authDatabaseManager.addUser(new User("richk@i.it", "00000000", true));
            authDatabaseManager.addUser(new User("er@fv.it", "00000000", false));
            deviceDatabaseManager.addDevice(new Device("rick2", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it"));
            deviceDatabaseManager.addDevice(new Device("rick3", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it"));
            deviceDatabaseManager.addDevice(new Device("rick1", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "er@fv.it"));
            authDatabaseManager.addUser(new User("richk@i.it", "00000000", true));
        } catch (DatabaseException e) {
            Logger.e("Session ",e);
        }

    }

    /*public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }*/

    public DeviceDatabaseManager getDeviceDatabaseManager() throws DatabaseException {
        if(deviceDatabaseManager != null){
            return deviceDatabaseManager;
        }else{
            deviceDatabaseManager = new DeviceDatabaseManager();
            return deviceDatabaseManager;
        }
    }

    public AuthDatabaseManager getAuthDatabaseManager() throws DatabaseException {
        if(authDatabaseManager != null){
            return authDatabaseManager;
        }else{
            authDatabaseManager = new AuthDatabaseManager();
            return authDatabaseManager;
        }
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
