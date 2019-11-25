package torres.jeff.database;

import java.io.File;

public class CreateFolderStructure {
	
	public static File workspacePathXccdfDrop;
	
	public static void createFolders() {
		String mainDirectory = System.getProperty("user.dir");
		String workSpace = mainDirectory + "/WorkSpace";
		
		String cklDrop = mainDirectory + "/cklDrop_Drop";
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
		String stigDrop = mainDirectory + "/STIG_Drop";
		File stigDirectory = new File(stigDrop);
		if (!stigDirectory.exists()) {
			stigDirectory.mkdir();
		}
}
}
