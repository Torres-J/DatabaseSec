package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;


public class Main {
	
	// importNewVulnerabilityMinutes sets the interval in which the database runs through it's program. It's NOT "Real Time" (A design choice that would have added too much overhead)
	final private static int importNewVulnerabilityMinutes = 30;

	// The driver, what the DB first does when initiating itself. Note: immediate import of data will happen after importNewVulnerabilityMinutes time is reached, unless the program is
	// exited and started again after creation
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, ParserConfigurationException, IOException {
		// Creates the directory and folders from where the JAR file is ran to interact with the DB
		CreateFolderStructure.createFolders();
		// Creates the driver that connects with the DB to the program. This is passed on to most classes so concurrency is appropriately handled
		Connection db = new Connections().ConnectDB();
		// Creates the database tables upon first running needed to store the data
		CreateTables.createTables();
		// Imports the assets. An asset is a hostname from SCAP, a CKL or ACAS field that correlates to a CSV that has to be generated manually for grouping like systems with like systems
		Asset_Importer.importAssets(db);
		// Creates the STIG Importer object so that it doesn't have to be instantiated again
		StigUpdater u = new StigUpdater();
		// Imports the STIG's from the STIG Drop folder. Data inputed without a correlated STIG will be disregarded. This was done to ensure integrity
		u.unzip(db);
		// Creates or initiates the first exported CSV's. If a CSV is open for some reason, blocking the writing of a new file, the current connections will be closed so new CSV's can be written
		BiExporter bI = new BiExporter();
		// Creates a new thread that executes a series of tasks on a periodic basis based on the importNewVulnerabilityMinutes
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleAtFixedRate(new Runnable() {
		    public void run() {
		    	try {
		    		// Imports new asset files that were imported before
		    		Asset_Importer.importAssets(db);
		    		// Imports the STIG's from the STIG Drop folder
		    		u.unzip(db);
		    		// Parses CKL's
		    		CKL_Parser.CKLParserStart(db);
		    		// Parses xccdf from SCAP
		    		XccdfReader.go(db);
		    		// After the import of CKL's and XCCDF files, the workflow keeps the first occurance of a vulnerability by host and the date it was remediated
		    		MainWorkflow.startWorkflow(db);
		    		// Writes the primary tables. If a CSV is open for some reason, blocking the writing of a new file, the current connections will be closed so new CSV's can be written
		    		bI.exportBiFiles(db);
				} catch (SQLException | IOException | InterruptedException | ParserConfigurationException e) {
					// For troubleshooting purposes. Only visable by me during program execution in Eclipse IDE
					e.printStackTrace();
				}
		    }
		}, 0, importNewVulnerabilityMinutes, TimeUnit.MINUTES);

	}
}
