package it.richkmeli.rms.data.device.model;

import it.richkmeli.jframework.orm.DatabaseException;

import java.util.List;

public interface DeviceModel {

    List<Device> getAllDevices() throws DatabaseException;

    List<Device> getUserDevices(String user) throws DatabaseException;

    boolean addDevice(Device device) throws DatabaseException;

    boolean editDevice(Device device) throws DatabaseException;

    boolean removeDevice(String string) throws DatabaseException;

    Device getDevice(String name) throws DatabaseException;

    String getEncryptionKey(String name) throws DatabaseException;
}
