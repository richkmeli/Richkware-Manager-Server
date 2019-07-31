package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.auth.AuthDatabaseManager;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.data.rmc.RMCDatabaseManager;

public class Session {
    private DeviceDatabaseManager deviceDatabaseManager;
    private AuthDatabaseManager authDatabaseManager;
    private RMCDatabaseManager rmcDatabaseManager;
    private String userID;      //user from AuthSchema
    private String rmcID;       //client id from RichkwareSchema
    private Boolean isAdmin;
    private Crypto.Server cryptoServer;
    private String channel;

    public Session() throws DatabaseException {
        deviceDatabaseManager = new DeviceDatabaseManager();
        authDatabaseManager = new AuthDatabaseManager();
        rmcDatabaseManager = new RMCDatabaseManager();
        channel = null;
        userID = null;
        rmcID = null;
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

    public RMCDatabaseManager getRmcDatabaseManager() throws DatabaseException {
        if (rmcDatabaseManager != null) {
            return rmcDatabaseManager;
        } else {
            rmcDatabaseManager = new RMCDatabaseManager();
            return rmcDatabaseManager;
        }
    }

    public String getRmcID() {
        return rmcID;
    }

    public void setRmcID(String rmcID) {
        this.rmcID = rmcID;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


}

