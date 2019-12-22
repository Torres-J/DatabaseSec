package torres.jeff.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Asset_Importer {
	
	public static void importAssets(Connection db) throws IOException, SQLException {
		String mainDirectory = System.getProperty("user.dir");
		String workSpace = mainDirectory + "/WorkSpace";
		String AssetDir = workSpace + "/Asset_Drop";
		File AssetDirDrop = new File(AssetDir);
		BufferedReader csvReader = null;
    	PreparedStatement pS = db.prepareStatement("INSERT INTO dbo.Assets (OU, Host_Name) VALUES (?,?)");
		for (File file : AssetDirDrop.listFiles()) {
			try {
				csvReader = new BufferedReader(new FileReader(file));	
				String row;
				db.createStatement().execute("DELETE FROM DBO.ASSETS");
				while ((row = csvReader.readLine()) != null) {
					String[] rows = row.split(",");
					String group = rows[0].toUpperCase();
					String hostName = rows[1].toUpperCase();
					pS.setString(1, group);
					pS.setString(2, hostName);
					pS.execute();
				}
				csvReader.close();
				file.delete();
		} catch (Exception e) {
			csvReader.close();
			e.printStackTrace();
						
		}
	}
		db.createStatement().execute("UPDATE DBO.MAIN_TABLE " +
				"SET DBO.MAIN_TABLE.Group_Org = (SELECT DISTINCT DBO.ASSETS.OU FROM DBO.ASSETS WHERE DBO.Assets.Host_name = DBO.MAIN_TABLE.Host_Name)"); 
		db.createStatement().execute("UPDATE DBO.METRICS " +
				"SET DBO.METRICS.OU = (SELECT DISTINCT DBO.ASSETS.OU FROM DBO.ASSETS WHERE DBO.Assets.Host_name LIKE DBO.METRICS.Host_Name)");
	}
}
