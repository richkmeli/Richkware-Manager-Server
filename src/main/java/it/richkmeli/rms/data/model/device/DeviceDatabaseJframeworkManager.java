//package it.richkmeli.rms.data.model.device;
//
//import it.richkmeli.jframework.orm.AuthDatabaseException;
//import it.richkmeli.jframework.orm.DatabaseManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Using Jframework ORM
// */
//public class DeviceDatabaseJframeworkManager extends DatabaseManager implements DeviceDatabaseModel {
//
//    public DeviceDatabaseJframeworkManager() throws AuthDatabaseException {
//        schemaName = "AuthSchema";
//        tableName = schemaName + "." + "device";
//        table = "(" +
//                "name VARCHAR(50) NOT NULL," +
//                "ip VARCHAR(25) NOT NULL," +
//                "serverPort VARCHAR(10)," +
//                "lastConnection VARCHAR(25)," +
//                "encryptionKey VARCHAR(32)," +
//                "associatedUser VARCHAR(50) REFERENCES AuthSchema.auth(email) ON DELETE SET NULL," +
//                "commands " + ("mysql".equalsIgnoreCase(dbtype) ? "TEXT" : "VARCHAR(1000)") + "," +
//                "commandsOutput " + ("mysql".equalsIgnoreCase(dbtype) ? "TEXT" : "VARCHAR(1000)") + "," +
//                "PRIMARY KEY (name)" +
//                //"FOREIGN KEY (associatedUser) REFERENCES AuthSchema.auth(email) ON DELETE SET NULL" +
//                ")";
//
//        init();
//
//    }
//
//
//    public List<Device> getAllDevices() throws AuthDatabaseException {
//        return getUserDevices(null);
//    }
//
//
//    // TODO ORM aggiungi foreign keys
//    public List<Device> getUserDevices(String user) throws AuthDatabaseException {
//        List<Device> devices = readAll(Device.class);
//        if (devices != null) {
//            if (user != null) {
//                // filter user devices
//                List<Device> userDevices = new ArrayList<>();
//                for (Device device : devices) {
//                    if (device.getAssociatedUser().equalsIgnoreCase(user)) {
//                        userDevices.add(device);
//                    }
//                }
//                return userDevices;
//            } else {
//                // return all devices
//                return devices;
//            }
//        } else {
//            return null;
//        }
//    }
//
//    /*public boolean addDevice(Device device) throws AuthDatabaseException {
//        return add(device);
//    }*/
//
//    public Device addDevice(Device device) throws AuthDatabaseException {
//        //return create(device);
//        create(device);
//        return device;
//    }
//
//    public Device editDevice(Device device) throws AuthDatabaseException {
//        //return update(device);
//        update(device);
//        return device;
//    }
//
//    public Device getDevice(String name) throws AuthDatabaseException {
//        return read(new Device(name, null, null, null, null, null, null, null));
//    }
//
//
//    public void removeDevice(String name) throws AuthDatabaseException {
//        //return
//        delete(new Device(name, null, null, null, null, null, null, null));
//    }
//
//    public String getEncryptionKey(String name) throws AuthDatabaseException {
//        Device device = getDevice(name);
//        if (device != null) {
//            return device.getEncryptionKey();
//        } else {
//            return null;
//        }
//    }
//
//    public boolean editCommands(String deviceName, String commands) throws AuthDatabaseException {
//        return update(new Device(deviceName, null, null, null, null, null, commands, null));
//    }
//
//    public String getCommands(String deviceName) throws AuthDatabaseException {
//        Device device = getDevice(deviceName);
//        if (device != null) {
//            return device.getCommands();
//        } else {
//            return null;
//        }
//    }
//
//    public boolean setCommandsOutput(String deviceName, String commandsOutput) throws AuthDatabaseException {
//        return update(new Device(deviceName, null, null, null, null, null, null, commandsOutput));
//    }
//
//    public String getCommandsOutput(String deviceName) throws AuthDatabaseException {
//        Device device = getDevice(deviceName);
//        if (device != null) {
//            return device.getCommandsOutput();
//        } else {
//            return null;
//        }
//    }
//
//}