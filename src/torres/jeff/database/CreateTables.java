package torres.jeff.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEvent;



public class CreateTables  {
	
	public static void createTables() throws ClassNotFoundException, SQLException {
		Connection db = new Connections().ConnectDB();
		try {
			db.createStatement().execute("CREATE TABLE dbo.Metrics (CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), OU varchar(255), Host_Name varchar(150), Date_Found DATE NOT NULL DEFAULT CURRENT_DATE)");
		} catch (Exception e) {

		} try {
			db.createStatement().execute("CREATE TABLE dbo.Assets (CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), OU varchar(255), Host_Name varchar(150))");
		} catch (Exception e) {
			
		}
		try {
		db.createStatement().execute("CREATE TABLE dbo.Stage_xc ("
				+ "V_ID varchar(15),"
				+ "Host_Name varchar(150),"
				+ "Status varchar(20),"
				+ "STIG varchar(300),"
				+ "Date_Found TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
		} catch (Exception e) {
			
		}
		try {
		db.createStatement().execute("CREATE TABLE dbo.Completed ("
				+ "CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 2),"
				+ "V_ID varchar(15),"
				+ "Host_Name varchar(150),"
				+ "Status varchar(20),"
				+ "STIG varchar(300),"
				+ "Date_Found TIMESTAMP NOT NULL)");
		} catch (Exception e) {
			
		}
		try {
			db.createStatement().execute("CREATE TABLE dbo.Completed_Temp ("
					+ "CUST_ID INT,"
					+ "V_ID varchar(15),"
					+ "Host_Name varchar(150),"
					+ "Status varchar(20),"
					+ "STIG varchar(300),"
					+ "Date_Found TIMESTAMP NOT NULL)");
			} catch (Exception e) {
				
			}
		try {
			db.createStatement().execute("CREATE TABLE dbo.Completed_Temp1 ("
					+ "CUST_ID INT,"
					+ "V_ID varchar(15),"
					+ "Host_Name varchar(150),"
					+ "Status varchar(20),"
					+ "STIG varchar(300),"
					+ "Date_Found TIMESTAMP NOT NULL)");
			} catch (Exception e) {
				
			}
		try {
			db.createStatement().execute("CREATE TABLE dbo.Ongoing_Temp ("
					+ "CUST_ID INT,"
					+ "V_ID varchar(15),"
					+ "Host_Name varchar(150),"
					+ "Status varchar(20),"
					+ "STIG varchar(300),"
					+ "Date_Found TIMESTAMP NOT NULL)");
			} catch (Exception e) {
				
			}
		try {
		db.createStatement().execute("CREATE TABLE dbo.Stig_Table ("
				+ "CUST_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "V_ID varchar(15),"
				+ "Severity varchar(20),"
				+ "Title varchar(500),"
				+ "Check_Text varchar(30000),"
				+ "Fix_Text varchar(30000),"
				+ "STIG varchar(300),"
				+ "PRIMARY KEY (V_ID, STIG))");
		} catch (Exception e) {
			
		}
		try {
			db.createStatement().execute("CREATE TABLE dbo.Ongoing ("
					+ "CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 2, INCREMENT BY 2),"
					+ "V_ID varchar(15),"
					+ "Host_Name varchar(150),"
					+ "Status varchar(20),"
					+ "STIG varchar(300),"
					+ "Date_Found TIMESTAMP NOT NULL)");
			} catch (Exception e) {
				
			}
		try {
			db.createStatement().execute("CREATE TABLE dbo.Main_Table ("
					+ "CUST_ID INT NOT NULL PRIMARY KEY,"
					+ "Group_Org varchar(255),"
					+ "Host_Name varchar(150),"
					+ "V_ID varchar(15),"
					+ "Severity varchar(20),"
					+ "Status varchar(20),"
					+ "Title varchar(500),"
					+ "Check_Text varchar(30000),"
					+ "Fix_Text varchar(30000),"
					+ "STIG varchar(300),"
					+ "Date_Found TIMESTAMP)");
			} catch (Exception e) {
				
			}
		try {
			db.createStatement().execute("CREATE TABLE dbo.ACAS ("
					+ "CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ "Group_Org varchar(255),"
					+ "Plugin varchar(15),"
					+ "Plugin_Name varchar(500),"
					+ "Family varchar(500),"
					+ "Severity varchar(10),"
					+ "IP_Address varchar(20),"
					+ "Protocol varchar(10),"
					+ "Port varchar(10),"
					+ "MAC_Address varchar(20),"
					+ "DNS_Name varchar(150),"
					+ "NetBIOS_Name varchar(150),"
					+ "Plugin_Text varchar(30000),"
					+ "First_Discovered varchar(50),"
					+ "Last_Observed varchar(50),"
					+ "Vuln_Publication_Date varchar(50),"
					+ "Patch_Publication_Date varchar(50),"
					+ "Plugin_Publication_Date varchar(50),"
					+ "Upload_Date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
			
			} catch (Exception e) {
				
			}
		
		try {
			db.createStatement().execute("CREATE TABLE dbo.CONFIG ("
					+ "XCCDF_Drop_Path varchar(255),"
					+ "CKL_Drop_Path varchar(255),"
					+ "Asset_Drop_Path varchar(255),"
					+ "ACAS_Drop_Path varchar(255),"
					+ "BI_Drop_Path varchar(255),"
					+ "STIG_Drop_Path varchar(255),"
					+ "startTime int,"
					+ "intervalTime int,"
					+ "DATABASE_BACKUP_DROP varchar(255),"
					+ "THREADS_ENABLED boolean)");
			db.createStatement().execute("insert into dbo.config (threads_enabled)  VALUES (true)");
			} catch (Exception e) {
			}
		try {
			db.createStatement().execute("CREATE TABLE dbo.Scap_Open_Vulns ("
					+ "Group_Org varchar(255),"
					+ "Severity varchar(20),"
					+ "Open_V_ID int)");
			
			} catch (Exception e) {
			}
		try {
			db.createStatement().execute("CREATE TABLE DBO.Stig_Table_Scap ("
					+ "Group_Org varchar(255),"
					+ "Severity varchar(15),"
					+ "Total_Open int)");
		} catch (Exception e) {
		}
		try {
			db.createStatement().execute("CREATE TABLE DBO.SCAP_Metrics_Score ("
					+ "Group_Org varchar(255),"
					+ "Open_V_ID int,"
					+ "Severity varchar(15),"
					+ "Total_Possible int)");
		} catch (Exception e) {
			
		}
		
	}
}
