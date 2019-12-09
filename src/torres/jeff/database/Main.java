package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;


public class Main {
	
	final private static int stigUpdateTimeMinutes = 5;
	final private static int importAssetsMinutes = 5;
	final private static int importNewVulnerabilitySeconds = 30;
	final private static int exportBIFilesMinutes = 30;

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
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
/*
		executorService.scheduleAtFixedRate(new Runnable() {
		    public void run() {
		    	try {
					u.unzip(db);
				} catch (ParserConfigurationException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}, 0, stigUpdateTimeMinutes, TimeUnit.MINUTES);
		
		executorService.scheduleAtFixedRate(new Runnable() {
		    public void run() {
		    	try {
		    		Asset_Importer.importAssets(db);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}, 0, importAssetsMinutes, TimeUnit.MINUTES);
*/	
		
		executorService.scheduleAtFixedRate(new Runnable() {
		    public void run() {
		    	try {
		    		Asset_Importer.importAssets(db);
		    		u.unzip(db);
		    		XccdfReader.go(db);
		    		bI.exportBiFiles(db);
				} catch (SQLException | IOException | InterruptedException | ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}, 0, importNewVulnerabilitySeconds, TimeUnit.MINUTES);
/*	
		executorService.scheduleAtFixedRate(new Runnable() {
		    public void run() {
		    	try {
		    		bI.exportBiFiles(db);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}, 0, exportBIFilesMinutes, TimeUnit.MINUTES);
*/		

	}
}
