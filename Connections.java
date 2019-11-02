package torres.stig.program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Connections {
	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String JDBC_URL = "jdbc:derby:MyDB;create=true";
	
	public static Connection ConnectDB() throws SQLException, ClassNotFoundException {
		Class.forName(DRIVER);
		Connection connection = DriverManager.getConnection(JDBC_URL);
		return connection;
	}
	
	public static ResultSet resultSet(String query) throws ClassNotFoundException, SQLException {
		ResultSet results = ConnectDB().createStatement().executeQuery(query);
		return results;
	}
	
}
