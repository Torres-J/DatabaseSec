package torres.jeff.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, ParserConfigurationException, IOException {
		/*
		CreateFolderStructure.createFolders();
		Connection db = new Connections().ConnectDB();
		CreateTables.createTables();
		Triggers.setTriggers(db);
		XccdfReader.go(db);
		*/
		StigUpdater u = new StigUpdater();
		u.runStigUpdate();
	}
}
