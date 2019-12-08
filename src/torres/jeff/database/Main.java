package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.xml.parsers.ParserConfigurationException;


public class Main {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, ParserConfigurationException, IOException {
		
		CreateFolderStructure.createFolders();
		Connection db = new Connections().ConnectDB();
		CreateTables.createTables();
		Triggers.setTriggers(db);
		Asset_Importer.importAssets(db);
		StigUpdater u = new StigUpdater();
		u.unzip(db);
		BiExporter bI = new BiExporter();
		bI.exportBiFiles(db);
		//XccdfReader.go(db);
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
		/*
		executorService.execute(new Runnable() {
		    public void run() {
		    	try {
					u.unzip(db);
				} catch (ParserConfigurationException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		executorService.execute(new Runnable() {
		    public void run() {
		    	try {
		    		Asset_Importer.importAssets(db);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		
		executorService.execute(new Runnable() {
		    public void run() {
		    	try {
		    		XccdfReader.go(db);
				} catch (SQLException | IOException | InterruptedException | ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		
		executorService.execute(new Runnable() {
		    public void run() {
		    	try {
		    		bI.exportBiFiles(db);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		*/
		
		//u.unzip(db);
		//Asset_Importer.importAssets(db);
		//XccdfReader.go(db);
		
		//bI.exportBiFiles(db);

	}
}
