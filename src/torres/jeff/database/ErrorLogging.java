package torres.jeff.database;
import java.io.IOException;
import
java.util.logging.*;

public class ErrorLogging {
	
	public Logger logger(String className, String fileName, Level level) throws SecurityException, IOException {
		String mainDirectory = System.getProperty("user.dir");
		String logDir = mainDirectory + "/logs/";
		Logger loggerObj = Logger.getLogger( className );
		FileHandler fh= new FileHandler(logDir + fileName);
		fh.setLevel(level);
		fh.setFormatter(new SimpleFormatter());
		loggerObj.addHandler(fh);
		return loggerObj;
	}

}
