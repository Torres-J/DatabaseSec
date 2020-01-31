package torres.jeff.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Asset_Importer {
	
	public static void importAssets(Connection db) throws IOException, SQLException {
		BufferedReader csvReader = null;
    	PreparedStatement pS = db.prepareStatement("INSERT INTO dbo.Assets (OU, Host_Name) VALUES (?,?)");
		for (File file : CreateFolderStructure.workspacePathAssetDrop.listFiles()) {
			try {
				//db.createStatement().execute("DELETE FROM DBO.ASSETS");
				//db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_IMPORT_DATA_BULK('DBO', 'ASSETS','OU, HOST_NAME'," + "'1,2','" + file.toString() + "', null, null, null, 0, 1)");
				csvReader = new BufferedReader(new FileReader(file));	
				String row;
				while ((row = csvReader.readLine()) != null) {
					String[] rows = row.split(",");
					String group = rows[0].toUpperCase();
					String hostName = rows[1].toUpperCase();
					pS.setString(1, group);
					pS.setString(2, hostName);
					pS.addBatch();
				}
				db.createStatement().execute("DELETE FROM DBO.ASSETS");
				pS.executeBatch();
				csvReader.close();
				System.out.println("ok");

				//file.delete();
				 
		} catch (Exception e) {
			//csvReader.close();
			e.printStackTrace();
			
						
		}
	}
		//db.createStatement().execute("UPDATE DBO.MAIN_TABLE " +
			//	"SET DBO.MAIN_TABLE.Group_Org = (SELECT DISTINCT DBO.ASSETS.OU FROM DBO.ASSETS WHERE DBO.Assets.Host_name = DBO.MAIN_TABLE.Host_Name)");
		//db.createStatement().execute("UPDATE DBO.METRICS " +
			//	"SET DBO.METRICS.OU = (SELECT DISTINCT DBO.ASSETS.OU FROM DBO.ASSETS WHERE DBO.Assets.Host_name = DBO.METRICS.Host_Name)");
	}
	
}
