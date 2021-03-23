package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class DatabaseConnection {

	private static DatabaseConnection DBConnection = null;

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/";
	private static final String DBName = "bookstore";
	private static final  String username = "root";
	private static final String password = "12345678";
	private static Connection connection;

	/*
	 * private constructor restricted to this class itself
	 */
	private DatabaseConnection() {

	}

	/*
	* static method to create instance of Singleton class
	*/
	public static DatabaseConnection getInstance() {

		if (DBConnection == null) {
			DBConnection = new DatabaseConnection();
			DBConnection.connect();
		}
		return DBConnection;
	}
	
	private void connect() {
		// Driver registration
		try {
			Class.forName(JDBC_DRIVER);

		} catch (ClassNotFoundException e) {
			System.out.println("An error during loading driver class!");
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(DB_URL + DBName, username, password);
		} catch (Exception e) {
			System.out.println("An error during getting connection");
			e.printStackTrace();
		}
	}

	/*
	 * Getters
	 */
	public  String getDBName() {
		return DBName;
	}

	public  String getUsername() {
		return username;
	}

	public  String getPassword() {
		return password;
	}

	public  Connection getConnection() {
		return connection;
	}

	/*
	 * closing connection
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("An error during closing connection");
			e.printStackTrace();
		}
	}

}
