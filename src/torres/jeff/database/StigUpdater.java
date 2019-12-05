package torres.jeff.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* public static void main(String[] args) throws IOException {
		StigUpdater zip = new StigUpdater();
		String userDir = System.getProperty("user.dir");
		File zipFile = new File(userDir);
		for (File f : zipFile.listFiles()) {
			if (f.toString().contains(".zip")) {
				String fi = f.toString();
				zip.unzip(fi, userDir);
			}
		}

	}*/

public class StigUpdater {

    public void unzip(Connection db) throws ParserConfigurationException, SQLException {
    	String mainDirectory = System.getProperty("user.dir");
		String workSpace = mainDirectory + "/WorkSpace";
		String stigDrop = workSpace + "/STIG_Drop";
        File dir = new File(stigDrop);
        for (File f: dir.listFiles()) {  
	        // create output directory if it doesn't exist
	        if(!dir.exists()) dir.mkdirs();
	        FileInputStream fis;
	        //buffer for read and write data to file
	        byte[] buffer = new byte[1024];
	        try {
	            fis = new FileInputStream(f);
	            ZipInputStream zis = new ZipInputStream(fis);
	            ZipEntry ze = zis.getNextEntry();
	            while(ze != null){
	                String fileName = ze.getName();
	                File newFile = new File(dir + File.separator + fileName);
	                if (newFile.toString().contains("xccdf")) {
	                //create directories for sub directories in zip
	                new File(newFile.getParent()).mkdirs();
	                FileOutputStream fos = new FileOutputStream(newFile);
	                int len;
	                while ((len = zis.read(buffer)) > 0) {
	                fos.write(buffer, 0, len);
	                }
	                fos.close();
	                }
	                //close this ZipEntry
	                zis.closeEntry();
	                ze = zis.getNextEntry();
	            }
	            
	            //close last ZipEntry
	            zis.closeEntry();
	            zis.close();
	            fis.close();
	            f.delete();
	            
	      
	    		File folderList = new File(stigDrop);
	    		for (File folder : folderList.listFiles()) {
	    			for (File file : folder.listFiles()) {
	    				runStigUpdate(db, file);
	    				file.delete();
	    			}
	    			folder.delete();
	    		}
	        } catch (IOException e) {
	            //e.printStackTrace();
	        }
        }
        
    }
    
