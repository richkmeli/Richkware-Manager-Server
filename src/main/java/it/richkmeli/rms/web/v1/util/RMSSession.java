package it.richkmeli.rms.web.v1.util;

import it.richkmeli.jframework.auth.AuthDatabaseModel;
import it.richkmeli.jframework.auth.web.util.AuthSession;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.model.device.DeviceDatabaseModel;
import it.richkmeli.rms.data.model.rmc.RmcDatabaseModel;

/**
 * do not add static modifier to these fields, because this respective ServletManager has a static object of this class
 */

public class RMSSession extends AuthSession {
    private DeviceDatabaseModel deviceDatabaseModel;
    private RmcDatabaseModel rmcDatabaseModel;
    private String rmcID;       //client id from RichkwareSchema
    private String channel;


    public RMSSession(AuthDatabaseModel authDatabaseModel, DeviceDatabaseModel deviceDatabaseModel, RmcDatabaseModel rmcDatabaseModel) throws DatabaseException {
        super(authDatabaseModel);
        this.deviceDatabaseModel = deviceDatabaseModel;
        this.rmcDatabaseModel = rmcDatabaseModel;
        channel = null;
        rmcID = null;
    }

    public RMSSession(AuthSession authSession, DeviceDatabaseModel deviceDatabaseModel, RmcDatabaseModel rmcDatabaseModel) throws DatabaseException {
        super(authSession);
        this.deviceDatabaseModel = deviceDatabaseModel;
        this.rmcDatabaseModel = rmcDatabaseModel;
        channel = null;
        rmcID = null;
    }

    public RMSSession(RMSSession rmsSession, AuthSession authSession) {
        super(authSession);
        this.deviceDatabaseModel = rmsSession.deviceDatabaseModel;
        this.rmcDatabaseModel = rmsSession.rmcDatabaseModel;
        channel = rmsSession.channel;
        rmcID = rmsSession.rmcID;
    }

    public DeviceDatabaseModel getDeviceDatabaseManager() throws DatabaseException {
        //Logger.i("deviceDatabaseManager" + deviceDatabaseManager);
        if (deviceDatabaseModel == null) {
            Logger.info("init deviceDatabaseModel");
            //deviceDatabaseManager = new DeviceDatabaseManager();
        }
        return deviceDatabaseModel;
    }


    public RmcDatabaseModel getRmcDatabaseManager() throws DatabaseException {
        if (rmcDatabaseModel == null) {
            Logger.info("init rmcDatabaseManager");
            //rmcDatabaseManager = new _RmcDatabaseManager();
        }
        return rmcDatabaseModel;
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

