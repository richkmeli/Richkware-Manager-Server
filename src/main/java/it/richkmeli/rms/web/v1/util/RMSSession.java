package it.richkmeli.rms.web.v1.util;

import it.richkmeli.jframework.auth.AuthDatabaseModel;
import it.richkmeli.jframework.auth.web.util.AuthSession;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.model.device.DeviceDatabaseModel;
import it.richkmeli.rms.data.model.device.DeviceDatabaseSpringManager;
import it.richkmeli.rms.data.model.rmc._RmcDatabaseManager;

/**
 * do not add static modifier to these fields, because this respective ServletManager has a static object of this class
 */

public class RMSSession extends AuthSession {
    private DeviceDatabaseModel deviceDatabaseModel;
    private _RmcDatabaseManager rmcDatabaseManager;
    private String rmcID;       //client id from RichkwareSchema
    private String channel;

    public RMSSession(DeviceDatabaseModel deviceDatabaseModel, AuthDatabaseModel authDatabaseModel) throws DatabaseException {
        super(authDatabaseModel);
        this.deviceDatabaseModel = deviceDatabaseModel;
        rmcDatabaseManager = new _RmcDatabaseManager();
        channel = null;
        rmcID = null;
    }

    public RMSSession(DeviceDatabaseModel deviceDatabaseModel, AuthSession authSession) throws DatabaseException {
        super(authSession);
        this.deviceDatabaseModel = deviceDatabaseModel;
        rmcDatabaseManager = new _RmcDatabaseManager();
        channel = null;
        rmcID = null;
    }

    public RMSSession(RMSSession rmsSession, AuthSession authSession) {
        super(authSession);
        this.deviceDatabaseModel = rmsSession.deviceDatabaseModel;
        rmcDatabaseManager = rmsSession.rmcDatabaseManager;
        channel = rmsSession.channel;
        rmcID = rmsSession.rmcID;
    }

    public DeviceDatabaseModel getDeviceDatabaseManager() throws DatabaseException {
        //Logger.i("deviceDatabaseManager" + deviceDatabaseManager);
        if (deviceDatabaseModel == null) {
            Logger.info("init AuthDatabase");
            //deviceDatabaseManager = new DeviceDatabaseManager();
        }
        return deviceDatabaseModel;
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

