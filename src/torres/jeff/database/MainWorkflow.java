package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWorkflow {
	
	public static void startWorkflow(Connection db) throws SecurityException, IOException {
		Logger errorLog = new ErrorLogging().logger(XccdfReader.class.getName(), "XccdfReader.log", Level.WARNING);
		String ldt = LocalDateTime.now().toLocalDate().minus(90, ChronoUnit.DAYS).toString();
		try {
			// Deletes from the stage table where host_name does not exist for unknown reasons
			db.createStatement().execute("DELETE FROM DBO.STAGE_XC where host_name = '' OR HOST_NAME iS NULL");
			System.out.println("1");
			db.createStatement().execute("INSERT INTO dbo.METRICS_TEMP (OU, Host_Name) SELECT DISTINCT dbo.Assets.OU, dbo.STAGE_XC.Host_Name FROM dbo.STAGE_XC JOIN "
					+ "dbo.Assets ON dbo.Assets.Host_Name = dbo.STAGE_XC.Host_Name");
			db.createStatement().execute("INSERT INTO dbo.METRICS_TEMP (OU, Host_Name) SELECT DISTINCT dbo.Assets.OU, dbo.METRICS.Host_Name FROM dbo.METRICS JOIN "
					+ "dbo.Assets ON dbo.Assets.Host_Name = dbo.METRICS.Host_Name");
			db.createStatement().execute("DELETE FROM DBO.METRICS");
			db.createStatement().execute("INSERT INTO dbo.METRICS (OU, Host_Name) SELECT DISTINCT dbo.METRICS_TEMP.OU, dbo.METRICS_TEMP.Host_Name FROM dbo.METRICS_TEMP");
			db.createStatement().execute("DELETE FROM DBO.METRICS_TEMP");
			System.out.println("2");
			// Deletes records if a hostname has not been scanned within 90 days
			//db.createStatement().execute("DELETE from dbo.ongoing where host_name NOT IN (SELECT Host_Name from DBO.METRICS WHERE DATE_FOUND >= '" + ldt + "')");
			//db.createStatement().execute("DELETE from dbo.completed where host_name NOT IN (SELECT Host_Name from DBO.METRICS WHERE DATE_FOUND >= '" + ldt + "')");
			System.out.println("3");
        	// simple insert with join on Stig_Table to save time in the future
			db.createStatement().execute("INSERT INTO DBO.ONGOING (V_ID, Host_Name, Status, STIG, Date_Found) "
					+ "Select DISTINCT dbo.STAGE_XC.V_ID, dbo.STAGE_XC.Host_Name, dbo.STAGE_XC.Status, DBO.Stig_Table.STIG, dbo.STAGE_XC.Date_Found from dbo.STAGE_XC " + 
					"JOIN dbo.Stig_Table ON dbo.STAGE_XC.V_ID = dbo.Stig_Table.V_ID " + 
					"WHERE dbo.STAGE_XC.Status = 'fail' AND dbo.STAGE_XC.STIG Like dbo.Stig_Table.STIG");
			System.out.println("4");
        	// simple insert with join on Stig_Table to save time in the future
			db.createStatement().execute(" INSERT INTO DBO.COMPLETED_TEMP (V_ID, Host_Name, Status, STIG, Date_Found) "
					+ "Select DISTINCT dbo.STAGE_XC.V_ID, dbo.STAGE_XC.Host_Name, dbo.STAGE_XC.Status, DBO.Stig_Table.STIG, dbo.STAGE_XC.Date_Found from dbo.STAGE_XC " + 
					"JOIN dbo.Stig_Table ON dbo.STAGE_XC.V_ID = dbo.Stig_Table.V_ID " + 
					"WHERE dbo.STAGE_XC.Status = 'pass' AND dbo.STAGE_XC.STIG Like dbo.Stig_Table.STIG");
			System.out.println("5");
			// No need for info on this table after the above queries run. It makes room for the next batch
			db.createStatement().execute("DELETE FROM dbo.Stage_xc");	
			System.out.println("6");
			// Moves the oldest (earliest date) records into a temp table. Save time in the future
			db.createStatement().execute("INSERT INTO DBO.COMPLETED_TEMP1 (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
					"SELECT DISTINCT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM (SELECT Host_Name, V_ID, STATUS, STIG, MIN(DATE_FOUND) as "
					+ "DATE_FOUND FROM DBO.COMPLETED_Temp GROUP BY HOST_NAME, V_ID, Status, STIG) as f");
			System.out.println("7");
			// Only transfers records where a v_id and hostname and stig overrides a finding, or if it as part of an already completed vulnerability
			db.createStatement().execute("INSERT INTO DBO.COMPLETED (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
					"SELECT DISTINCT DBO.COMPLETED_TEMP1.HOST_NAME, DBO.COMPLETED_TEMP1.V_ID, DBO.COMPLETED_TEMP1.STATUS, DBO.COMPLETED_TEMP1.STIG, DBO.COMPLETED_TEMP1.DATE_FOUND FROM DBO.COMPLETED_TEMP1 "
					+ "JOIN DBO.ONGOING ON DBO.ONGOING.Host_Name = DBO.COMPLETED_TEMP1.Host_Name AND DBO.ONGOING.V_ID = DBO.COMPLETED_TEMP1.V_ID "
					+ "AND DBO.ONGOING.Stig = DBO.COMPLETED_TEMP1.Stig "
					+ "WHERE DBO.ONGOING.Stig IS NOT NULL AND DBO.ONGOING.V_ID IS NOT NULL");
			System.out.println("8");
			// The below two delete all data from temp tables of completed vulnerabilities
			db.createStatement().execute("DELETE FROM dbo.Completed_Temp");
			db.createStatement().execute("DELETE FROM dbo.Completed_Temp1");
			System.out.println("9");
			// Only transfers records where a v_id and hostname and stig overrides a finding, or if it as part of an already open vulnerability
			db.createStatement().execute("INSERT INTO DBO.ONGOING_TEMP (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
					"SELECT DISTINCT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM (SELECT Host_Name, V_ID, STATUS, STIG, MIN(DATE_FOUND) as "
					+ "DATE_FOUND FROM DBO.ONGOING GROUP BY HOST_NAME, V_ID, Status, STIG) as f");
			System.out.println("10");
			// Deletes all current vulnerabilities
			db.createStatement().execute("DELETE FROM dbo.Ongoing");
			System.out.println("11");
			// Inserts all vulnerabilities back into the open vulnerability table
			db.createStatement().execute("INSERT INTO DBO.Ongoing (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
					"SELECT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM DBO.ONGOING_TEMP");
			System.out.println("12");
			// Deletes from temp table
			db.createStatement().execute("DELETE FROM DBO.ONGOING_TEMP");
			System.out.println("13");
			// Deletes from completed table where an open vulnerability appears again
			db.createStatement().execute("DELETE FROM dbo.Completed " + 
        			"WHERE CUST_ID IN (" + 
        			"SELECT S.CUST_ID " + 
        			"FROM DBO.Completed AS S " + 
        			"JOIN DBO.Ongoing ON dbo.Ongoing.host_name = S.host_name AND dbo.Ongoing.V_ID = S.V_ID AND dbo.Ongoing.STIG = S.STIG " + 
        			"WHERE DBO.Ongoing.Date_Found > S.Date_Found)");
			System.out.println("14");
			// deletes from open findings table where an open finding has been remediated
        	db.createStatement().execute("DELETE FROM dbo.Ongoing " + 
        			"WHERE CUST_ID IN (" + 
        			"SELECT S.CUST_ID " + 
        			"FROM DBO.ONGOING AS S " + 
        			"JOIN DBO.Completed ON dbo.completed.host_name = S.host_name AND dbo.completed.V_ID = S.V_ID AND dbo.completed.STIG = S.STIG " + 
        			"WHERE DBO.Completed.Date_Found > S.Date_Found)");
        	System.out.println("15");
        	// deletes from main table
        	db.createStatement().execute("DELETE FROM DBO.MAIN_TABLE");
        	System.out.println("16");
        	// Inserts into main table open vulnerability data matched with STIG table and asset table
        	db.createStatement().execute("INSERT INTO dbo.Main_Table (CUST_ID, Group_Org, Host_Name, V_ID, Severity, Status, Title, Check_Text, Fix_Text, STIG, Date_Found) "
        			+ "SELECT DISTINCT dbo.Ongoing.CUST_ID, dbo.Assets.OU, dbo.Ongoing.Host_Name, dbo.Ongoing.V_ID, dbo.Stig_Table.Severity, dbo.Ongoing.Status, dbo.Stig_Table.Title, dbo.Stig_Table.Check_Text, dbo.Stig_Table.Fix_Text, dbo.Stig_Table.STIG, dbo.Ongoing.Date_Found "
        			+ "FROM dbo.Ongoing "
        			+ "LEFT JOIN dbo.Assets ON dbo.Assets.Host_Name = dbo.Ongoing.Host_Name "
        			+ "JOIN dbo.Stig_Table ON dbo.Ongoing.V_ID = dbo.Stig_Table.V_ID "
        			+ "LEFT JOIN dbo.Main_Table ON dbo.Main_Table.CUST_ID = dbo.Ongoing.CUST_ID "
        			+ "WHERE dbo.Ongoing.Stig = dbo.Stig_Table.Stig AND dbo.Main_Table.CUST_ID IS NULL");
        	System.out.println("17");
        	// Inserts into main table completed vulnerability data matched with STIG table and asset table
        	db.createStatement().execute("INSERT INTO dbo.Main_Table (CUST_ID, Group_Org, Host_Name, V_ID, Severity, Status, Title, Check_Text, Fix_Text, STIG, Date_Found) "
        			+ "SELECT DISTINCT dbo.Completed.CUST_ID, dbo.Assets.OU, dbo.Completed.Host_Name, dbo.Completed.V_ID, dbo.Stig_Table.Severity, dbo.Completed.Status, dbo.Stig_Table.Title, dbo.Stig_Table.Check_Text, dbo.Stig_Table.Fix_Text, dbo.Stig_Table.STIG, dbo.Completed.Date_Found "
        			+ "FROM dbo.Completed "
        			+ "LEFT JOIN dbo.Assets ON dbo.Assets.Host_Name = dbo.Completed.Host_Name "
        			+ "JOIN dbo.Stig_Table ON dbo.Completed.V_ID = dbo.Stig_Table.V_ID "
        			+ "LEFT JOIN dbo.Main_Table ON dbo.Main_Table.CUST_ID = dbo.Completed.CUST_ID "
        			+ "WHERE dbo.Completed.Stig = dbo.Stig_Table.Stig AND dbo.Main_Table.CUST_ID IS NULL");
        	System.out.println("18");
        	// The below is used to create the scores for SCAP CCRI
        	db.createStatement().execute("UPDATE DBO.Main_Table SET GROUP_ORG = '' WHERE GROUP_ORG IS NULL");
        	db.createStatement().execute("DELETE FROM DBO.Scap_Open_Vulns");
        	db.createStatement().execute("DELETE FROM DBO.Stig_Table_Scap");
        	db.createStatement().execute("DELETE FROM DBO.SCAP_Metrics_Score");
        	db.createStatement().execute("INSERT INTO DBO.Stig_Table_Scap (Group_Org, Severity, Total_Open) "
        			+ "SELECT DBO.Main_Table.Group_Org, DBO.STIG_Table.Severity, count(distinct DBO.STIG_Table.V_ID) FROM DBO.STIG_Table "
        			+ "JOIN DBO.Main_Table ON DBO.Main_Table.STIG = DBO.STIG_Table.STIG "
        			+ "WHERE DBO.Main_Table.Status = 'fail' "
        			+ "GROUP BY DBO.Main_Table.Group_Org, DBO.Stig_Table.Severity");
        	System.out.println("19");
        	db.createStatement().execute("INSERT INTO DBO.Scap_Open_Vulns (Group_Org, Severity, Open_V_ID) "
        			+ "SELECT DBO.Main_Table.Group_Org, DBO.Main_Table.Severity, count(distinct DBO.Main_Table.V_ID) FROM DBO.Main_Table "
        			+ "WHERE DBO.Main_Table.Status = 'fail' "
        			+ "GROUP BY DBO.Main_Table.Group_Org, DBO.Main_Table.Severity");
        	System.out.println("20");
        	db.createStatement().execute("INSERT INTO DBO.SCAP_Metrics_Score (Group_Org, Open_V_ID, Severity, Total_Possible) "
        			+ "SELECT DBO.Stig_Table_Scap.Group_Org, NULLIF(DBO.SCAP_OPEN_VULNS.Open_V_ID, 0) AS Total_Open, DBO.Stig_Table_Scap.Severity, NULLIF(DBO.STIG_Table_Scap.Total_Open, 0) AS Total_Possible " + 
        			"FROM DBO.Stig_Table_Scap " + 
        			"LEFT JOIN DBO.Scap_Open_Vulns ON DBO.Scap_Open_Vulns.Group_Org = DBO.Stig_Table_Scap.Group_Org AND DBO.Stig_Table_Scap.Severity = DBO.Scap_Open_Vulns.Severity");
        	System.out.println("21");
        	db.createStatement().execute("UPDATE DBO.SCAP_Metrics_Score SET OPEN_V_ID = 0 WHERE OPEN_V_ID IS NULL");
        	System.out.println("22");
		}
        catch (Exception e) {
        	//errorLog.log(Level.SEVERE, "XccdfReader Database Appending Error", e);
        	e.printStackTrace();
        }
	}
}
