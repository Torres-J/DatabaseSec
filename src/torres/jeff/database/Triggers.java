package torres.jeff.database;

import java.sql.Connection;
import java.sql.SQLException;

public class Triggers {
	
	public static void setTriggers(Connection db) throws SQLException {
		try {
			db.createStatement().execute("CREATE TRIGGER stage_transformation "
					+ "AFTER INSERT ON dbo.STAGE_XC "
					+ "REFERENCING NEW AS NEWROW "
					+ "FOR EACH ROW "
					+ "INSERT INTO dbo.METRICS (Host_Name, Date_Found) "
					+ "VALUES (NEWROW.Host_Name, NEWROW.Date_Found)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.createStatement().execute("CREATE TRIGGER stage_to_ongoing "
					+ "AFTER INSERT ON dbo.STAGE_XC "
					+ "REFERENCING NEW AS NEWROW "
					+ "FOR EACH ROW "
					+ "INSERT INTO dbo.Ongoing (V_ID, Host_Name, Status, STIG, Date_Found) "
					+ "VALUES (NEWROW.V_ID, NEWROW.Host_Name, NEWROW.Status, NEWROW.STIG, NEWROW.Date_Found) "
					+ "WHERE NEWROW.Status = 'pass'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

/*try {
	db.createStatement().execute("CREATE TRIGGER stage_to_ongoing "
			+ "AFTER INSERT ON dbo.STAGE_XC "
			+ "REFERENCING NEW AS NEWROW "
			+ "FOR EACH ROW "
			+ "INSERT INTO dbo.Ongoing (V_ID, Host_Name, Status, STIG, Date_Found) "
			+ "VALUES (NEWROW.V_ID, NEWROW.Host_Name, NEWROW.Status, NEWROW.STIG, NEWROW.Date_Found) "
			+ "WHERE NEWROW.Status = 'pass'");
} catch (Exception e) {
	e.printStackTrace();
}*/