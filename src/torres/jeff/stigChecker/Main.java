package torres.jeff.stigChecker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
		
		Connection db = new Connections().ConnectDB();
		CreateDb.createTheDB();
		SocketsClient socketClient = new SocketsClient(db);
		socketClient.setLocalPort(20128);
		socketClient.startServer();

	}
}
