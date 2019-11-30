package torres.jeff.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void runStigUpdate() throws ParserConfigurationException {
    	File stigDrop = new File("WorkSpace/STIG_Drop");
    	File[] listXML = stigDrop.listFiles();
    	for (File xmlDoc : listXML) {
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
    			currStig = stig;
    			
    			for (int i = 0; i < vIDList.size(); i++) {
    				
    			}
    			
    			
    		} catch (Exception e) {
    			
    		}
    		
    	}
    	
    }

}
