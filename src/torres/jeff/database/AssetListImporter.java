package torres.jeff.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AssetListImporter {
	
	public static void importAssets(Connection db, File file) throws SQLException, IOException {
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader(file));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    db.createStatement().execute("INSERT INTO dbo.Assets VALUES ('" + data[0] + "'," + data[1] + ")");
		}
	}

}
