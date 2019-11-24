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
		
		CreateFolderStructure.createFolders();
		Connection db = new Connections().ConnectDB();
		CreateTables.createTables();
		Triggers.setTriggers(db);
		XccdfReader.go(db);
		
		
		/*File inputFile = new File("C:\\Users\\Torre\\git\\DatabaseSec\\XCCDFEXAMPLE.XML");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        NodeList vID = doc.getElementsByTagName("cdf:Group");
	        for (int i = 0; i < vID.getLength(); i++) {
	        	Node nNode = vID.item(i);
        		NamedNodeMap v = nNode.getAttributes();
        		String vulnID = v.getNamedItem("id").toString().replace("id=\"xccdf_mil.disa.stig_group_", "").replace("\"", "");
        		System.out.println(vulnID);            
	            
	        } 
	        
		} catch (Exception e) {e.printStackTrace();}
		*/
	}
}
