package it.richkmeli.RMS.data.device.model;

import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.database.DatabaseModel;

import java.util.List;

public interface DeviceModel extends DatabaseModel {

    public List<Device> refreshDevice() throws DatabaseException;

    public List<Device> refreshDevice(String user) throws DatabaseException;

    public boolean addDevice(Device device) throws DatabaseException;

    public boolean editDevice(Device device) throws DatabaseException;

    public boolean removeDevice(String string) throws DatabaseException;

    public Device getDevice(String name) throws DatabaseException;

    public String getEncryptionKey(String name) throws DatabaseException;
}
