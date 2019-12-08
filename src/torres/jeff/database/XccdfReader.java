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
					System.out.println(xmlFile.toString());
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
				        	//String export = sysInfo.get(1).replace(",", "','") + "','" + vulnIDList.get(i) + "','" + results.get(i).toString().replace(" ", "").replace(",", ",") + "','" + sysInfo.get(0).replace(",", ",");
				        	
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
				try {
					db.createStatement().execute("INSERT INTO dbo.METRICS (Host_Name) SELECT DISTINCT Host_Name FROM dbo.STAGE_XC");
		        	
					db.createStatement().execute("INSERT INTO DBO.ONGOING (V_ID, Host_Name, Status, STIG, Date_Found) "
							+ "Select DISTINCT dbo.STAGE_XC.V_ID, dbo.STAGE_XC.Host_Name, dbo.STAGE_XC.Status, DBO.Stig_Table.STIG, dbo.STAGE_XC.Date_Found from dbo.STAGE_XC " + 
							"JOIN dbo.Stig_Table ON dbo.STAGE_XC.V_ID = dbo.Stig_Table.V_ID " + 
							"WHERE dbo.STAGE_XC.Status = 'fail' AND dbo.STAGE_XC.STIG Like dbo.Stig_Table.STIG");
					
					db.createStatement().execute(" INSERT INTO DBO.COMPLETED (V_ID, Host_Name, Status, STIG, Date_Found) "
							+ "Select DISTINCT dbo.STAGE_XC.V_ID, dbo.STAGE_XC.Host_Name, dbo.STAGE_XC.Status, DBO.Stig_Table.STIG, dbo.STAGE_XC.Date_Found from dbo.STAGE_XC " + 
							"JOIN dbo.Stig_Table ON dbo.STAGE_XC.V_ID = dbo.Stig_Table.V_ID " + 
							"WHERE dbo.STAGE_XC.Status = 'pass' AND dbo.STAGE_XC.STIG Like dbo.Stig_Table.STIG");	
					
					db.createStatement().execute("DELETE FROM dbo.Stage_xc");	
					/*
					db.createStatement().execute("DELETE FROM dbo.Ongoing "
							+ "WHERE DATE_FOUND NOT IN (SELECT MIN(DATE_FOUND) FROM DBO.ONGOING  " 
							+ "GROUP BY HOST_NAME, V_ID, STIG)");
					
					db.createStatement().execute("DELETE FROM dbo.Completed "
							+ "WHERE DATE_FOUND NOT IN (SELECT MIN(DATE_FOUND) FROM DBO.Completed " 
							+ "GROUP BY HOST_NAME, V_ID, STIG)");		
					*/
					
					db.createStatement().execute("INSERT INTO DBO.COMPLETED_TEMP (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
							"SELECT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM (SELECT Host_Name, V_ID, STATUS, STIG, MIN(DATE_FOUND) as "
							+ "DATE_FOUND FROM DBO.COMPLETED GROUP BY HOST_NAME, V_ID, Status, STIG) as f");
					
					db.createStatement().execute("DELETE FROM dbo.Completed");
					
					db.createStatement().execute("INSERT INTO DBO.COMPLETED (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
							"SELECT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM DBO.COMPLETED_TEMP");
					
					db.createStatement().execute("DELETE FROM dbo.Completed_Temp");
					
					db.createStatement().execute("INSERT INTO DBO.ONGOING_TEMP (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
							"SELECT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM (SELECT Host_Name, V_ID, STATUS, STIG, MIN(DATE_FOUND) as "
							+ "DATE_FOUND FROM DBO.ONGOING GROUP BY HOST_NAME, V_ID, Status, STIG) as f");
					
					db.createStatement().execute("DELETE FROM dbo.Ongoing");
					
					db.createStatement().execute("INSERT INTO DBO.Ongoing (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
							"SELECT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM DBO.ONGOING_TEMP");
					
					db.createStatement().execute("DELETE FROM DBO.ONGOING_TEMP");
					
					db.createStatement().execute("DELETE FROM dbo.Completed " + 
		        			"WHERE CUST_ID IN (" + 
		        			"SELECT S.CUST_ID " + 
		        			"FROM DBO.Completed AS S " + 
		        			"JOIN DBO.Ongoing ON dbo.Ongoing.host_name = S.host_name AND dbo.Ongoing.V_ID = S.V_ID AND dbo.Ongoing.STIG = S.STIG " + 
		        			"WHERE DBO.Ongoing.Date_Found > S.Date_Found)");
					
		        	db.createStatement().execute("DELETE FROM dbo.Ongoing " + 
		        			"WHERE CUST_ID IN (" + 
		        			"SELECT S.CUST_ID " + 
		        			"FROM DBO.ONGOING AS S " + 
		        			"JOIN DBO.Completed ON dbo.completed.host_name = S.host_name AND dbo.completed.V_ID = S.V_ID AND dbo.completed.STIG = S.STIG " + 
		        			"WHERE DBO.Completed.Date_Found > S.Date_Found)");
		        	
		        	db.createStatement().execute("DELETE from DBO.MAIN_TABLE " + 
		        			"WHERE CUST_ID NOT IN (" + 
		        			"select CUST_ID " + 
		        			"from DBO.ONGOING " + 
		        			") and DBO.MAIN_TABLE.status = 'fail'");
		        	
		        	db.createStatement().execute("DELETE from DBO.MAIN_TABLE " + 
		        			"WHERE CUST_ID NOT IN (" + 
		        			"select CUST_ID " + 
		        			"from DBO.Completed " + 
		        			") and DBO.MAIN_TABLE.status = 'pass'");
		        	
		        	db.createStatement().execute("INSERT INTO dbo.Main_Table (CUST_ID, Group_Org, Host_Name, V_ID, Severity, Status, Title, Check_Text, Fix_Text, STIG, Date_Found) "
		        			+ "SELECT DISTINCT dbo.Ongoing.CUST_ID, dbo.Assets.OU, dbo.Ongoing.Host_Name, dbo.Ongoing.V_ID, dbo.Stig_Table.Severity, dbo.Ongoing.Status, dbo.Stig_Table.Title, dbo.Stig_Table.Check_Text, dbo.Stig_Table.Fix_Text, dbo.Stig_Table.STIG, dbo.Ongoing.Date_Found "
		        			+ "FROM dbo.Ongoing "
		        			+ "LEFT JOIN dbo.Assets ON dbo.Assets.Host_Name = dbo.Ongoing.Host_Name "
		        			+ "JOIN dbo.Stig_Table ON dbo.Ongoing.V_ID = dbo.Stig_Table.V_ID "
		        			+ "LEFT JOIN dbo.Main_Table ON dbo.Main_Table.CUST_ID = dbo.Ongoing.CUST_ID "
		        			+ "WHERE dbo.Ongoing.Stig = dbo.Stig_Table.Stig AND dbo.Main_Table.CUST_ID IS NULL");
		        	
		        	db.createStatement().execute("INSERT INTO dbo.Main_Table (CUST_ID, Group_Org, Host_Name, V_ID, Severity, Status, Title, Check_Text, Fix_Text, STIG, Date_Found) "
		        			+ "SELECT DISTINCT dbo.Completed.CUST_ID, dbo.Assets.OU, dbo.Completed.Host_Name, dbo.Completed.V_ID, dbo.Stig_Table.Severity, dbo.Completed.Status, dbo.Stig_Table.Title, dbo.Stig_Table.Check_Text, dbo.Stig_Table.Fix_Text, dbo.Stig_Table.STIG, dbo.Completed.Date_Found "
		        			+ "FROM dbo.Completed "
		        			+ "LEFT JOIN dbo.Assets ON dbo.Assets.Host_Name = dbo.Completed.Host_Name "
		        			+ "JOIN dbo.Stig_Table ON dbo.Completed.V_ID = dbo.Stig_Table.V_ID "
		        			+ "LEFT JOIN dbo.Main_Table ON dbo.Main_Table.CUST_ID = dbo.Completed.CUST_ID "
		        			+ "WHERE dbo.Completed.Stig = dbo.Stig_Table.Stig AND dbo.Main_Table.CUST_ID IS NULL");
		        	
		        	System.out.println("done");
		        			
				}
		        catch (Exception e) {
		        	errorLog.log(Level.SEVERE, "XccdfReader Database Appending Error", e);
		        	e.printStackTrace();
		        }
				}	
		        
		}
	}				
