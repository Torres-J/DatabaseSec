package torres.jeff.stigChecker;

import java.sql.SQLException;

public class CreateDb extends Connections{


	public static void createTheDB() throws ClassNotFoundException, SQLException {		
		try {
			ConnectDB().createStatement().execute("create table ASSET_LIST_STAGE (HostName varchar(255), Type varchar(255))");
			} catch (Exception e){
			}
		try {
		ConnectDB().createStatement().execute("create table ASSET_LIST (HostName varchar(255), LastScanDate DATE, ERROR_REASON varchar(255), PINGABLE varchar(3), Latest_Version varchar(3))");
			} catch (Exception e){
			}
		try {
			ConnectDB().createStatement().execute("create table CONFIG (THREADS INT, DAYS_SINCE_SCAN INT)");
			ConnectDB().createStatement().execute("INSERT INTO CONFIG (THREADS, DAYS_SINCE_SCAN) VALUES (20, 14)");
			} catch (Exception e){
			}
		try {
			ConnectDB().createStatement().execute("CREATE TABLE dbo.CONFIG ("
					+ "Export_Path varchar(255),"
					+ "Threads int,"
					+ "DAYS_SINCE_SCAN int,"
					+ "Stig_Checker_Enabled boolean,"
					+ "Stig_Checker_Hostnames varchar(255),"
					+ "Stig_Checker_Finished varchar(3))");
		} catch (Exception e) {
		}
		try {
			ConnectDB().createStatement().execute("INSERT INTO dbo.CONFIG (THREADS, DAYS_SINCE_SCAN, Stig_Checker_Enabled) VALUES (20, 14, false)");
		} catch (Exception e) {
		}
	}
}

