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
	private int localHost;

	
	public void startServer() {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(new Runnable() {
			public void run() {
				try { 
					ss = new ServerSocket(localHost);
					while (true) {
						soc = ss.accept();
						while (true) {
							BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
							String str = in.readLine();
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
		socOutput = new Socket("localhost", 443);
		PrintWriter out = new PrintWriter(socOutput.getOutputStream(), true);
		out.println(s);
		socOutput.close();
	}
	
	// Switch statement for commands go here
	private void caseStatement(String str) {
		switch (str) {
			case "Command1": doSomething();	break;
		}
	}
	private void setLocalHost(int host) {
		localHost = host;
	}
	
	
	private void doSomething() {
		System.out.println("command1 initiatiated");
	}

}
