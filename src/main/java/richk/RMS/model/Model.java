package richk.RMS.model;

import java.util.List;

public interface Model {
    public boolean CreateSchema() throws ModelException;

    public boolean CreateTables() throws ModelException;

    public List<Device> RefreshDevice() throws ModelException;

    public boolean AddDevice(Device device) throws ModelException;

    public boolean EditDevice(Device device) throws ModelException;

    public boolean RemoveDevice(String string) throws ModelException;

    public Device GetDevice(String name) throws ModelException;

    public String GetEncryptionKey(String name) throws ModelException;

    public boolean AddUser(User user) throws ModelException;

    public boolean IsUserPresent(String email) throws ModelException;

    public boolean EditPassword(User user) throws ModelException;

    public boolean CheckPassword(User user) throws ModelException;
}
