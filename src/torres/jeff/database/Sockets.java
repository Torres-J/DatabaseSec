package torres.jeff.database;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Sockets {
	
	private ServerSocket ss;
	private Socket soc;
	private Socket socOutput;
	private int localPort;
	private String targetHost;
	
	public void startServer() {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(new Runnable() {
			public void run() {
				try { 
					ss = new ServerSocket(localPort);
					while (true) {
						soc = ss.accept();
						while (true) {
							String hostnameRecieved = soc.getInetAddress().getHostName();
							BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
							String str = in.readLine();
							caseStatement("setTargetHost," + hostnameRecieved);
							caseStatement(str);
							break;
						}
					}
				}
				catch (Exception e) {	
				}
			}
		});
	}
	
	public void sendData(String s) throws UnknownHostException, IOException {
		socOutput = new Socket(targetHost, 20122);
		PrintWriter out = new PrintWriter(socOutput.getOutputStream(), true);
		out.println(s);
		socOutput.close();
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
		if (command[0].startsWith("setTargetHost")) {
			setTargetHost(command[1]);
		} else if (command[0].startsWith("test")) {
			sendData("Huray");
		}
	}

}
