package torres.jeff.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;


public class Main {
	
	// The driver, what the DB first does when initiating itself
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, ParserConfigurationException, IOException, URISyntaxException {
		
		// Creates the driver that connects with the DB to the program. This is passed on to most classes so concurrency is appropriately handled
		Connection db = new Connections().ConnectDB();
		// Creates the database tables upon first running needed to store the data
		CreateTables.createTables(db);
		// Creates the directory and folders from where the JAR file is ran to interact with the DB
		CreateFolderStructure.createFolders(db);
		// Creates the STIG Importer object so that it doesn't have to be instantiated again
		StigUpdater stigUpdater = new StigUpdater();
		// Creates the bI Object that allows exporting of data to CSV
		BiExporter bI = new BiExporter();
		// Creates the ACAS object
		ACAS acas = new ACAS(db);
		// Starts sockets
		SocketsServer startSocket = new SocketsServer(db);
		startSocket.setLocalPort(20127);
		startSocket.startServer();
		// Starts the GUI
		Gui.run(db, stigUpdater, bI, acas, startSocket);
	}
}
