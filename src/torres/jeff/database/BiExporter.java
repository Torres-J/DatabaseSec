package torres.jeff.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class BiExporter {
	
	public void exportBiFiles(Connection db) throws SQLException, IOException, InterruptedException {
		String mainDirectory = System.getProperty("user.dir");
		String workSpace = mainDirectory + "/WorkSpace";
		String biDir = workSpace + "/BI_Files";
		File[] fileList = new File(biDir).listFiles();
		for (File f : fileList) {
			Process p = Runtime.getRuntime().exec("cmd.exe /c taskkill /f /FI \"windowtitle eq " + f.getName() + "*\"");
			Thread.sleep(5000);
			f.delete();
		}
		db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY('select DISTINCT * from dbo.Main_Table'," + 
				"'" + biDir + "/main_table.csv" + "', null, null, null)");
		db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY('select DISTINCT * from dbo.Metrics'," + 
				"'" + biDir + "/Metrics.csv" + "', null, null, null)");
		db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY('select DISTINCT * from dbo.Assets'," + 
				"'" + biDir + "/Assets.csv" + "', null, null, null)");
		
	}

}
