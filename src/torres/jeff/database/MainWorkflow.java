package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWorkflow {
	
	public static void startWorkflow(Connection db) throws SecurityException, IOException {
		Logger errorLog = new ErrorLogging().logger(XccdfReader.class.getName(), "XccdfReader.log", Level.WARNING);
		try {
			// Deletes from the stage table where host_name does not exist for unknown reasons
			db.createStatement().execute("DELETE FROM DBO.STAGE_XC where host_name = '' OR HOST_NAME iS NULL");
			
			db.createStatement().execute("INSERT INTO dbo.METRICS (Host_Name) SELECT DISTINCT Host_Name FROM dbo.STAGE_XC");
        	// simple insert with join on Stig_Table to save time in the future
			db.createStatement().execute("INSERT INTO DBO.ONGOING (V_ID, Host_Name, Status, STIG, Date_Found) "
					+ "Select DISTINCT dbo.STAGE_XC.V_ID, dbo.STAGE_XC.Host_Name, dbo.STAGE_XC.Status, DBO.Stig_Table.STIG, dbo.STAGE_XC.Date_Found from dbo.STAGE_XC " + 
					"JOIN dbo.Stig_Table ON dbo.STAGE_XC.V_ID = dbo.Stig_Table.V_ID " + 
					"WHERE dbo.STAGE_XC.Status = 'fail' AND dbo.STAGE_XC.STIG Like dbo.Stig_Table.STIG");
        	// simple insert with join on Stig_Table to save time in the future
			db.createStatement().execute(" INSERT INTO DBO.COMPLETED_TEMP (V_ID, Host_Name, Status, STIG, Date_Found) "
					+ "Select DISTINCT dbo.STAGE_XC.V_ID, dbo.STAGE_XC.Host_Name, dbo.STAGE_XC.Status, DBO.Stig_Table.STIG, dbo.STAGE_XC.Date_Found from dbo.STAGE_XC " + 
					"JOIN dbo.Stig_Table ON dbo.STAGE_XC.V_ID = dbo.Stig_Table.V_ID " + 
					"WHERE dbo.STAGE_XC.Status = 'pass' AND dbo.STAGE_XC.STIG Like dbo.Stig_Table.STIG");	
			// No need for info on this table after the above queries run. It makes room for the next batch
			db.createStatement().execute("DELETE FROM dbo.Stage_xc");	
			
			// Moves the oldest (earliest date) records into a temp table. Save time in teh future
			db.createStatement().execute("INSERT INTO DBO.COMPLETED_TEMP1 (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
					"SELECT DISTINCT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM (SELECT Host_Name, V_ID, STATUS, STIG, MIN(DATE_FOUND) as "
					+ "DATE_FOUND FROM DBO.COMPLETED_Temp GROUP BY HOST_NAME, V_ID, Status, STIG) as f");
			
			// Only transfers records where a v_id and hostname and stig overrides a finding, or if it as part of an already completed open vulnerability
			
			db.createStatement().execute("INSERT INTO DBO.COMPLETED (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
					"SELECT DISTINCT DBO.COMPLETED_TEMP1.HOST_NAME, DBO.COMPLETED_TEMP1.V_ID, DBO.COMPLETED_TEMP1.STATUS, DBO.COMPLETED_TEMP1.STIG, DBO.COMPLETED_TEMP1.DATE_FOUND FROM DBO.COMPLETED_TEMP1 "
					+ "JOIN DBO.ONGOING ON DBO.ONGOING.Host_Name = DBO.COMPLETED_TEMP1.Host_Name AND DBO.ONGOING.V_ID = DBO.COMPLETED_TEMP1.V_ID "
					+ "AND DBO.ONGOING.Stig = DBO.COMPLETED_TEMP1.Stig "
					+ "WHERE DBO.ONGOING.Stig IS NOT NULL AND DBO.ONGOING.V_ID IS NOT NULL");
			
			db.createStatement().execute("DELETE FROM dbo.Completed_Temp");
			db.createStatement().execute("DELETE FROM dbo.Completed_Temp1");
			
			db.createStatement().execute("INSERT INTO DBO.ONGOING_TEMP (Host_Name, V_ID, STATUS, STIG, DATE_FOUND) " + 
					"SELECT DISTINCT HOST_NAME, V_ID, STATUS, STIG, DATE_FOUND FROM (SELECT Host_Name, V_ID, STATUS, STIG, MIN(DATE_FOUND) as "
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
        
        	db.createStatement().execute("DELETE FROM DBO.MAIN_TABLE");
        	
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
