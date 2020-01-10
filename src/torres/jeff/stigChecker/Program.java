package torres.jeff.stigChecker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Program {
	
	private Connection db;
	
	public Program(Connection dbo) {
		db = dbo;
	}
	
	public void startProgram() throws SQLException {
		ResultSet rs = db.createStatement().executeQuery("SELECT * FROM DBO.CONFIG");	
	}
	
}
