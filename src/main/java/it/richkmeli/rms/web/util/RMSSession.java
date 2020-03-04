package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.auth.web.util.AuthSession;
import it.richkmeli.jframework.network.tcp.server.http.util.Session;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.data.rmc.RMCDatabaseManager;

/**
 * do not add static modifier to these fields, because this respective ServletManager has a static object of this class
 */

public class RMSSession extends AuthSession {
    private DeviceDatabaseManager deviceDatabaseManager;
    private RMCDatabaseManager rmcDatabaseManager;
    private String rmcID;       //client id from RichkwareSchema
    private String channel;

    public RMSSession() throws DatabaseException {
        super();
        deviceDatabaseManager = new DeviceDatabaseManager();
        rmcDatabaseManager = new RMCDatabaseManager();
        channel = null;
        rmcID = null;
    }

    public RMSSession(AuthSession authSession) throws DatabaseException {
        super(authSession);
        deviceDatabaseManager = new DeviceDatabaseManager();
        rmcDatabaseManager = new RMCDatabaseManager();
        channel = null;
        rmcID = null;
    }

    public RMSSession(RMSSession rmsSession, AuthSession authSession) {
        super(authSession);
        deviceDatabaseManager = rmsSession.deviceDatabaseManager;
        rmcDatabaseManager = rmsSession.rmcDatabaseManager;
        channel = rmsSession.channel;
        rmcID = rmsSession.rmcID;
    }

    public DeviceDatabaseManager getDeviceDatabaseManager() throws DatabaseException {
        //Logger.i("deviceDatabaseManager" + deviceDatabaseManager);
        if (deviceDatabaseManager == null) {
            Logger.info("init AuthDatabase");
            deviceDatabaseManager = new DeviceDatabaseManager();
        }
        return deviceDatabaseManager;
    }


    public RMCDatabaseManager getRmcDatabaseManager() throws DatabaseException {
        if (rmcDatabaseManager == null) {
            Logger.info("init AuthDatabase");
            rmcDatabaseManager = new RMCDatabaseManager();
        }
        return rmcDatabaseManager;
    }

    public String getRmcID() {
        return rmcID;
    }

    public void setRmcID(String rmcID) {
        this.rmcID = rmcID;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


}

