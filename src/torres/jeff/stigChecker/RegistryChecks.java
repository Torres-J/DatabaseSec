package torres.jeff.stigChecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;



public class RegistryChecks extends Connections {
	
	
	public static ArrayList<String> winCurrentVersion(String hostName) throws IOException, ClassNotFoundException, SQLException {
		Process process = Runtime.getRuntime().exec("reg query \"\\\\" + hostName + "\\HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion" + "\" /v " + "CurrentVersion");
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		ArrayList<String> statusCode = new ArrayList<String>();
		if (reader.read() == -1) {
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((line = reader1.readLine()) != null) {
				statusCode.add(line);
			}
		} else {
		
		while ((line = reader.readLine()) != null) {
			statusCode.add(line);
		}
	}
		return statusCode;
	}
}