package it.richkmeli.rms.data.rmc;

import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.database.DatabaseManager;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.data.rmc.model.RMCModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RMCDatabaseManager extends DatabaseManager implements RMCModel {

    public RMCDatabaseManager() throws DatabaseException {
        schemaName = "AuthSchema";
        tableName = schemaName + "." + "rmc";
        table = "(" +
                "user VARCHAR(50)," +
                "rmcId VARCHAR(68) NOT NULL," +
                "PRIMARY KEY (user, rmcId)," +
                "FOREIGN KEY (user) REFERENCES auth(email) ON DELETE CASCADE" +
                ")";

        init();
    }

    @Override
    public boolean addRMC(RMC client) throws DatabaseException {
        add(client);
        return false;
    }

    @Override
    public boolean editRMC(RMC client) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET user = ? WHERE rmcId = ? AND user = ?");
            // arguments that will be edited
            preparedStatement.setString(1, client.getUser());
            preparedStatement.setString(2, client.getRmcId());
            preparedStatement.setString(3, "");

            preparedStatement.executeUpdate();
        } catch (SQLException | DatabaseException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
        }
        disconnect(connection, preparedStatement, null);
        return false;
    }

    @Override
    public boolean removeRMC(String id) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE rmcId = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | DatabaseException e) {
            disconnect(connection, preparedStatement, null);
            throw new DatabaseException(e);
            //return false;
        }
        disconnect(connection, preparedStatement, null);
        return false;
    }

    public List<RMC> getRMCs() throws DatabaseException {
        return getRMCs("");
    }

    @Override
    public List<RMC> getRMCs(String user) throws DatabaseException {
        Connection connnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<RMC> rmcs = new ArrayList<>();

        try {
            connnection = connect();
            String query = user == "" ? "SELECT * FROM " + tableName : "SELECT * FROM " + tableName + " WHERE user = ?";
            preparedStatement = connnection.prepareStatement(query);
            if (user != "") {
                preparedStatement.setString(1, user);
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RMC tmp = new RMC(
                        resultSet.getString("user"),
                        resultSet.getString("rmcId"));
                // add to the list the devices of the relative user.
                rmcs.add(tmp);

            }
        } catch (SQLException | DatabaseException e) {
            disconnect(connnection, preparedStatement, null);
            throw new DatabaseException(e);
        }
        disconnect(connnection, preparedStatement, null);
        return rmcs;
    }

    @Override
    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException {
        Connection connnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> accounts = new ArrayList<>();

        try {
            connnection = connect();
            preparedStatement = connnection.prepareStatement("SELECT * FROM " + tableName + "WHERE rmcId = ? AND user = ''");
            preparedStatement.setString(1, rmcID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accounts.add(resultSet.getString("user"));
            }
        } catch (SQLException | DatabaseException e) {
            disconnect(connnection, preparedStatement, null);
            throw new DatabaseException(e);
        }
        disconnect(connnection, preparedStatement, null);
        return accounts;
    }
}