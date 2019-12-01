package torres.jeff.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class Triggers {
	
	public static void setTriggers(Connection db) throws SQLException, SecurityException, IOException {
		
		//Logger errorLog = new ErrorLogging().logger(Triggers.class.getName(), "Trigger.log", Level.WARNING);
		
		try {
			// After a new record is imported from xccdf or ckl, the records where a check failed are appended to the main ongoing table
			db.createStatement().execute("CREATE TRIGGER stage_to_ongoing "
					+ "AFTER INSERT ON dbo.STAGE_XC "
					+ "REFERENCING NEW AS NEWROW "
					+ "FOR EACH ROW "
					+ "INSERT INTO dbo.Ongoing (V_ID, Host_Name, Status, STIG, Date_Found) "
					+ "Select DISTINCT NEWROW.V_ID, NEWROW.Host_Name, NEWROW.Status, NEWROW.STIG, NEWROW.Date_Found from dbo.STAGE_XC "
					+ "WHERE NEWROW.Status = 'fail'");
		} catch (Exception e) {
			
		}
		try {
			// After
			db.createStatement().execute("CREATE TRIGGER ongoing_delete_latest "
					+ "AFTER INSERT ON dbo.Ongoing "
					+ "REFERENCING NEW AS NEWROW "
					+ "FOR EACH ROW "
					+ "DELETE FROM dbo.Ongoing "
					+ "WHERE DATE_FOUND NOT IN (SELECT MIN(DATE_FOUND) FROM DBO.ONGOING  " 
					+ "GROUP BY HOST_NAME, V_ID, STIG)");
		} catch (Exception e) {
			
		}
		try {
			// After a new record is imported from xccdf or ckl, the records where a check passed are appended to the main completed table
			db.createStatement().execute("CREATE TRIGGER stage_to_completed "
					+ "AFTER INSERT ON dbo.STAGE_XC "
					+ "REFERENCING NEW AS NEWROW "
					+ "FOR EACH ROW "
					+ "INSERT INTO dbo.Completed (V_ID, Host_Name, Status, STIG, Date_Found) "
					+ "Select DISTINCT NEWROW.V_ID, NEWROW.Host_Name, NEWROW.Status, NEWROW.STIG, NEWROW.Date_Found from dbo.STAGE_XC "
					+ "WHERE NEWROW.Status = 'pass'");
		} catch (Exception e) {
			
		}
		try {
			// After insert on completed table, the youngest files are purged
			db.createStatement().execute("CREATE TRIGGER completed_delete_earliest "
					+ "AFTER INSERT ON dbo.Completed "
					+ "REFERENCING NEW AS NEWROW "
					+ "FOR EACH ROW "
					+ "DELETE FROM dbo.Completed "
					+ "WHERE DATE_FOUND NOT IN (SELECT MAX(DATE_FOUND) FROM DBO.Completed " 
					+ "GROUP BY HOST_NAME, V_ID, STIG)");
		} catch (Exception e) {
			
		}
	}

}
