package it.richkmeli.rms.data.model.device;

import it.richkmeli.jframework.orm.DatabaseException;

import java.util.List;

public interface DeviceDatabaseModel {

    List<Device> getAllDevices() throws DatabaseException;

    List<Device> getUserDevices(String user) throws DatabaseException;

    Device addDevice(Device device) throws DatabaseException;

    Device editDevice(Device device) throws DatabaseException;

    void removeDevice(String string) throws DatabaseException;

    Device getDevice(String name) throws DatabaseException;

    String getEncryptionKey(String name) throws DatabaseException;

    boolean editCommands(String deviceName, String commands) throws DatabaseException;

    String getCommands(String deviceName) throws DatabaseException;

    boolean setCommandsOutput(String deviceName, String commandsOutput) throws DatabaseException;

    String getCommandsOutput(String deviceName) throws DatabaseException;
}
