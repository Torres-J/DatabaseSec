package torres.jeff.database;

import java.io.File;
import java.io.IOException;


public class CreateFolderStructure {
	
	//private static final Logger LOGGER = Logger.getLogger( CreateFolderStructure.class.getName() );

	public static File workspacePathXccdfDrop;
	
	public static void createFolders() throws SecurityException, IOException {
		
		
		
		String mainDirectory = System.getProperty("user.dir");
		String workSpace = mainDirectory + "/WorkSpace";
		
		String cklDrop = workSpace + "/ckl_Drop_Drop";
		File cklDirectory = new File(cklDrop);
		if (!cklDirectory.exists()) {
			cklDirectory.mkdir();
		}
		
		File xccdfDrop = new File(workSpace + "/xccdfDrop");
		if (!xccdfDrop.exists()) {
			xccdfDrop.mkdirs();
		}
		workspacePathXccdfDrop = xccdfDrop;
		
		String settings = mainDirectory + "/settings";
		File settingsDirectory = new File(settings);
		if (!settingsDirectory.exists()) {
			settingsDirectory.mkdir();
		}
		String stigDrop = workSpace + "/STIG_Drop";
		File stigDirectory = new File(stigDrop);
		if (!stigDirectory.exists()) {
			stigDirectory.mkdir();
		}
		String logDir = mainDirectory + "/logs";
		File logDirFiles = new File(logDir);
		if (!logDirFiles.exists()) {
			logDirFiles.mkdir();
		}
		String biDir = workSpace + "/BI_Files";
		File biDirFile = new File(biDir);
		if (!biDirFile.exists()) {
			biDirFile.mkdir();
		}
		String AssetDir = workSpace + "/Asset_Drop";
		File AssetDirDrop = new File(AssetDir);
		if (!AssetDirDrop.exists()) {
			AssetDirDrop.mkdir();
		}

}
}