    private void runStigUpdate(Connection db, File xmlDoc) throws ParserConfigurationException, SQLException {
    	PreparedStatement pS = db.prepareStatement("INSERT INTO dbo.Stig_Table (V_ID,Severity,Title,Check_Text,Fix_Text,Stig) VALUES (?,?,?,?,?,?)");
		try {
			ArrayList<String> vIDList = new ArrayList<String>();
			ArrayList<String> severityList = new ArrayList<String>();
			ArrayList<String> titleList = new ArrayList<String>();
			ArrayList<String> checkTextList = new ArrayList<String>();
			ArrayList<String> fixTextList = new ArrayList<String>();
			String currStig;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		Document doc = builder.parse(xmlDoc);
    		
    		NodeList nList = doc.getElementsByTagName("Group");
    		for (int i =0; i < nList.getLength(); i++) {			
    			Node nNode = nList.item(i);
    			String vID = nNode.getAttributes().getNamedItem("id").getTextContent();
    			vIDList.add(vID);
    		}
    		NodeList rule = doc.getElementsByTagName("Rule");
			for (int ii=0; ii < rule.getLength(); ii++) {
				Node ruleNode = rule.item(ii);
				String severity = ruleNode.getAttributes().getNamedItem("severity").getTextContent();
				severityList.add(severity);
			}
			//NodeList title = doc.getElementsByTagName("Rule");
			for (int ii=0; ii < rule.getLength(); ii++) {
				Node ruleNode = rule.item(ii);
				Element elm = (Element) ruleNode;
				String titles = elm.getElementsByTagName("title").item(0).getTextContent();
				titleList.add(titles);
			}
			NodeList checkNode = doc.getElementsByTagName("check-content");
			for (int ii=0; ii < checkNode.getLength(); ii++) {
				Node ruleNode = checkNode.item(ii);
				String checkText = ruleNode.getTextContent();
				checkTextList.add(checkText);
			}
			NodeList fixNode = doc.getElementsByTagName("fixtext");
			for (int ii=0; ii < fixNode.getLength(); ii++) {
				Node ruleNode = fixNode.item(ii);
				String fixText = ruleNode.getTextContent();
				fixTextList.add(fixText);
			}
			NodeList stigTitle = doc.getElementsByTagName("title");
			Node ruleNode = stigTitle.item(0);
			String stig = ruleNode.getTextContent();
			currStig = stig.toUpperCase();
			db.createStatement().execute("DELETE FROM dbo.Stig_Table WHERE Stig = '" + currStig + "'");
			for (int i = 0; i < vIDList.size(); i++) {
				pS.setString(1, vIDList.get(i));
				pS.setString(2, severityList.get(i));
				pS.setString(3, titleList.get(i));
				pS.setString(4, checkTextList.get(i));
				pS.setString(5, fixTextList.get(i));
				pS.setString(6, currStig);
				pS.execute();
			}
			
			
			db.createStatement().execute("delete from dbo.Ongoing "
					+ "WHERE CUST_ID NOT IN ("
					+ "SELECT dbo.Ongoing.CUST_ID "
					+ "FROM dbo.Ongoing "
					+ "JOIN dbo.Stig_Table ON dbo.Stig_Table.V_ID = dbo.Ongoing.V_ID AND dbo.Stig_Table.STIG = dbo.Ongoing.STIG "
					+ "WHERE dbo.Stig_Table.V_ID IS NOT NULL AND dbo.Stig_Table.Stig IS NOT NULL)");
			
			db.createStatement().execute("delete from dbo.Completed "
					+ "WHERE CUST_ID NOT IN ("
					+ "SELECT dbo.Completed.CUST_ID "
					+ "FROM dbo.Completed "
					+ "JOIN dbo.Stig_Table ON dbo.Stig_Table.V_ID = dbo.Completed.V_ID AND dbo.Stig_Table.STIG = dbo.Completed.STIG "
					+ "WHERE dbo.Stig_Table.V_ID IS NOT NULL AND dbo.Stig_Table.Stig IS NOT NULL)");
			
			db.createStatement().execute("delete from dbo.Main_Table "
					+ "WHERE CUST_ID NOT IN ("
					+ "SELECT dbo.Main_Table.CUST_ID "
					+ "FROM dbo.Main_Table "
					+ "JOIN dbo.Stig_Table ON dbo.Stig_Table.V_ID = dbo.Main_Table.V_ID AND dbo.Stig_Table.STIG = dbo.Main_Table.STIG "
					+ "WHERE dbo.Stig_Table.V_ID IS NOT NULL AND dbo.Stig_Table.Stig IS NOT NULL)");
			
			db.createStatement().execute("UPDATE dbo.Main_Table "
					+ "SET dbo.Main_Table.V_ID = dbo.Stig_Table.V_ID,"
					+ "dbo.Main_Table.Severity = dbo.Stig_Table.Severity,"
					+ "dbo.Main_Table.Title = dbo.Stig_Table.Title,"
					+ "dbo.Main_Table.Check_Text = dbo.Stig_Table.Check_Text,"
					+ "dbo.Main_Table.Fix_Text = dbo.Stig_Table.Fix_Text,"
					+ "dbo.Main_Table.STIG = dbo.Stig_Table.STIG "
					+ "WHERE CUST_ID IN ("
					+ "SELECT dbo.Stig_Table.CUST_ID FROM dbo.Stig_Table "
					+ "JOIN dbo.Main_Table ON dbo.stig_table.V_ID = dbo.Main_Table.V_ID AND dbo.Stig_Table.STIG = dbo.Main_Table.STIG)");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
    	
    	
    }

}
