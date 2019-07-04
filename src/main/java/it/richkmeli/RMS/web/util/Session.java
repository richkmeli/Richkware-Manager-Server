package it.richkmeli.RMS.web.util;

import it.richkmeli.RMS.data.device.DeviceDatabaseManager;
import it.richkmeli.jcrypto.Crypto;
import it.richkmeli.jframework.auth.AuthDatabaseManager;
import it.richkmeli.jframework.database.DatabaseException;

public class Session {
    private DeviceDatabaseManager deviceDatabaseManager;
    private AuthDatabaseManager authDatabaseManager;
    private String userID;
    private Boolean isAdmin;
    private Crypto.Server cryptoServer;


    public Session() throws DatabaseException {
        deviceDatabaseManager = new DeviceDatabaseManager();
        authDatabaseManager = new AuthDatabaseManager();
        userID = null;
        isAdmin = false;
        cryptoServer = new Crypto.Server();
    }

    /*public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }*/

    public DeviceDatabaseManager getDeviceDatabaseManager() throws DatabaseException {
        //Logger.i("deviceDatabaseManager" + deviceDatabaseManager);
        if (deviceDatabaseManager != null) {
            return deviceDatabaseManager;
        } else {
            deviceDatabaseManager = new DeviceDatabaseManager();
            return deviceDatabaseManager;
        }
    }

    public AuthDatabaseManager getAuthDatabaseManager() throws DatabaseException {
        //Logger.i("authDatabaseManager" + authDatabaseManager);
        if (authDatabaseManager != null) {
            return authDatabaseManager;
        } else {
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

    public Crypto.Server getCryptoServer() {
        return cryptoServer;
    }
}
