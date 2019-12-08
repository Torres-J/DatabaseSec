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
		StigUpdater u = new StigUpdater();
		u.unzip(db);
		Asset_Importer.importAssets(db);
		//XccdfReader.go(db);
		BiExporter bI = new BiExporter();
		bI.exportBiFiles(db);

	}
}
