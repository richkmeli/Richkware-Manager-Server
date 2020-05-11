package it.richkmeli.rms.data.model.device;


import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;

import java.util.List;

public interface DeviceDatabaseModel {

    List<Device> getAllDevices() throws AuthDatabaseException;

    List<Device> getUserDevices(String user) throws AuthDatabaseException;

    Device addDevice(Device device) throws AuthDatabaseException;

    Device editDevice(Device device) throws AuthDatabaseException;

    void removeDevice(String string) throws AuthDatabaseException;

    Device getDevice(String name) throws AuthDatabaseException;

    String getEncryptionKey(String name) throws AuthDatabaseException;

    boolean editCommands(String deviceName, String commands) throws AuthDatabaseException;

    String getCommands(String deviceName) throws AuthDatabaseException;

    boolean setCommandsOutput(String deviceName, String commandsOutput) throws AuthDatabaseException;

    String getCommandsOutput(String deviceName) throws AuthDatabaseException;
}
