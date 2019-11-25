package torres.jeff.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
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
		while (ThreadController.threadsActive) {
			TimeUnit.SECONDS.sleep(1);
			File inputFiles = CreateFolderStructure.workspacePathXccdfDrop;
			File[] inputFilesList = inputFiles.listFiles();
			for (File xmlFile : inputFilesList) {
				InputStream inputStream = new FileInputStream(xmlFile);
				try {
					Document doc = builder.parse(inputStream);
			        doc.getDocumentElement().normalize();
			        NodeList stigList = doc.getElementsByTagName("cdf:title");
			        for (int i = 0; i < 1; i++) {
			            Node nNode = stigList.item(i);
			            String stig = nNode.getTextContent();
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
			        	String targetHost = nNode.getTextContent();
			        	sysInfo.add(targetHost);	
			        }
			        
			        for (int i = 0; i < results.size(); i++) {
			        	   	
			        	String export = sysInfo.get(1).replace(",", "','") + "','" + vulnIDList.get(i) + "','" + results.get(i).toString().replace(" ", "").replace(",", ",") + "','" + sysInfo.get(0).replace(",", ",");
			        	db.createStatement().execute("INSERT INTO dbo.Stage_xc (Host_Name, V_ID, Status, STIG) VALUES ('" + export + "')");
			     
			        }  

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
			try {
				db.createStatement().execute("INSERT INTO dbo.METRICS (Host_Name) SELECT DISTINCT Host_Name FROM dbo.STAGE_XC");
	        	db.createStatement().execute("DELETE FROM dbo.Stage_xc");
	        	db.createStatement().execute("DELETE FROM dbo.Ongoing " + 
	        			"WHERE EXISTS (" + 
	        			"SELECT * " + 
	        			"FROM DBO.ONGOING " + 
	        			"JOIN DBO.Completed ON dbo.completed.host_name = dbo.ongoing.host_name AND dbo.completed.V_ID = dbo.Ongoing.V_ID AND dbo.completed.STIG = dbo.Ongoing.STIG " + 
	        			"WHERE DBO.Completed.Date_Found > DBO.Ongoing.Date_Found)");
			}
	        catch (Exception e) {
	        	errorLog.log(Level.SEVERE, "XccdfReader Database Appending Error", e);
	        }
	        		
	        }
		}
	}				
