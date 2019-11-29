package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.network.tcp.server.http.util.Session;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.data.rmc.RMCDatabaseManager;

public class RMSSession extends Session {
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

    public RMSSession(Session session) throws DatabaseException {
        super(session);
        deviceDatabaseManager = new DeviceDatabaseManager();
        rmcDatabaseManager = new RMCDatabaseManager();
        channel = null;
        rmcID = null;
    }

    public DeviceDatabaseManager getDeviceDatabaseManager() throws DatabaseException {
        //Logger.i("deviceDatabaseManager" + deviceDatabaseManager);
        if (deviceDatabaseManager != null) {
            return deviceDatabaseManager;
        } else {
            deviceDatabaseManager = new DeviceDatabaseManager();
            return deviceDatabaseManager;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


}

