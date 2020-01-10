package torres.jeff.stigChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class TransferFiles {
	
	private String stigLocation = System.getProperty("user.dir") + "\\STIGChecks\\";
	private String win10Stig = stigLocation + "Win10-STIG-Audit\\";
	private String serverStig = stigLocation + "WinServ12-STIG-Audit\\";
	private static String stigDroptoDbPath = "\\\\ZNC121A0SCAP06\\E$\\Reports\\Vulnerability_Imports";
	
	public void beginFileTransferDownstream(String hostName, String type) throws InterruptedException, IOException {
		String line;
		String destinationFolderWorkstations = "\\\\" + hostName + "\\C$\\Software\\Win10-STIG-Audit\\";
		String destinationFolderServers = "\\\\" + hostName + "\\C$\\Software\\WinServ12-STIG-Audit\\";
		if (type.contains("WORKSTATION")) {
			Process process = Runtime.getRuntime().exec("XCOPY " + win10Stig + "* " + destinationFolderWorkstations + " /E /R /Y");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = reader.readLine()) != null) {
			}
		} else if (type.contains("SERVER")) {
			Process process = Runtime.getRuntime().exec("XCOPY " + serverStig + "* " + destinationFolderServers + " /E /R /Y");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = reader.readLine()) != null) {	
			}
		}
	}
	public static void beginFileTransferUpstream(String hostName, String type) throws IOException {
		String destinationFolderWorkstations = "\\\\" + hostName + "\\C$\\Software\\Win10-STIG-Audit\\Result";
		String destinationFolderServers = "\\\\" + hostName + "\\C$\\Software\\WinServ12-STIG-Audit\\Result";
		CopyOption[] options = new CopyOption[] {
				StandardCopyOption.COPY_ATTRIBUTES
			};
		if (type.contains("WORKSTATION")) {
			File desinationPath = new File(stigDroptoDbPath);			
			File[] n = new File(destinationFolderWorkstations).listFiles();
			for (File file : n) {
				String fileName = file.getName();
				String destination = desinationPath + "\\" + fileName;
				Path finalDestination = new File(destination).toPath();
				Path source = file.toPath();
				Files.copy(source, finalDestination, options);
				if (finalDestination.toFile().exists()) {
					file.delete();
				}
			}
				
		} else if (type.contains("SERVER")) {
			File desinationPath = new File(stigDroptoDbPath);			
			File[] n = new File(destinationFolderServers).listFiles();
			for (File file : n) {
				String fileName = file.getName();
				String destination = desinationPath + "\\" + fileName;
				Path finalDestination = new File(destination).toPath();
				Path source = file.toPath();
				Files.copy(source, finalDestination, options);
				if (finalDestination.toFile().exists()) {
					file.delete();
				}
			}
		}
	}
}
