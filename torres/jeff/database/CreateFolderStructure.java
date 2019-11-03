package torres.jeff.database;

import java.io.File;

public class CreateFolderStructure {
	
	public static void createFolders() {
		String mainDirectory = System.getProperty("user.dir");
		String workSpace = mainDirectory + "/WorkSpace";
		
		File xccdfDrop = new File(workSpace + "/xccdfDrop");
		if (!xccdfDrop.exists()) {
			xccdfDrop.mkdirs();
		}
		String settings = mainDirectory + "/settings";
		File settingsDirectory = new File(settings);
		if (!settingsDirectory.exists()) {
			settingsDirectory.mkdir();
		}
}
}
