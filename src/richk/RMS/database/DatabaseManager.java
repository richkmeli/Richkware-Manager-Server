package richk.RMS.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import richk.RMS.model.Device;
import richk.RMS.model.Model;
import richk.RMS.model.ModelException;

public class DatabaseManager implements Model{
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;

	public DatabaseManager() throws DatabaseException{
		ResourceBundle resource = ResourceBundle.getBundle("configuration");
		
		if(resource.getString("database.name").compareTo("openshift") == 0){
			dbUrl = "jdbc:mysql://" + "postgresql://" + System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST") + ":" + System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT");
			dbUsername = resource.getString("database.username");
			dbPassword = resource.getString("database.password");
		}
		else if (resource.getString("database.name").compareTo("apache") == 0){
			dbUrl = resource.getString("database.url");
			String dbClass = resource.getString("database.class");

			try {
				Class.forName(dbClass);
			} catch (ClassNotFoundException e) {
				throw new DatabaseException(e);
			}
		}
		else{
			throw new DatabaseException(null);
		}
	}

	private Connection connect() throws DatabaseException{
		try {
			return DriverManager.getConnection(dbUrl);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	private void disconnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws DatabaseException{
		try {
			resultSet.close();
		}catch(SQLException e){
			throw new DatabaseException(e);
		}catch (Exception e1) {		
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

	@Override
	public List<Device> RefreshDevice() throws ModelException {
		List<Device> deviceList = new ArrayList<>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try{
			connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			preparedStatement = connection.prepareStatement("SELECT * FROM ?");
			preparedStatement.setString(1, "DEVICE");
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()){
				Device tmp = new Device(resultSet.getInt("ID"),resultSet.getString("NAME"),resultSet.getString("IP"),resultSet.getBoolean("ONLINE"),resultSet.getString("LASTCONNECTION"));
				deviceList.add(tmp);
			}
		}
		catch(SQLException e){
			disconnect(connection, preparedStatement, resultSet);
		}
		disconnect(connection, preparedStatement, resultSet);
		return deviceList;
	}

	@Override
	public boolean AddDevice(Device device) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try{
			connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			preparedStatement = connection.prepareStatement	("INSERT INTO ? (ID, NAME, IP, ONLINE, LASTCONNECTION) VALUES (?,?,?,TRUE,?)");
			preparedStatement.setString(1,"DEVICE");
			preparedStatement.setInt(2, device.getID());
			preparedStatement.setString(3, device.getName());
			preparedStatement.setString(4, device.getIP());
			preparedStatement.setString(6, device.getLastConnection());
			resultSet = preparedStatement.executeQuery();
		}
		catch(SQLException e){
			disconnect(connection, preparedStatement, resultSet);
			return false;
		}
		disconnect(connection, preparedStatement, resultSet);
		return true;
	}


	@Override
	public boolean RemoveDevice(int ID) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try{
			connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			preparedStatement = connection.prepareStatement	("DELETE * FROM ? WHERE ID = ?");
			preparedStatement.setString(1, "DEVICE");
			preparedStatement.setInt(2, ID);
			resultSet = preparedStatement.executeQuery();
		}
		catch(SQLException e){
			disconnect(connection, preparedStatement, resultSet);
			return false;
		}
		disconnect(connection, preparedStatement, resultSet);
		return true;
	}
}