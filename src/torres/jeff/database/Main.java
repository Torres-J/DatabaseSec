package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;


public class Main {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, ParserConfigurationException, IOException {
		
		CreateFolderStructure.createFolders();
		Connection db = new Connections().ConnectDB();
		CreateTables.createTables();
		Triggers.setTriggers(db);
		//XccdfReader.go(db);
		StigUpdater u = new StigUpdater();
		u.runStigUpdate(db);
		//db.createStatement().execute("DELETE FROM dbo.stig_table where stig like '%MICRO%'");
	}
}
