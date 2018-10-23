package it.richkmeli.RMS.model;

import java.util.List;

public interface Model {
    public boolean createSchema() throws ModelException;

    public boolean createTables() throws ModelException;

    public List<Device> refreshDevice() throws ModelException;

    public List<Device> refreshDevice(String user) throws ModelException;

    public List<User> refreshUser() throws ModelException;

    public boolean addDevice(Device device) throws ModelException;

    public boolean editDevice(Device device) throws ModelException;

    public boolean removeDevice(String string) throws ModelException;

    public Device getDevice(String name) throws ModelException;

    public String getEncryptionKey(String name) throws ModelException;

    public boolean addUser(User user) throws ModelException;

    public boolean removeUser(String email) throws ModelException;

    public boolean isUserPresent(String email) throws ModelException;

    public boolean editPassword(String email, String pass) throws ModelException;

    public boolean editAdmin(String email, Boolean isAdmin) throws ModelException;

    public boolean checkPassword(String email, String pass) throws ModelException;

    public boolean isAdmin(String email) throws ModelException;

}
