package torres.jeff.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketsServer {
	
	private ServerSocket ss;
	private Socket soc;
	private Socket socOutput;
	private int localPort;
	private String targetHost;
	private String hostnameRecieved;
	private Connection db;
	
	public SocketsServer(Connection dbo) {
		db = dbo;
	}
	
	public void startServer() {
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.execute(new Runnable() {
			public void run() {
				try { 	
					ss = new ServerSocket(localPort);
					while (true) {
						soc = ss.accept();
						while (true) {
							try {
								hostnameRecieved = soc.getInetAddress().getHostName();
								if (hostnameRecieved.contains("127.0.0.1")) {
									hostnameRecieved = "localhost";
								}
								BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
								String str = in.readLine();
								setTargetHost(hostnameRecieved);
								caseStatement(str);
								break;
						} catch (Exception e) {
							e.printStackTrace();
							break;
						}
							
						}
					}
				} catch (Exception e) {	
					e.printStackTrace();
				}
			}
		});
		
		service.execute(new Runnable() {
			public void run() {
				// Check in to let database know this connection is active
				while (true) {
					try {
						StigCheckManagerDialog.resetRunningPrograms();
						ArrayList<String> array = new ArrayList<String>();	
						ResultSet rs = db.createStatement().executeQuery("SELECT DISTINCT Stig_Checker_Hostnames FROM DBO.CONFIG WHERE Stig_Checker_Hostnames IS NOT NULL");
						while (rs.next()) {
							array.add(rs.getString("Stig_Checker_Hostnames"));
						}
						StigCheckManagerDialog.setMaxHostsRunning(array.size());
						for (String host : array) {
							if (host.equalsIgnoreCase(InetAddress.getLocalHost().getHostName())) {
								host = "127.0.0.1,0";
							}
							String[] input = host.split(",");
							socOutput = new Socket(input[0], 20128);
							PrintWriter out = new PrintWriter(socOutput.getOutputStream(), true);
							out.println("checkIn");
							socOutput.close();
						}	
				} catch (Exception e) {
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
				}
			}
				
			}
		});
	}
	
	// Switch statement for commands go here
		private void caseStatement(String str) throws UnknownHostException, IOException {
			String[] command = str.split(",");
			switch (command[0]) {
			case "checkingIn" : StigCheckManagerDialog.setTotalRunning(); break;
			}
			
		}
	
	public void sendData(String s) {
		try {
			socOutput = new Socket(targetHost, 20128);
			PrintWriter out = new PrintWriter(socOutput.getOutputStream(), true);
			out.println(s);
			socOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	
	public void setLocalPort(int port) {
		localPort = port;
	}
	
	public void setTargetHost(String host) {
		targetHost = host;
	}
	

	

}
