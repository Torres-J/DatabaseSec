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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XccdfReader {

	public static void go(Connection db) throws SQLException, InterruptedException, ParserConfigurationException, IOException {
		Logger errorLog = new ErrorLogging().logger(XccdfReader.class.getName(), "XccdfReader.log", Level.WARNING);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		ArrayList<String> sysInfo = new ArrayList<String>();
		ArrayList<String> vulnIDList = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		PreparedStatement pS = db.prepareStatement("INSERT INTO dbo.Stage_xc (Host_Name, V_ID, Status, STIG) VALUES (?,?,?,?)");

			File inputFiles = CreateFolderStructure.workspacePathXccdfDrop;
			File[] inputFilesList = inputFiles.listFiles();
			if (inputFilesList.length > 0) {
				for (File xmlFile : inputFilesList) {
					//System.out.println(xmlFile.toString());
					InputStream inputStream = new FileInputStream(xmlFile);
					try {
						Document doc = builder.parse(inputStream);
				        doc.getDocumentElement().normalize();
				        NodeList stigList = doc.getElementsByTagName("cdf:title");
				        for (int i = 0; i < 1; i++) {
				            Node nNode = stigList.item(i);
				            String stig = nNode.getTextContent().toUpperCase();
				            sysInfo.add(stig);
				            
				        }
				        NodeList vID = doc.getElementsByTagName("cdf:Group");
				        for (int i = 0; i < vID.getLength(); i++) {
				        	Node nNode = vID.item(i);
			        		NamedNodeMap v = nNode.getAttributes();
			        		String vulnID = v.getNamedItem("id").toString().replace("id=\"xccdf_mil.disa.stig_group_", "").replace("\"", "");
			        		vulnIDList.add(vulnID);
			        		
				        }
				        NodeList vIDs = doc.getElementsByTagName("cdf:rule-result");
				        for (int i = 0; i < vIDs.getLength(); i++) {
				        	NodeList check = doc.getElementsByTagName("cdf:result");
				        	Node nNode1 = check.item(i);
				        	String ruleResult = nNode1.getTextContent().toString();
				        	results.add(ruleResult);	
				        	
				        }
				        NodeList hostName = doc.getElementsByTagName("cdf:target");
				        for (int i = 0; i < 1; i++) {
				        	Node nNode = hostName.item(i);
				        	String targetHost = nNode.getTextContent().toUpperCase();
				        	sysInfo.add(targetHost);	
				        	
				        }
				        for (int i = 0; i < results.size(); i++) {			        	   	
				        	pS.setString(1, sysInfo.get(1));
				        	pS.setString(2, vulnIDList.get(i));
				        	pS.setString(3, results.get(i));
				        	pS.setString(4, sysInfo.get(0));
				        	pS.addBatch();
				        	//db.createStatement().execute("INSERT INTO dbo.Stage_xc (Host_Name, V_ID, Status, STIG) VALUES ('" + export + "')");
				        }  
				        pS.executeBatch();
					} catch (Exception e) {
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
