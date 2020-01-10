package torres.jeff.stigChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class ProgramDeployer {
		
	private static int threads;
	private static int daysToSubtract;
	private static ExecutorService executor;
	private static String psExecLocation = System.getProperty("user.dir") + "\\STIGChecks\\PsExec.exe";
	
	public static void run(Connection db, boolean updateChecks) throws ClassNotFoundException, SQLException, IOException {
		
		TransferFiles transferFiles = new TransferFiles();
		
		ArrayList<String> uniHostList = new ArrayList<String>();
		executor = Executors.newFixedThreadPool(threads);
		
		LocalDate minusDays = LocalDate.now().minusDays(daysToSubtract);
		String newDate = minusDays.toString();
		
		// Returns unique hostnames that have not been scanned within a period of time 'newDate' variable above
		ResultSet uniqueHosts = Connections.resultSet("SELECT DISTINCT HostName, Type, LastScanDate from Asset_List WHERE LastScanDate < '" + newDate + "' OR LastScanDate is NULL ORDER BY Type ASC, HostName DESC");
		
		// Adds hosts that have not been scanned to uniHostList for ping process
		while (uniqueHosts.next()) {
			String host = uniqueHosts.getString("HostName");
			String type = uniqueHosts.getString("Type");
			uniHostList.add(host +","+type);
		}
		
		for (int i = 0; i < uniHostList.size(); i++) {
			String[] hos = uniHostList.get(i).split(",");
			
			// Start of thread. Begins by pinging device in asset list
			System.out.println(hos[0] + " " + hos[1]);
			Future<?> pinger = executor.submit(()-> {
				try {
					String line;
					//String ip;
					Process process = Runtime.getRuntime().exec("ping " + hos[0]);
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					while ((line = reader.readLine()) != null) {
						if (line.contains("Pinging")) {		
							process.destroy();
							String ip;
							ip = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
							LocalDate currentDate = LocalDate.now();							
							ArrayList<String> eval = RegistryChecks.winCurrentVersion(hos[0]);
							if (eval.get(0).contains("ERROR:")) {
								
								db.createStatement().execute("UPDATE ASSET_LIST SET LastScanDate = '"+ currentDate +"', ERROR_REASON = 'Insufficient Permissions' WHERE HostName = '" + hos[0] + "'");
								 
								//db.createStatement().execute("UPDATE ASSET_LIST SET LastScanDate = NULL WHERE HostName = '" + hos[0] + "'");
								System.out.println(hos[0] + ": contained error");
								break;
							}
							int timer = 0;
							if (hos[1].contains("SERVER")) {
								System.out.println(hos[0] + " Started");
								String destinationFolder = "\\\\" + hos[0] + "\\C$\\Software\\WinServ12-STIG-Audit";
								File folder = new File(destinationFolder);
								if (!folder.exists() || updateChecks == true) {
									transferFiles.beginFileTransferDownstream(hos[0], hos[1]);
								}
								Process psExecController = Runtime.getRuntime().exec(psExecLocation + " \\\\" + hos[0] + " /accepteula -i -d -s -e Powershell -windowstyle hidden -command C:\\Software\\Win10-STIG-Audit\\Execute-Win10STIGAudit.ps1");
								File done = new File(destinationFolder + "\\Result\\done.txt");
								
								while (true) {
									if (done.exists()) {
										done.delete();
										TransferFiles.beginFileTransferUpstream(hos[0], hos[1]);
										//
										break;
									} else if (timer == 50) {
										break;
									}
									Thread.sleep(5000);
									timer++;
								}
								db.createStatement().execute("UPDATE ASSET_LIST SET LastScanDate = '"+ currentDate +"', PINGABLE = 'Yes' WHERE HostName = '" + hos[0] + "'");
								System.out.println(hos[0] + " Finished");
								break;

							} else if (hos[1].contains("WORKSTATION")) {
								System.out.println(hos[0] + " Started");
								String destinationFolder = "\\\\" + hos[0] + "\\C$\\Software\\Win10-STIG-Audit";
								File folder = new File(destinationFolder);
								if (!folder.exists() || updateChecks == true) {
									transferFiles.beginFileTransferDownstream(hos[0], hos[1]);
								}
								Process psExecController = Runtime.getRuntime().exec(psExecLocation + " \\\\" + hos[0] + " /accepteula -i -d -s -e Powershell -windowstyle hidden -command C:\\Software\\Win10-STIG-Audit\\Execute-Win10STIGAudit.ps1");
								File done = new File(destinationFolder + "\\Result\\done.txt");
								
								while (true) {
									if (done.exists()) {
										done.delete();
										TransferFiles.beginFileTransferUpstream(hos[0], hos[1]);
										//
										break;
									} else if (timer == 50) {
										break;
									}
									Thread.sleep(5000);
									timer++;
								}
								
								//db.createStatement().execute("UPDATE ASSET_LIST SET LastScanDate = '"+ currentDate +"', PINGABLE = 'Yes' WHERE HostName = '" + hos[0] + "'");
								System.out.println(hos[0] + " Finished");
								break;
							} else if (line.contains("could not find host")) {
								db.createStatement().execute("UPDATE ASSET_LIST SET LastScanDate = '"+ currentDate +"', PINGABLE = 'No' WHERE HostName = '" + hos[0] + "'");
								System.out.println(hos[0] + " Failed Ping");
								break;
							}
						
							
						}
						} reader.close();
				}
				catch (IOException | ClassNotFoundException | SQLException | InterruptedException e) {e.printStackTrace();}
				});
			
		} executor.shutdown();
		while (true) {
		if (executor.isTerminated()) {
			uniHostList.clear();
			System.out.println("Operation Completed");
			break;
		}
		}
		
	}
	
	public static void cancelExecutor() {
		executor.shutdownNow();
	}
	public static void changeThreads(int i) {
		threads = i;
	}
	public static void changeDaysToSubtract(int i) {
		daysToSubtract = i;
	}
}

