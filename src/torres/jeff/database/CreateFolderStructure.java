package torres.jeff.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class CreateFolderStructure {
	
	//private static final Logger LOGGER = Logger.getLogger( CreateFolderStructure.class.getName() );
	
	public static File workspacePathXccdfDrop;
	public static File workspacePathCKLDrop;
	public static File workspacePathACASDrop;
	public static File workspacePathBIExportLocation;
	public static File workspacePathSTIGDrop;
	public static File workspacePathAssetDrop;
	public static File backupDirectoryLocation;
	public static File workspacePathAutoSTIGResults;

	public static void createFolders(Connection db) throws SecurityException, IOException, SQLException {
		ResultSet rS = db.createStatement().executeQuery("SELECT * FROM DBO.CONFIG");
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
		
		String autoSTIGD = workSpace + "/Auto_STIG_Drop";
		File autoSTIGDirectory = new File(autoSTIGD);
		if (!autoSTIGDirectory.exists()) {
			autoSTIGDirectory.mkdir();
		}
		// The ckl directory that can be called by the CKL_Parser class
		workspacePathAutoSTIGResults = autoSTIGDirectory;
		
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
		
		// temp location, it may not be needed depending on 
		String stigDrop = workSpace + "/STIG_Drop";
		File stigDirectory = new File(stigDrop);
		if (!stigDirectory.exists()) {
			stigDirectory.mkdir();
		}
		workspacePathSTIGDrop = stigDirectory;
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
		workspacePathAssetDrop = AssetDirDrop;			
		// The location where Asset Lists can be dropped
		String backupDir = workSpace + "/Database_Backup";
		File backupDirectoryLoc = new File(backupDir);
		if (!backupDirectoryLoc.exists()) {
			backupDirectoryLoc.mkdir();
		}
		backupDirectoryLocation = backupDirectoryLoc;	
				
		while (rS.next()) {
			// Location of where CKL's are dropped for import is created. SCAP results are dropped in a different folder.
			String cklDropNew = rS.getString("CKL_Drop_Path");
			if (!rS.wasNull()) {
				//db.createStatement().execute("INSERT INTO DBO.METRICS (XCCDF_Drop_Path) VALUES ('" + workspacePathXccdfDrop.toString() + "')");
				File newCklDrop = new File(cklDropNew);
				if (newCklDrop.exists() & newCklDrop.canWrite()) {
					workspacePathCKLDrop = newCklDrop;
				} else {
					workspacePathCKLDrop = cklDirectory;
					JOptionPane.showMessageDialog(null, "CKL Drop Folder: " + newCklDrop.toString() + "\n"
							+ "Either No Longer Exists Or Your Profile Does Not Have Permission To Read/Write To This Location\n"
							+ "Default Location In WorkSpace Will Be Used" );
				}
			} 

			// Location where backups are stored
			String backupDirectoryNew = rS.getString("DATABASE_BACKUP_DROP");
			if (!rS.wasNull()) {
				//db.createStatement().execute("INSERT INTO DBO.METRICS (XCCDF_Drop_Path) VALUES ('" + workspacePathXccdfDrop.toString() + "')");
				File newBackupDrop = new File(backupDirectoryNew);
				if (newBackupDrop.exists() & newBackupDrop.canWrite()) {
					backupDirectoryLocation = newBackupDrop;
				} else {
					backupDirectoryLocation = backupDirectoryLoc;
					JOptionPane.showMessageDialog(null, "Database Backup Folder: " + newBackupDrop.toString() + "\n"
							+ "Either No Longer Exists Or Your Profile Does Not Have Permission To Read/Write To This Location\n"
							+ "Default Location In WorkSpace Will Be Used" );
				}
			} 
			
			// The xccdf drop location that can be called by the XccdfReader class. If not specified in the GUI, the default local directory is used.
			String XCCDF_Drop_PathNew = rS.getString("XCCDF_Drop_Path");
			if (!rS.wasNull()) {
				//db.createStatement().execute("INSERT INTO DBO.METRICS (XCCDF_Drop_Path) VALUES ('" + workspacePathXccdfDrop.toString() + "')");
				File xccdfDropNew = new File(XCCDF_Drop_PathNew);
				if (xccdfDropNew.exists() & xccdfDropNew.canWrite()) {
					workspacePathXccdfDrop = xccdfDropNew;
				} else {
					workspacePathXccdfDrop = xccdfDrop;
					JOptionPane.showMessageDialog(null, "XCCDF Drop Folder: " + xccdfDrop.toString() + "\n"
							+ "Either No Longer Exists Or Your Profile Does Not Have Permission To Read/Write To This Location\n"
							+ "Default Location In WorkSpace Will Be Used" );
				}
			}
			// The ACAS drop location that can be called by the ACAS class
			String ACAS_Drop_PathNew = rS.getString("ACAS_Drop_Path");
			if (!rS.wasNull()) {
				File newACASDrop = new File(ACAS_Drop_PathNew);
				if (newACASDrop.exists() & newACASDrop.canWrite()) {
					workspacePathACASDrop = newACASDrop;
				} else {
					workspacePathACASDrop = ACASDrop;
					JOptionPane.showMessageDialog(null, "ACAS Drop Folder: " + newACASDrop.toString() + "\n"
							+ "Either No Longer Exists Or Your Profile Does Not Have Permission To Read/Write To This Location\n"
							+ "Default Location In WorkSpace Will Be Used" );
				}
				
			}
			// stigDrop
			String stigDropL = rS.getString("STIG_Drop_Path");
			if (!rS.wasNull()) {
				File stigDirectoryNew = new File(stigDropL);
				if (stigDirectoryNew.exists() & stigDirectoryNew.canWrite()) {
					workspacePathSTIGDrop = stigDirectoryNew;
				} else {
					workspacePathSTIGDrop = stigDirectory;
					JOptionPane.showMessageDialog(null, "STIG Drop Folder: " + stigDirectoryNew.toString() + "\n"
							+ "Either No Longer Exists Or Your Profile Does Not Have Permission To Read/Write To This Location\n"
							+ "Default Location In WorkSpace Will Be Used" );
				}
			}
			// The location where all .CSV files for BI are dropped
			String biDirloc = rS.getString("BI_Drop_Path");
			if (!rS.wasNull()) {
				File biDirFileNew = new File(biDirloc);
				if (biDirFileNew.exists() & biDirFileNew.canWrite()) {
					workspacePathBIExportLocation = biDirFileNew;
				} else {
					workspacePathBIExportLocation = biDirFile;
					JOptionPane.showMessageDialog(null, "BI Directory Folder: " + biDirFileNew.toString() + "\n"
							+ "Either No Longer Exists Or Your Profile Does Not Have Permission To Read/Write To This Location\n"
							+ "Default Location In WorkSpace Will Be Used" );
				}
			}
			// The location where Asset Lists can be dropped
			String Asset_Drop_Path = rS.getString("Asset_Drop_Path");
			if (!rS.wasNull()) {
				File AssetDirDropNew = new File(Asset_Drop_Path);
				if (AssetDirDropNew.exists() & AssetDirDropNew.canWrite()) {
					workspacePathAssetDrop = AssetDirDropNew;
				} else {
					workspacePathAssetDrop = AssetDirDrop;
					JOptionPane.showMessageDialog(null, "Asset Drop Directory Folder: " + AssetDirDropNew.toString() + "\n"
							+ "Either No Longer Exists Or Your Profile Does Not Have Permission To Read/Write To This Location\n"
							+ "Default Location In WorkSpace Will Be Used" );
				}
			} 
		}
	}
}

