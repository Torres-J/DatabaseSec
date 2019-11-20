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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XccdfReader {

	public static void go(Connection db) throws SQLException {
		
		File inputFile = new File("C:\\Users\\Torre\\git\\DatabaseSec\\XCCDFEXAMPLE.XML");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		ArrayList<String> sysInfo = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        NodeList stigList = doc.getElementsByTagName("cdf:title");
	        for (int i = 0; i < 1; i++) {
	            Node nNode = stigList.item(i);
	            String stig = nNode.getTextContent();
	            sysInfo.add(stig);
	        }
	        
	        NodeList vID = doc.getElementsByTagName("cdf:rule-result");
	        for (int i = 0; i < vID.getLength(); i++) {
	        	NodeList check = doc.getElementsByTagName("cdf:result");
	        	Node nNode = vID.item(i);
	        	Node nNode1 = check.item(i);
	        	String vulnID = nNode.getAttributes().getNamedItem("idref").toString().replace("idref=\"xccdf_mil.disa.stig_rule_", "").replace("\"", "");
	        	String ruleResult = nNode1.getTextContent().toString();
	        	results.add(vulnID + ", " + ruleResult);	
	        }
	        
	        NodeList hostName = doc.getElementsByTagName("cdf:target");
	        for (int i = 0; i < 1; i++) {
	        	Node nNode = hostName.item(i);
	        	String targetHost = nNode.getTextContent();
	        	sysInfo.add(targetHost);	
	        }
	        
	        for (int i = 0; i < results.size(); i++) {
	        	   	
	        	String export = sysInfo.get(1).replace(",", "','") + "','" + results.get(i).toString().replace(" ", "").replace(",", "','") + "','" + sysInfo.get(0).replace(",", "','");
	        	db.createStatement().execute("INSERT INTO dbo.Stage_xc (Host_Name, V_ID, Status, STIG) VALUES ('" + export + "')");
	        	
	        }
        	db.createStatement().execute("DELETE FROM dbo.Stage_xc");

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
