package it.richkmeli.RMS.data.device;

import it.richkmeli.RMS.data.device.model.Device;
import it.richkmeli.RMS.data.device.model.DeviceModel;
import it.richkmeli.jcrypto.Crypto;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceDatabaseManager extends DatabaseManager implements DeviceModel {

    public DeviceDatabaseManager() throws DatabaseException {
        schemaName = "DeviceSchema";
        tableName = schemaName + "." + "device";
        table = "(" +
                "name VARCHAR(50) NOT NULL PRIMARY KEY," +
                "ip VARCHAR(25) NOT NULL," +
                "serverPort VARCHAR(10)," +
                "lastConnection VARCHAR(25)," +
                "encryptionKey VARCHAR(32)," +
                "userAssociated VARCHAR(50) REFERENCES user(email)," +
                "commands TEXT," +
                "commandsOutput TEXT" +
                ")";

        init();
    }


    public List<Device> refreshDevice() throws DatabaseException {
        List<Device> deviceList = new ArrayList<Device>();

        deviceList = refreshDevice("");
//
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = connect();
//            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Device tmp = new Device(
//                        resultSet.getString("name"),
//                        resultSet.getString("ip"),
//                        resultSet.getString("serverPort"),
//                        resultSet.getString("lastConnection"),
//                        resultSet.getString("encryptionKey"),
//                        resultSet.getString("userAssociated"));
//                deviceList.add(tmp);
//            }
//        } catch (SQLException e) {
//            disconnect(connection, preparedStatement, resultSet);
//            throw new DatabaseException(e);
//        }
//        disconnect(connection, preparedStatement, resultSet);
        return deviceList;
    }


    public List<Device> refreshDevice(String user) throws DatabaseException {
        List<Device> deviceList = new ArrayList<Device>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            String query = user == "" ? "SELECT * FROM " + tableName : "SELECT * FROM " + tableName + " WHERE userAssociated = ?";
            preparedStatement = connection.prepareStatement(query);
            if (user != "") {
                preparedStatement.setString(1, user);
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Device tmp = new Device(
                        resultSet.getString("name"),
                        resultSet.getString("ip"),
                        resultSet.getString("serverPort"),
                        resultSet.getString("lastConnection"),
                        resultSet.getString("encryptionKey"),
                        resultSet.getString("userAssociated"),
                        resultSet.getString("commands"),
                        resultSet.getString("commandsOutput"));
                // add to the list the devices of the relative user.
                deviceList.add(tmp);

            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new DatabaseException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return deviceList;
    }

    /*public boolean addDevice(Device device) throws DatabaseException {
        return add(device);
    }*/

    public boolean addDevice(Device device) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("INSERT IGNORE INTO " + tableName + " (name, ip, serverPort, lastConnection, encryptionKey, userAssociated, commands, commandsOutput) VALUES (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, device.getName());
            preparedStatement.setString(2, device.getIp());
            preparedStatement.setString(3, device.getServerPort());
            preparedStatement.setString(4, device.getLastConnection());
            preparedStatement.setString(5, device.getEncryptionKey());
            preparedStatement.setString(6, device.getUserAssociated());
            preparedStatement.setString(7, device.getCommands());
            preparedStatement.setString(8, device.getCommandsOutput());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public boolean editDevice(Device device) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET ip = ?, serverPort = ?, lastConnection = ?, encryptionKey = ?, userAssociated = ? WHERE name = ?");
            // arguments that will be edited
            preparedStatement.setString(1, device.getIp());
            preparedStatement.setString(2, device.getServerPort());
            preparedStatement.setString(3, device.getLastConnection());
            preparedStatement.setString(4, device.getEncryptionKey());
            preparedStatement.setString(5, device.getUserAssociated());
            // SQL WHERE
            preparedStatement.setString(6, device.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public Device getDevice(String name) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Device device = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                device = new Device(resultSet.getString("name"), resultSet.getString("ip"), resultSet.getString("serverPort"), resultSet.getString("lastConnection"), resultSet.getString("encryptionKey"), resultSet.getString("userAssociated"), resultSet.getString("commands"), resultSet.getString("commandsOutput"));
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
        }
        disconnect(connection, preparedStatement, resultSet);
        return device;
    }


    public boolean removeDevice(String name) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE name = ?");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public String getEncryptionKey(String name) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String encryptionKey = "";

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                encryptionKey = resultSet.getString("encryptionKey");
            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new DatabaseException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return encryptionKey;
    }

    public boolean editCommands(String deviceName, String commands) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET commands = ? WHERE name = ?");
            // arguments that will be edited
            preparedStatement.setString(1, commands);
            preparedStatement.setString(2, deviceName);

            int modified = preparedStatement.executeUpdate();

            disconnect(connection, preparedStatement, null);

            if (modified > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("deviceName not found!");
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
//            return false;
        }
    }

    public String getCommands(String deviceName) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String commands = "";

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE name = ?");
            preparedStatement.setString(1, deviceName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                commands = resultSet.getString("commands");
            }
        } catch (SQLException | DatabaseException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new DatabaseException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return commands;
    }

    public boolean setCommandsOutput(String deviceName, String commandsOutput) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET commandsOutput = ? WHERE name = ?");
            // arguments that will be edited
            preparedStatement.setString(1, commandsOutput);
            preparedStatement.setString(2, deviceName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public String getCommandsOutput(String deviceName) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String commandsOutput = "";

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE name = ?");
            preparedStatement.setString(1, deviceName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                commandsOutput = resultSet.getString("commandsOutput");
            }
        } catch (SQLException | DatabaseException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new DatabaseException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return commandsOutput;
    }

    public boolean checkPassword(String email, String pass) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isPass = false;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            String hash = Crypto.HashSHA256(pass);

            if (resultSet.next()) {
                if (resultSet.getString("pass").compareTo(hash) == 0) {
                    isPass = true;
                }

            }

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
        }
        disconnect(connection, preparedStatement, resultSet);
        return isPass;
    }

}