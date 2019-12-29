package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

public class ScheduledTasks {
	
	
	private static int startTime;
	private static int intervalTime;
	private static ScheduledExecutorService executorService;
	private static ScheduledExecutorService executorServiceBackup;

	public static boolean continueLoop = true;
	public static boolean workflowRunning = false;
	public static boolean backupWorkflowLoop = true;
	
	public static void executeTasks(Connection db, StigUpdater stigUpdater, ACAS acas, BiExporter bI, boolean threadsEnabled) throws InterruptedException, SQLException {
		if (threadsEnabled == true) {
			ResultSet rsStartTime = db.createStatement().executeQuery("SELECT startTime FROM DBO.CONFIG WHERE startTime IS NOT NULL"); 
			ResultSet rsIntervalTime = db.createStatement().executeQuery("SELECT intervalTime FROM DBO.CONFIG WHERE intervalTime IS NOT NULL"); 
			executorService = Executors.newScheduledThreadPool(1);
			while (rsStartTime.next()) {
				startTime = rsStartTime.getInt("startTime");
			}
			if (rsIntervalTime.next() == false) {
				intervalTime = 30;
			} else if (!rsIntervalTime.wasNull()) {
				intervalTime = rsIntervalTime.getInt("intervalTime");
			}
			while (continueLoop) {
				int minute = LocalDateTime.now().getMinute();
				if (minute == startTime) {
					// Creates a new thread that executes a series of tasks on a periodic basis based on the importNewVulnerabilityMinutes
					executorService.scheduleAtFixedRate(new Runnable() {
					    public void run() {
					    	try {
						    	if (workflowRunning == false) {
						    		workflowRunning = true;
						    		// Creates the directory and folders from where the JAR file is ran to interact with the DB
						    		Gui.addProgress();
						    		CreateFolderStructure.createFolders(db);
						    		// Imports new asset files that were imported before
						    		Gui.addProgress();
						    		Asset_Importer.importAssets(db);
						    		// Imports the STIG's from the STIG Drop folder
						    		Gui.addProgress();
						    		stigUpdater.unzip(db);
						    		// Parses CKL's
						    		Gui.addProgress();
						    		CKL_Parser.CKLParserStart(db);
						    		// Parses xccdf from SCAP
						    		Gui.addProgress();
						    		XccdfReader.go(db);
						    		// After the import of CKL's and XCCDF files, the workflow keeps the first occurrence of a vulnerability by host and the date it was remediated
						    		Gui.addProgress();
						    		MainWorkflow.startWorkflow(db);
						    		// Parses ACAS file. Data is always overwritten. The file must always contain all current vulnerabilities
						    		acas.beginParsingACAS();
						    		Gui.addProgress();
						    		// Writes the primary tables. If a CSV is open for some reason, blocking the writing of a new file, the current connections will be closed so new CSV's can be written
						    		Gui.addProgress();
						    		bI.exportBiFiles(db);
						    		Gui.resetProgress();
						    		workflowRunning = false;
						    		System.out.println("done");
					    		}
							} catch (SQLException | IOException | InterruptedException | ParserConfigurationException e) {
								// For troubleshooting purposes. Only visible by me during program execution in Eclipse IDE
								e.printStackTrace();
							}
					    }
					}, 0, intervalTime, TimeUnit.MINUTES);
					break;
				}
				Thread.sleep(1000);
			}
		}
	}
	
	public static void immediateWorkflowExecution(Connection db, StigUpdater stigUpdater, ACAS acas, BiExporter bI) {
			try {
				if (workflowRunning == false) {
		    		workflowRunning = true;
		    		// Creates the directory and folders from where the JAR file is ran to interact with the DB
		    		Gui.addProgress();
		    		CreateFolderStructure.createFolders(db);
		    		// Imports new asset files that were imported before
		    		Gui.addProgress();
		    		Asset_Importer.importAssets(db);
		    		// Imports the STIG's from the STIG Drop folder
		    		Gui.addProgress();
		    		stigUpdater.unzip(db);
		    		// Parses CKL's
		    		Gui.addProgress();
		    		CKL_Parser.CKLParserStart(db);
		    		// Parses xccdf from SCAP
		    		Gui.addProgress();
		    		XccdfReader.go(db);
		    		// After the import of CKL's and XCCDF files, the workflow keeps the first occurrence of a vulnerability by host and the date it was remediated
		    		Gui.addProgress();
		    		MainWorkflow.startWorkflow(db);
		    		// Parses ACAS file. Data is always overwritten. The file must always contain all current vulnerabilities
		    		acas.beginParsingACAS();
		    		Gui.addProgress();
		    		// Writes the primary tables. If a CSV is open for some reason, blocking the writing of a new file, the current connections will be closed so new CSV's can be written
		    		Gui.addProgress();
		    		bI.exportBiFiles(db);
		    		Gui.resetProgress();
		    		System.out.println("done");
		    		workflowRunning = false;
				} else if (workflowRunning == true) {
					
				}
				} catch (SQLException | IOException | InterruptedException | ParserConfigurationException e) {
					// For troubleshooting purposes. Only visible by me during program execution in Eclipse IDE
					e.printStackTrace();
			}
		
	}
	public static void backupDatabase(Connection db, boolean threadsEnabled) throws SQLException, InterruptedException {
		if (threadsEnabled == true) {
			executorServiceBackup = Executors.newScheduledThreadPool(1);
			while (backupWorkflowLoop) {
				int hour = LocalDateTime.now().getHour();
				int minute = LocalDateTime.now().getMinute();
				if (hour == 23 & minute == 59) {
					executorServiceBackup.scheduleAtFixedRate(new Runnable() {
					    public void run() {
					    	while (true) {
						    	if (workflowRunning == false) {
						    		workflowRunning = true;
						    		try {
										db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE('" + CreateFolderStructure.backupDirectoryLocation.toString() + "')");
										workflowRunning = false;
										break;
									} catch (SQLException e) {
										e.printStackTrace();
									}
						    	}
						    	try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
						    }
					    }
					}, 0, 1440, TimeUnit.MINUTES);
					break;
				}
				Thread.sleep(2000);
			}
		}
	}
	public static void destroyThread() {
		if (!executorService.isShutdown()) {
			executorService.shutdownNow();
		} else {
		}
	}
	public static void destroyThreadBackup() {
		if (!executorServiceBackup.isShutdown()) {
			executorServiceBackup.shutdownNow();
		} else {
		}
	}
	public static void continueOrStopWhileLoop(boolean bool) {
		continueLoop = bool;
	}
	
}


















