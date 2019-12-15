package torres.jeff.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CKL_Parser {

	public static void CKLParserStart(Connection db) throws SecurityException, IOException, ParserConfigurationException, SQLException {

		Logger errorLog = new ErrorLogging().logger(XccdfReader.class.getName(), "XccdfReader.log", Level.WARNING);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		ArrayList<String> sysInfo = new ArrayList<String>();
		ArrayList<String> vulnIDList = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> stigList = new ArrayList<String>();
		String hostName = null;
		String[] stigName = null;
		PreparedStatement pS = db.prepareStatement("INSERT INTO dbo.Stage_xc (Host_Name, V_ID, Status, STIG) VALUES (?,?,?,?)");
		File inputFiles = CreateFolderStructure.workspacePathCKLDrop;
		File[] inputFilesList = inputFiles.listFiles();
		if (inputFilesList.length > 0) {
			for (File xmlFile : inputFilesList) {
				//System.out.println(xmlFile.toString());
				InputStream inputStream = new FileInputStream(xmlFile);
				try {
					Document doc = builder.parse(inputStream);
			        doc.getDocumentElement().normalize();
			        			        
			        NodeList hostNameNode = doc.getElementsByTagName("HOST_NAME");
			        for (int i = 0; i < 1; i++) {
			            Node nNode = hostNameNode.item(i);
			            hostName = nNode.getTextContent().toUpperCase();			            
			        }
			        NodeList stig = doc.getElementsByTagName("VULN");
			        for (int i = 0; i < stig.getLength(); i++) {
			            Node nNode = stig.item(i);
			            Element eElement = (Element) nNode;
			            stigName = eElement.getElementsByTagName("STIG_DATA").item(22).getTextContent().replace("STIGRef", "").trim().split("::");
			            stigList.add(stigName[0].trim().toUpperCase());
			           
			        }
			        NodeList vID = doc.getElementsByTagName("VULN");
			        for (int i = 0; i < vID.getLength(); i++) {
			            Node nNode = vID.item(i);
			            Element eElement = (Element) nNode;
			            String vIDName = eElement.getElementsByTagName("ATTRIBUTE_DATA").item(0).getTextContent();	
			            vulnIDList.add(vIDName);  
			        }
			        NodeList status = doc.getElementsByTagName("STATUS");
			        for (int i = 0; i < status.getLength(); i++) {
			            Node nNode = status.item(i);
			            String statusValue = nNode.getTextContent();
			            if (statusValue.contains("Open")) {
			            	statusValue = "fail";
			            } else if (statusValue.contains("NotAFinding")) {
			            	statusValue = "pass";
			            }
			            results.add(statusValue);  
			        }
			        for (int i = 0; i < results.size(); i++) {			        	   		        	
			        	pS.setString(1, hostName);
			        	pS.setString(2, vulnIDList.get(i));
			        	pS.setString(3, results.get(i));
			        	pS.setString(4, stigList.get(i));
			        	pS.addBatch();
			        	//System.out.println(hostName + ", " + vulnIDList.get(i) + ", " + results.get(i) + ", " + stigList.get(i));
			        } 
			        pS.executeBatch();
				} 
		        catch (Exception e) {
		        	errorLog.log(Level.WARNING, xmlFile.getName().toString() + " Could not parse", e);
					inputStream.close();
					xmlFile.delete();
					sysInfo.clear();
					vulnIDList.clear();
					results.clear();
					continue;
		        }
		        inputStream.close();
				xmlFile.delete();
				sysInfo.clear();
				vulnIDList.clear();
				results.clear();
				}
			}
		}
}

