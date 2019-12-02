package torres.jeff.database;

public class ThreadController {
	
	public static boolean threadsActive = true;
	
	public void startThreads() {
		threadsActive = true;
	}
	
	public void stopThreads() {
		threadsActive = false;
	}

}
