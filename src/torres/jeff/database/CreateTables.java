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
		db.createStatement().execute("CREATE TABLE dbo.Ongoing ("
				+ "CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "V_ID varchar(15),"
				+ "Host_Name varchar(150),"
				+ "Status varchar(20),"
				+ "STIG varchar(300),"
				+ "Date_Found TIMESTAMP NOT NULL)");
		} catch (Exception e) {
			
		}
		try {
		db.createStatement().execute("CREATE TABLE dbo.Completed ("
				+ "CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "V_ID varchar(15),"
				+ "Host_Name varchar(150),"
				+ "Status varchar(20),"
				+ "STIG varchar(300),"
				+ "Date_Found TIMESTAMP NOT NULL)");
		} catch (Exception e) {
			
		}
		try {
		db.createStatement().execute("CREATE TABLE dbo.Stig_Table ("
				+ "CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
				+ "V_ID varchar(15),"
				+ "Severity varchar(20),"
				+ "Title varchar(500),"
				+ "Check_Text varchar(5000),"
				+ "Fix_Text varchar(5000),"
				+ "STIG varchar(300))");
		} catch (Exception e) {
			
		}
		try {
			db.createStatement().execute("CREATE TABLE dbo.Main_Table ("
					+ "CUST_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ "Group_Org varchar(255),"
					+ "Host_Name varchar(150),"
					+ "V_ID varchar(15),"
					+ "Severity varchar(20),"
					+ "Status varchar(20),"
					+ "Title varchar(500),"
					+ "Check_Text varchar(5000),"
					+ "Fix_Text varchar(5000),"
					+ "STIG varchar(300),"
					+ "Date_Found TIMESTAMP)");
			} catch (Exception e) {
				
			}
	}
}
