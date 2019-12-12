package torres.jeff.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
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

	public static void main(String[] args) throws SecurityException, IOException, ParserConfigurationException {
		
		// remove this block before integrating with xccdfreader
		CreateFolderStructure.createFolders();
		// remove this block before integrating with xccdfreader
		
		Logger errorLog = new ErrorLogging().logger(XccdfReader.class.getName(), "XccdfReader.log", Level.WARNING);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		ArrayList<String> sysInfo = new ArrayList<String>();
		ArrayList<String> vulnIDList = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		String hostName = null;
		String stigName = null;
		//PreparedStatement pS = db.prepareStatement("INSERT INTO dbo.Stage_xc (Host_Name, V_ID, Status, STIG) VALUES (?,?,?,?)");
		File inputFiles = CreateFolderStructure.workspacePathXccdfDrop;
		File[] inputFilesList = inputFiles.listFiles();
		if (inputFilesList.length > 0) {
			for (File xmlFile : inputFilesList) {
				System.out.println(xmlFile.toString());
				InputStream inputStream = new FileInputStream(xmlFile);
				try {
					Document doc = builder.parse(inputStream);
			        doc.getDocumentElement().normalize();
			        NodeList hostNameNode = doc.getElementsByTagName("HOST_NAME");
			        for (int i = 0; i < 1; i++) {
			            Node nNode = hostNameNode.item(i);
			            hostName = nNode.getTextContent().toUpperCase();			            
			        }
			        NodeList stig = doc.getElementsByTagName("SI_DATA");
			        for (int i = 0; i < 1; i++) {
			            Node nNode = stig.item(7);
			            Element eElement = (Element) nNode;
			            stigName = eElement.getElementsByTagName("SID_DATA").item(0).getTextContent();
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
			        

				} 
		        catch (Exception e) {
		        	e.printStackTrace();
		        }
				}
			}
		}
}

