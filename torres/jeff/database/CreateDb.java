package torres.jeff.database;

import java.sql.SQLException;

public class CreateDb extends Connections{


	public static void createTheDB() throws ClassNotFoundException, SQLException {		
		try {
		ConnectDB().createStatement().execute("create table VULN_TABLE (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), OU varchar(255), HostName varchar(255), V_ID varchar(20), rule varchar(500), severity varchar(20), Status varchar(50), Stig varchar(250), Result_Value varchar(5000))");
		} catch (Exception e){
		}
		try {
		ConnectDB().createStatement().execute("create table ASSET_LIST (HostName varchar(255), OU varchar(255))");
			} catch (Exception e){
			}
		try {
		ConnectDB().createStatement().execute("create table VULNS_FOR_EXPORT (V_ID varchar(20), HostName varchar(255), Status varchar(20), Stig varchar(250))");
		} catch (Exception e) {
		}
		try {
		ConnectDB().createStatement().execute("create table VID_LOOKUP (V_ID varchar(20), rule varchar(500), severity varchar(20), Stig varchar(255), reg varchar(250), keyText varchar(1000), check_value INT)");
		} catch (Exception e) {
		}
		try {
		ConnectDB().createStatement().execute("create table VULN_SELECTION (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), HostName varchar(255), V_ID varchar(20))");
		} catch (Exception e) {
		}
	}
}