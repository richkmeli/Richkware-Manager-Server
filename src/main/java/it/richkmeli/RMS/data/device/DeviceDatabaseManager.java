package it.richkmeli.RMS.data.device;

import it.richkmeli.RMS.data.device.model.Device;
import it.richkmeli.RMS.data.device.model.DeviceModel;
import it.richkmeli.jcrypto.Crypto;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.database.DatabaseManager;

import java.sql.*;
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
                "userAssociated VARCHAR(50) REFERENCES user(email)" +
                ")";

        init();
    }


    public List<Device> refreshDevice() throws DatabaseException {
        List<Device> deviceList = new ArrayList<Device>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Device tmp = new Device(
                        resultSet.getString("name"),
                        resultSet.getString("ip"),
                        resultSet.getString("serverPort"),
                        resultSet.getString("lastConnection"),
                        resultSet.getString("encryptionKey"),
                        resultSet.getString("userAssociated"));
                deviceList.add(tmp);
            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new DatabaseException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return deviceList;
    }


    public List<Device> refreshDevice(String user) throws DatabaseException {
        List<Device> deviceList = new ArrayList<Device>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Device tmp = new Device(
                        resultSet.getString("name"),
                        resultSet.getString("ip"),
                        resultSet.getString("serverPort"),
                        resultSet.getString("lastConnection"),
                        resultSet.getString("encryptionKey"),
                        resultSet.getString("userAssociated"));
                // add to the list the devices of the relative user.
                if (user.compareTo(resultSet.getString("userAssociated")) == 0) {
                    deviceList.add(tmp);
                }
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
            preparedStatement = connection.prepareStatement("INSERT IGNORE INTO " + tableName + " (name, ip, serverPort, lastConnection, encryptionKey, userAssociated) VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1, device.getName());
            preparedStatement.setString(2, device.getIp());
            preparedStatement.setString(3, device.getServerPort());
            preparedStatement.setString(4, device.getLastConnection());
            preparedStatement.setString(5, device.getEncryptionKey());
            preparedStatement.setString(6, device.getUserAssociated());
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
                device = new Device(resultSet.getString("name"), resultSet.getString("ip"), resultSet.getString("serverPort"), resultSet.getString("lastConnection"), resultSet.getString("encryptionKey"), resultSet.getString("userAssociated"));
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