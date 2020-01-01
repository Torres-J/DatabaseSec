package torres.jeff.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ACAS {
	
	private Connection db;
	private boolean deleteOldRecords = true;
	private String line;
	private int pluginL,pluginNameL,familyL,severityL,iPAddressL,protocolL,portL,macAddressL,
				dnsNameL,netBIOSNameL,pluginTextL,firstDiscoveredL,lastObservedL,
				vulnPublicationDateL,patchPublicationDateL,pluginPublicationDateL;
	private String pluginS,pluginNameS,familyS,severityS,iPAddressS,protocolS,portS,macAddressS,
	dnsNameS,netBIOSNameS,pluginTextS,firstDiscoveredS,lastObservedS,
	vulnPublicationDateS,patchPublicationDateS,pluginPublicationDateS;
	
	public ACAS(Connection dbo) {
		this.db = dbo;
	}
	
	public void beginParsingACAS() throws IOException, SQLException, InterruptedException {

		for (File f : CreateFolderStructure.workspacePathACASDrop.listFiles()) {
			Process p = Runtime.getRuntime().exec("cmd.exe /c taskkill /f /FI \"windowtitle eq " + f.getName() + "*\"");
		}
		Thread.sleep(2000);
		for (File f : CreateFolderStructure.workspacePathACASDrop.listFiles()) {
			ArrayList<String> index = new ArrayList<String>();
			LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(new FileInputStream(f)));
			try {			
				while (((line = lineReader.readLine()) != null) && lineReader.getLineNumber() <= 1) {
					String[] rows = line.split(",");
					for (int i = 0; i < rows.length; i++) {
						
						if (rows[i].contentEquals("Plugin")) {
							pluginL = i + 1;
							pluginS = "Plugin";
							index.add(pluginL + "," + pluginS);		
						}
						else if (rows[i].contentEquals("Plugin Name")) {
							pluginNameL = i + 1;
							pluginNameS = "Plugin_Name";
							index.add(pluginNameL + "," + pluginNameS);
						}
						else if (rows[i].contentEquals("Family")) {
							familyL = i + 1;
							familyS = "Family";
							index.add(familyL + "," + familyS);
						}
						else if (rows[i].contentEquals("Severity")) {
							severityL = i + 1;
							severityS = "Severity";
							index.add(severityL + "," + severityS);
						}
						else if (rows[i].contentEquals("IP Address")) {
							iPAddressL = i + 1;
							iPAddressS = "IP_Address";
							index.add(iPAddressL + "," + iPAddressS);
						}
						else if (rows[i].contentEquals("Protocol")) {
							protocolL = i + 1;
							protocolS = "Protocol";
							index.add(protocolL + "," + protocolS);
						}
						else if (rows[i].contentEquals("Port")) {
							portL = i + 1;
							portS = "Port";
							index.add(portL + "," + portS);
						}
						else if (rows[i].contentEquals("MAC Address")) {
							macAddressL = i + 1;
							macAddressS = "MAC_Address";
							index.add(macAddressL + "," + macAddressS);
						}
						else if (rows[i].contentEquals("DNS Name")) {
							dnsNameL = i + 1;
							dnsNameS = "DNS_Name";
							index.add(dnsNameL + "," + dnsNameS);
						}
						else if (rows[i].contentEquals("NetBIOS Name")) {
							netBIOSNameL = i + 1;
							netBIOSNameS = "NetBIOS_Name";
							index.add(netBIOSNameL + "," + netBIOSNameS);
						}
						else if (rows[i].contains("Plugin Text")) {
							pluginTextL = i + 1;
							pluginTextS = "Plugin_Text";
							index.add(pluginTextL + "," + pluginTextS);
						}
						else if (rows[i].equalsIgnoreCase("First Discovered")) {
							firstDiscoveredL = i + 1;
							firstDiscoveredS = "First_Discovered";
							index.add(firstDiscoveredL + "," + firstDiscoveredS);
						}
						else if (rows[i].equalsIgnoreCase("Last Observed")) {
							lastObservedL = i + 1;
							lastObservedS = "Last_Observed";
							index.add(lastObservedL + "," + lastObservedS);
						}
						else if (rows[i].equalsIgnoreCase("Vuln Publication Date")) {
							vulnPublicationDateL = i + 1;
							vulnPublicationDateS = "Vuln_Publication_Date";
							index.add(vulnPublicationDateL + "," + vulnPublicationDateS);
						}
						else if (rows[i].equalsIgnoreCase("Patch Publication Date")) {
							patchPublicationDateL = i + 1;
							patchPublicationDateS = "Patch_Publication_Date";
							index.add(patchPublicationDateL + "," + patchPublicationDateS);
						}
						else if (rows[i].equalsIgnoreCase("Plugin Publication Date")) {
							pluginPublicationDateL = i + 1;
							pluginPublicationDateS = "Plugin_Publication_Date";
							index.add(pluginPublicationDateL + "," + pluginPublicationDateS);
						}						
					}
				}
				if (severityL != 0 & netBIOSNameL != 0 & dnsNameL != 0 & iPAddressL != 0 & pluginL != 0) {
					ArrayList<String> columnNumbers = new ArrayList<String>();
					ArrayList<String> columnNames = new ArrayList<String>();
					String[] rows;
					for (String indexRow : index) {
						rows = indexRow.split(",");
						columnNumbers.add(rows[0]);		
						columnNames.add(rows[1]);
					}
					String columnNumbersString = columnNumbers.toString().replace("[", "").replace("]", "").replace(" " , "");
					String columnNamesString = columnNames.toString().replace("[", "").replace("]", "").replace(" ", "").toUpperCase();
					if (deleteOldRecords == true) {
						deleteOldRecords = false;
						db.createStatement().execute("DELETE FROM DBO.ACAS");
						db.createStatement().execute("INSERT INTO dbo.ACAS (Group_Org, Plugin, Plugin_Name, Family, Severity, IP_Address, Protocol, Port, MAC_Address, DNS_Name, NetBIOS_Name,"
								+ "Plugin_Text, First_Discovered, Last_Observed, Vuln_Publication_Date, Patch_Publication_Date, Plugin_Publication_Date) VALUES "
								+ "('Group','Plugin','Plugin Name','Family','Severity','IP Address','Protocol','Port','MAC Address','DNS Name','NetBIOS Name','Plugin Text','First Discovered',"
								+ "'Last Observed','Vuln Publication Date','Patch Publication Date','Plugin Publication Date')");
						db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_IMPORT_DATA('DBO', 'ACAS','" + columnNamesString + "','" + columnNumbersString + "','" + f.toString() + "', null, null, null, 1)");
						acasWorkflow();
						resetIndex();
					} else if (deleteOldRecords == false) {
						db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_IMPORT_DATA('DBO', 'ACAS','" + columnNamesString + "','" + columnNumbersString + "','" + f.toString() + "', null, null, null, 1)");
						acasWorkflow();
						resetIndex();
					}
				}	
				lineReader.close();
			} catch (Exception e) {
				resetIndex();
				f.delete();
				e.printStackTrace();
			}
			f.delete();
		}
		deleteOldRecords = true;		
	}
	
	private void acasWorkflow() throws SQLException {
		db.createStatement().execute("UPDATE DBO.ACAS " + 
				"SET NETBIOS_NAME = DNS_NAME " + 
				"WHERE NETBIOS_NAME IS NULL OR NETBIOS_NAME = ''");
		db.createStatement().execute("UPDATE DBO.ACAS " + 
				"SET NETBIOS_NAME = IP_ADDRESS " + 
				"WHERE NETBIOS_NAME IS NULL OR NETBIOS_NAME = ''");
		db.createStatement().execute("UPDATE dbo.acas SET dbo.acas.GROUP_ORG = (SELECT dbo.assets.OU FROM dbo.assets WHERE dbo.assets.host_name like dbo.acas.netbios_name)");
		db.createStatement().execute("UPDATE dbo.acas SET dbo.acas.GROUP_ORG = 'Group' WHERE dbo.acas.Plugin = 'Plugin' AND dbo.acas.Plugin_Name = 'Plugin Name'");
		db.createStatement().execute("UPDATE DBO.ACAS SET GROUP_ORG = '' WHERE GROUP_ORG IS NULL");
	}
	
	private void resetIndex() {
		pluginL = 0;
		pluginNameL = 0;
		familyL = 0; 
		severityL = 0;
		iPAddressL = 0;
		protocolL = 0;
		portL = 0;
		macAddressL = 0;
		dnsNameL = 0;
		netBIOSNameL = 0;
		pluginTextL = 0;
		firstDiscoveredL = 0;
		lastObservedL = 0;
		vulnPublicationDateL = 0;
		patchPublicationDateL = 0;
		pluginPublicationDateL = 0;
	}
}

	// Old method below. Could not get it to properly iterate over Plugin_Text field due to new embedded newlines. Saving this in case the above is slow
	/*
	public void beginParsingACAS() throws IOException, SQLException {
		PreparedStatement pS = db.prepareStatement("INSERT INTO dbo.ACAS (Plugin, Plugin_Name, Family, Severity, IP_Address, Protocol, Port, MAC_Address, DNS_Name, NetBIOS_Name,"
				+ "Plugin_Text, First_Discovered, Last_Observed, Vuln_Publication_Date, Patch_Publication_Date, Plugin_Publication_Date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		for (File f : CreateFolderStructure.workspacePathACASDrop.listFiles()) {
			LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(new FileInputStream(f)));
			BufferedReader csvReader = new BufferedReader(new FileReader(f));
			try {			
				while (((line = lineReader.readLine()) != null) && lineReader.getLineNumber() <= 1) {
					String[] rows = line.split(",");
					for (int i = 0; i < rows.length; i++) {
						if (rows[i].contentEquals("Plugin")) {
							pluginL = i;
						}
						else if (rows[i].contentEquals("Plugin Name")) {
							pluginNameL = i;
						}
						else if (rows[i].contentEquals("Family")) {
							familyL = i;
						}
						else if (rows[i].contentEquals("Severity")) {
							severityL = i;
						}
						else if (rows[i].contentEquals("IP Address")) {
							iPAddressL = i;
						}
						else if (rows[i].contentEquals("Protocol")) {
							protocolL = i;
						}
						else if (rows[i].contentEquals("Port")) {
							portL = i;
						}
						else if (rows[i].contentEquals("MAC Address")) {
							macAddressL = i;
						}
						else if (rows[i].contentEquals("DNS Name")) {
							dnsNameL = i;
						}
						else if (rows[i].contentEquals("NetBIOS Name")) {
							netBIOSNameL = i;
						}
						else if (rows[i].contains("Plugin Text")) {
							pluginTextL = i;
						}
						else if (rows[i].equalsIgnoreCase("First Discovered")) {
							firstDiscoveredL = i;
						}
						else if (rows[i].equalsIgnoreCase("Last Observed")) {
							lastObservedL = i;
						}
						else if (rows[i].equalsIgnoreCase("Vuln Publication Date")) {
							vulnPublicationDateL = i;
						}
						else if (rows[i].equalsIgnoreCase("Patch Publication Date")) {
							patchPublicationDateL = i;
						}
						else if (rows[i].equalsIgnoreCase("Plugin Publication Date")) {
							pluginPublicationDateL = i;
						}
					}
				}
				lineReader.close();
				
				csvReader.readLine();
				row = null;
				while ((row = csvReader.readLine()) != null) {
					String[] rows = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
					
					if (pluginL == 0) {
						pS.setString(1, rows[pluginL]);
					} else if (pluginL == 100) {
						break;
					}
					if (pluginNameL == 0) {
						pS.setString(2, "");
					} else {
						pS.setString(2, rows[pluginNameL]);
					}
					if (familyL == 0) {
						pS.setString(3, "");
					} else {
						pS.setString(3, rows[familyL]);
					}
					if (severityL == 0) {
						pS.setString(4, "");
					} else {
						pS.setString(4, rows[severityL]);
					}
					if (iPAddressL == 0) {
						pS.setString(5, "");
					} else {
						pS.setString(5, rows[iPAddressL]);
					}
					if (protocolL == 0) {
						pS.setString(6, "");
					} else {
						pS.setString(6, rows[protocolL]);
					}
					if (portL == 0) {
						pS.setString(7, "");
					} else {
						pS.setString(7, rows[portL]);
					}
					if (macAddressL == 0) {
						pS.setString(8, "");
					} else {
						pS.setString(8, rows[macAddressL]);
					}
					if (dnsNameL == 0) {
						pS.setString(9, "");
					} else {
						pS.setString(9, rows[dnsNameL].toUpperCase());
					}
					if (netBIOSNameL == 0) {
						pS.setString(10, "");
					} else {
						pS.setString(10, rows[netBIOSNameL].toUpperCase());
					}
					if (pluginTextL == 0) {
						pS.setString(11, "");
					} else {
						pS.setString(11, rows[pluginTextL]);
					}
					if (firstDiscoveredL == 0) {
						pS.setString(12, "");
					} else {
						pS.setString(12, rows[firstDiscoveredL].replace("\"", ""));
					}
					if (lastObservedL == 0) {
						pS.setString(13, "");
					} else {
						pS.setString(13, rows[lastObservedL].replace("\"", ""));
					}
					if (vulnPublicationDateL == 0) {
						pS.setString(14, "");
					} else {
						pS.setString(14, rows[vulnPublicationDateL].replace("\"", ""));
					}
					if (patchPublicationDateL == 0) {
						pS.setString(15, "");
					} else {
						pS.setString(15, rows[patchPublicationDateL].replace("\"", ""));
					}
					if (pluginPublicationDateL == 0) {
						pS.setString(16, "");
					} else {
						pS.setString(16, rows[pluginPublicationDateL].replace("\"", ""));
					}
					pS.addBatch();
					rowCounter++;
					if (rowCounter % 1000 == 0) {
						if (deleteOldRecords == true) {
							db.createStatement().execute("DELETE FROM DBO.ACAS");
							db.createStatement().execute("INSERT INTO dbo.ACAS (Group_Org, Plugin, Plugin_Name, Family, Severity, IP_Address, Protocol, Port, MAC_Address, DNS_Name, NetBIOS_Name,"
									+ "Plugin_Text, First_Discovered, Last_Observed, Vuln_Publication_Date, Patch_Publication_Date, Plugin_Publication_Date) VALUES "
									+ "('Group','Plugin','Plugin Name','Family','Severity','IP Address','Protocol','Port','MAC Address','DNS Name','NetBIOS Name','Plugin Text','First Discovered',"
									+ "'Last Observed','Vuln Publication Date','Patch Publication Date','Plugin Publication Date')");
							deleteOldRecords = false;
						}
						pS.executeBatch();
						acasWorkflow();
					}
				}
				if (deleteOldRecords == true) {
					db.createStatement().execute("DELETE FROM DBO.ACAS");
					db.createStatement().execute("INSERT INTO dbo.ACAS (Group_Org, Plugin, Plugin_Name, Family, Severity, IP_Address, Protocol, Port, MAC_Address, DNS_Name, NetBIOS_Name,"
							+ "Plugin_Text, First_Discovered, Last_Observed, Vuln_Publication_Date, Patch_Publication_Date, Plugin_Publication_Date) VALUES "
							+ "('Group','Plugin','Plugin Name','Family','Severity','IP Address','Protocol','Port','MAC Address','DNS Name','NetBIOS Name','Plugin Text','First Discovered',"
							+ "'Last Observed','Vuln Publication Date','Patch Publication Date','Plugin Publication Date')");
					deleteOldRecords = false;
				}
				pS.executeBatch();
				acasWorkflow();
				csvReader.close();
				pluginL = 100;
				pluginNameL = 0;
				familyL = 0; 
				severityL = 0;
				iPAddressL = 0;
				protocolL = 0;
				portL = 0;
				macAddressL = 0;
				dnsNameL = 0;
				netBIOSNameL = 0;
				pluginTextL = 0;
				firstDiscoveredL = 0;
				lastObservedL = 0;
				vulnPublicationDateL = 0;
				patchPublicationDateL = 0;
				pluginPublicationDateL = 0;
				f.delete();
				
		} catch (Exception e) {
			e.printStackTrace();
			csvReader.close();
			lineReader.close();
			pluginL = 100;
			pluginNameL = 0;
			familyL = 0; 
			severityL = 0;
			iPAddressL = 0;
			protocolL = 0;
			portL = 0;
			macAddressL = 0;
			dnsNameL = 0;
			netBIOSNameL = 0;
			pluginTextL = 0;
			firstDiscoveredL = 0;
			lastObservedL = 0;
			vulnPublicationDateL = 0;
			patchPublicationDateL = 0;
			pluginPublicationDateL = 0;
			f.delete();
		}
	}
		deleteOldRecords = true;
	}
	 */
















