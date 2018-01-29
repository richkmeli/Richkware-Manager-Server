package richk.RMS.database;

import richk.RMS.model.Device;
import richk.RMS.model.Model;
import richk.RMS.model.ModelException;
import richk.RMS.model.User;
import richk.RMS.util.Crypto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatabaseManager implements Model {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String schemaDbName;
    private String tableDbName;
    private String authTableDbName;

    public DatabaseManager() throws DatabaseException {
        ResourceBundle resource = ResourceBundle.getBundle("configuration");
        String dbClass = null;

//        if (resource.getString("database.name").equals("mysql")) {
        dbUsername = resource.getString("database.username");
        dbPassword = resource.getString("database.password");
        dbUrl = resource.getString("database.url");
//            schemaDbName = "RichkwareMS";
/*        } else if (resource.getString("database.name").equals("openshift_mysql")) {
            dbUsername = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
            dbPassword = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
            dbUrl = "jdbc:" + "mysql://" + System.getenv("OPENSHIFT_MYSQL_DB_HOST") + ":" + System.getenv("OPENSHIFT_MYSQL_DB_PORT") + "/";
            schemaDbName = System.getenv("OPENSHIFT_APP_NAME");
        }
*/
        dbClass = "com.mysql.jdbc.Driver";
        try {
            Class.forName(dbClass);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException(e);
        }

        // database schema creation
//        tableDbName = schemaDbName + ".device";
        try {
            schemaDbName = "RichkwareMS";

            createSchema();
            dbUrl += schemaDbName;


            tableDbName = schemaDbName + ".device";
            authTableDbName = schemaDbName + ".user";
            createTables();

//            CreateDeviceSchema();
//            dbUrl += schemaDbName;
//            CreateDeviceTable();
        } catch (ModelException e) {
            throw new DatabaseException(e);
        }
    }

    private Connection connect() throws DatabaseException {
        try {
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private void disconnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws DatabaseException {
        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } catch (Exception e1) {        // null value of ResultSet in addDevice, removeDevice...
        }
        try {
            preparedStatement.close();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        try {
            connection.close();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

    public boolean createSchema() throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String schemaSQL = "CREATE SCHEMA " + schemaDbName;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement(schemaSQL);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public boolean createTables() throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String authTableSQL = "CREATE TABLE " + authTableDbName + "(" +
                "email VARCHAR(50) NOT NULL PRIMARY KEY," +
                "pass VARCHAR(64) NOT NULL," +
                "isAdmin BOOLEAN NOT NULL DEFAULT 0" +
                ")";

        String tableSQL = "CREATE TABLE " + tableDbName + "(" +
                "Name VARCHAR(50) NOT NULL PRIMARY KEY," +
                "IP VARCHAR(25) NOT NULL," +
                "ServerPort VARCHAR(10)," +
                "LastConnection VARCHAR(25)," +
                "EncryptionKey VARCHAR(32)," +
                "UserAssociated VARCHAR(50) REFERENCES user(email)" +
                ")";

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement(authTableSQL);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(tableSQL);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public List<Device> refreshDevice() throws ModelException {
        List<Device> deviceList = new ArrayList<Device>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableDbName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Device tmp = new Device(
                        resultSet.getString("Name"),
                        resultSet.getString("IP"),
                        resultSet.getString("ServerPort"),
                        resultSet.getString("LastConnection"),
                        resultSet.getString("EncryptionKey"),
                        resultSet.getString("UserAssociated"));
                deviceList.add(tmp);
            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new ModelException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return deviceList;
    }


    public List<Device> refreshDevice(String user) throws ModelException {
        List<Device> deviceList = new ArrayList<Device>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableDbName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Device tmp = new Device(
                        resultSet.getString("Name"),
                        resultSet.getString("IP"),
                        resultSet.getString("ServerPort"),
                        resultSet.getString("LastConnection"),
                        resultSet.getString("EncryptionKey"),
                        resultSet.getString("UserAssociated"));
                // add to the list the devices of the relative user.
                if(user.compareTo(resultSet.getString("UserAssociated"))==0){
                    deviceList.add(tmp);
                }
            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new ModelException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return deviceList;
    }


    public List<User> refreshUser() throws ModelException {
        List<User> userList = new ArrayList<User>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + authTableDbName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User tmp = new User(
                        resultSet.getString("email"),
                        resultSet.getString("pass"),
                        resultSet.getBoolean("isAdmin"));
                userList.add(tmp);
            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new ModelException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return userList;
    }

    public boolean addDevice(Device device) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("INSERT INTO " + tableDbName + " (name, ip, serverport, lastconnection, encryptionkey, userAssociated) VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1, device.getName());
            preparedStatement.setString(2, device.getIP());
            preparedStatement.setString(3, device.getServerPort());
            preparedStatement.setString(4, device.getLastConnection());
            preparedStatement.setString(5, device.getEncryptionKey());
            preparedStatement.setString(6, device.getUserAssociated());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new ModelException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public boolean editDevice(Device device) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + tableDbName + " SET ip = ?, serverport = ?, lastconnection = ?, encryptionkey = ? WHERE name = ?");
            preparedStatement.setString(1, device.getIP());
            preparedStatement.setString(2, device.getServerPort());
            preparedStatement.setString(3, device.getLastConnection());
            preparedStatement.setString(4, device.getEncryptionKey());
            preparedStatement.setString(5, device.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new ModelException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public Device getDevice(String name) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Device device = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableDbName + " WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                device = new Device(resultSet.getString("Name"), resultSet.getString("IP"), resultSet.getString("ServerPort"), resultSet.getString("LastConnection"), resultSet.getString("EncryptionKey"), resultSet.getString("UserAssociated"));
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
        }
        disconnect(connection, preparedStatement, resultSet);
        return device;
    }


    public boolean removeDevice(String name) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableDbName + " WHERE name = ?");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new ModelException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public String getEncryptionKey(String name) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String encryptionKey = "";

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + tableDbName + " WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                encryptionKey = resultSet.getString("EncryptionKey");
            }
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
            throw new ModelException(e);
        }
        disconnect(connection, preparedStatement, resultSet);
        return encryptionKey;
    }


    public boolean addUser(User user) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();

            String hash = Crypto.HashSHA256(user.getPassword());

            preparedStatement = connection.prepareStatement("INSERT INTO " + authTableDbName + " (email, pass, isAdmin) VALUES (?,?,?)");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, hash);
            preparedStatement.setBoolean(3,user.isAdmin());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new ModelException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public boolean isUserPresent(String email) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isPresent = false;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + authTableDbName + " WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isPresent = true;
            }

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
        }
        disconnect(connection, preparedStatement, resultSet);
        return isPresent;
    }

    public boolean editPassword(String email, String pass) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + authTableDbName + " SET pass = ? WHERE email = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new ModelException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }

    public boolean editAdmin(String email, Boolean isAdmin) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + authTableDbName + " SET isAdmin = ? WHERE email = ?");
            preparedStatement.setBoolean(1, isAdmin);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            disconnect(connection, preparedStatement, null);
            throw new ModelException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return true;
    }


    public boolean checkPassword(String email, String pass) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isPass = false;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + authTableDbName + " WHERE email = ?");
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

    public boolean isAdmin(String email) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isAdmin = false;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + authTableDbName + " WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getBoolean("isAdmin")) {
                    isAdmin = true;
                }

            }

        } catch (SQLException e) {
            disconnect(connection, preparedStatement, resultSet);
        }
        disconnect(connection, preparedStatement, resultSet);
        return isAdmin;
    }

}