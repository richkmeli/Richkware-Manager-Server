package richk.RMS.model;

import java.util.List;

public interface Model {
    public boolean CreateDeviceSchema() throws ModelException;

    public boolean CreateDeviceTable() throws ModelException;

    public List<Device> RefreshDevice() throws ModelException;

    public boolean AddDevice(Device device) throws ModelException;

    public boolean EditDevice(Device device) throws ModelException;

    public boolean RemoveDevice(String string) throws ModelException;

    public Device GetDevice(String name) throws ModelException;

    public String GetEncryptionKey(String name) throws ModelException;
}
