package torres.jeff.stigChecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketsClient {
	
	private ServerSocket ss;
	private Socket soc;
	private Socket socOutput;
	private int localPort;
	private String targetHost;
	private String hostnameRecieved;
	private Connection db;
	
	public SocketsClient(Connection dbo) {
		db = dbo;
	}
	
	public void startServer() {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(new Runnable() {
			public void run() {
				try { 	
					ss = new ServerSocket(localPort);
					while (true) {
						soc = ss.accept();
						while (true) {
							try {
								hostnameRecieved = soc.getInetAddress().getHostName();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (hostnameRecieved.contains("127.0.0.1")) {
								hostnameRecieved = "localhost";
							}
							BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
							String str = in.readLine();
							setTargetHost(hostnameRecieved);
							caseStatement(str);
							break;
							
						}
					}
				}
				catch (Exception e) {	
					e.printStackTrace();
				}
			}
		});
		
	}
	
	
	
	public void sendData(String s) {
		try {
			socOutput = new Socket(targetHost, 20127);
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

	// Switch statement for commands go here
	private void caseStatement(String str) throws UnknownHostException, IOException {
		String[] command = str.split(",");
		switch (command[0]) {
		case "checkIn" : sendData("checkingIn,0"); break;
		}
		
	}

}
