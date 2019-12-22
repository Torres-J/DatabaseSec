package torres.jeff.database;

import java.io.File;
import java.io.IOException;


public class CreateFolderStructure {
	
	//private static final Logger LOGGER = Logger.getLogger( CreateFolderStructure.class.getName() );
	
	public static File workspacePathXccdfDrop;
	public static File workspacePathCKLDrop;
	public static File workspacePathACASDrop;
	public static File workspacePathBIExportLocation;
	public static void createFolders() throws SecurityException, IOException {
		
		// Directory of where the program executes is created under a folder called workspace. Each files purpose is further divided into their own folder
		String mainDirectory = System.getProperty("user.dir");
		String workSpace = mainDirectory + "/WorkSpace";
		
		// Location of where CKL's are dropped for import is created. SCAP results are dropped in a different folder.
		String cklDrop = workSpace + "/ckl_Drop_Drop";
		File cklDirectory = new File(cklDrop);
		if (!cklDirectory.exists()) {
			cklDirectory.mkdir();
		}
		// The ckl directory that can be called by the CKL_Parser class
		workspacePathCKLDrop = cklDirectory;
		
		File xccdfDrop = new File(workSpace + "/xccdfDrop");
		if (!xccdfDrop.exists()) {
			xccdfDrop.mkdirs();
		}
		// The xccdf drop location that can be called by the XccdfReader class
		workspacePathXccdfDrop = xccdfDrop;
		
		// The ACAS drop location that can be called by the ACAS class
		File ACASDrop = new File(workSpace + "/ACAS_Drop");
		if (!ACASDrop.exists()) {
			ACASDrop.mkdirs();
		}
		// The xccdf drop location that can be called by the XccdfReader class
		workspacePathACASDrop = ACASDrop;
		
		String settings = mainDirectory + "/settings";
		File settingsDirectory = new File(settings);
		if (!settingsDirectory.exists()) {
			settingsDirectory.mkdir();
		}
		// temp location, it may not be needed depending on 
		String stigDrop = workSpace + "/STIG_Drop";
		File stigDirectory = new File(stigDrop);
		if (!stigDirectory.exists()) {
			stigDirectory.mkdir();
		}
		// log directory for testing. I'm not sure whether or not logs are necessary
		String logDir = mainDirectory + "/logs";
		File logDirFiles = new File(logDir);
		if (!logDirFiles.exists()) {
			logDirFiles.mkdir();
		}
		// The location where all .CSV files for BI are dropped
		String biDir = workSpace + "/BI_Files";
		File biDirFile = new File(biDir);
		if (!biDirFile.exists()) {
			biDirFile.mkdir();
		}
		workspacePathBIExportLocation = biDirFile;
		// The location where Asset Lists can be dropped
		String AssetDir = workSpace + "/Asset_Drop";
		File AssetDirDrop = new File(AssetDir);
		if (!AssetDirDrop.exists()) {
			AssetDirDrop.mkdir();
		}

}
}
