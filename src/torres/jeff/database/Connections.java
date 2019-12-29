package torres.jeff.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connections {
	// This is the embedded driver library
	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	// This creates the database in the local directory from where it's started
	private static final String JDBC_URL = "jdbc:derby:VulnerabilityDB;create=true";
	
	// This method is passed on to every other class to ensure concurrency
	public Connection ConnectDB() throws SQLException, ClassNotFoundException {
		Class.forName(DRIVER);
		Connection connection = DriverManager.getConnection(JDBC_URL);
		return connection;
	}
}
