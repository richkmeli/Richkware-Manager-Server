package it.richkmeli.rms.web.v1.util;

import it.richkmeli.jframework.auth.web.util.AuthSession;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.model.device._DeviceDatabaseManager;
import it.richkmeli.rms.data.model.rmc._RmcDatabaseManager;

/**
 * do not add static modifier to these fields, because this respective ServletManager has a static object of this class
 */

public class RMSSession extends AuthSession {
    private _DeviceDatabaseManager deviceDatabaseManager;
    private _RmcDatabaseManager rmcDatabaseManager;
    private String rmcID;       //client id from RichkwareSchema
    private String channel;

    public RMSSession() throws DatabaseException {
        super();
        deviceDatabaseManager = new _DeviceDatabaseManager();
        rmcDatabaseManager = new _RmcDatabaseManager();
        channel = null;
        rmcID = null;
    }

    public RMSSession(AuthSession authSession) throws DatabaseException {
        super(authSession);
        deviceDatabaseManager = new _DeviceDatabaseManager();
        rmcDatabaseManager = new _RmcDatabaseManager();
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

    public _DeviceDatabaseManager getDeviceDatabaseManager() throws DatabaseException {
        //Logger.i("deviceDatabaseManager" + deviceDatabaseManager);
        if (deviceDatabaseManager == null) {
            Logger.info("init AuthDatabase");
            deviceDatabaseManager = new _DeviceDatabaseManager();
        }
        return deviceDatabaseManager;
    }


    public _RmcDatabaseManager getRmcDatabaseManager() throws DatabaseException {
        if (rmcDatabaseManager == null) {
            Logger.info("init AuthDatabase");
            rmcDatabaseManager = new _RmcDatabaseManager();
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

