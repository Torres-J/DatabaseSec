package torres.jeff.database;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JProgressBar;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class Gui extends JFrame {

	private JPanel contentPane;
	private ExecutorService exec;
	private ExecutorService execExecNow;
	private ExecutorService execDBBackupNow;
	private static boolean threadsEnabled;
	private static boolean stigCheckerEnabled = false;


	private static JProgressBar progressBar;
	private static int progressCount;
	/**
	 * Launch the application.
	 */
	public static void run(Connection db, StigUpdater stigUpdater, BiExporter bI, ACAS acasObject, Sockets socket) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui(db, stigUpdater, bI, acasObject, socket);
					frame.setVisible(true);
					
					frame.addWindowListener(new java.awt.event.WindowAdapter() {
					    @Override
					    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					        if (JOptionPane.showConfirmDialog(frame, 
					            "Are you sure you want to exit?", "Exit CyberSec?", 
					            JOptionPane.YES_NO_OPTION,
					            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					        	try {
					        		if (ScheduledTasks.workflowRunning == false) {
					        			db.close();
										System.exit(0);
					        		} else if (ScheduledTasks.workflowRunning == true) {
										JOptionPane.showMessageDialog(null, "Wait For Workflow to Finish or Data Corruption May Occur");
					        		}
								} catch (SQLException e) {
									e.printStackTrace();
								}
					        }
					    }
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws InterruptedException 
	 */
	public Gui(Connection db, StigUpdater stigUpdater, BiExporter bI, ACAS acasObject, Sockets socket) throws InterruptedException, SQLException {
		ResultSet rS = db.createStatement().executeQuery("SELECT THREADS_ENABLED FROM DBO.CONFIG WHERE THREADS_ENABLED IS NOT NULL");
		while (rS.next()) {
			boolean boolValue = rS.getBoolean("THREADS_ENABLED");
			threadsEnabled = boolValue;
		}
		setTitle("CyberSec");
		ImageIcon img = new ImageIcon(this.getClass().getResource("/images/logo.png"));	
		setIconImage(img.getImage());
		setForeground(new Color(240, 255, 255));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 325, 487);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 250, 250));
		contentPane.setForeground(new Color(240, 248, 255));
		contentPane.setBorder(new LineBorder(new Color(255, 0, 0), 2));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewXccdfDirectory = new JButton("New XCCDF Directory");
		btnNewXccdfDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewXccdfDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pathToUpdate = "XCCDF_DROP_PATH";
				try {
					changeFilePath(pathToUpdate, db);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btnNewXccdfDirectory.setBounds(10, 397, 137, 39);
		contentPane.add(btnNewXccdfDirectory);
		
		JButton btnNewCklDirectory = new JButton("New CKL Directory");
		btnNewCklDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewCklDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String CKL_Drop_Path = "CKL_Drop_Path";
				try {
					changeFilePath(CKL_Drop_Path, db);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btnNewCklDirectory.setBounds(10, 347, 137, 39);
		contentPane.add(btnNewCklDirectory);
		
		JButton btnNewStigDirectory = new JButton("New STIG Directory");
		btnNewStigDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewStigDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String stigDropPath = "STIG_Drop_Path";
				try {
					changeFilePath(stigDropPath, db);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewStigDirectory.setBounds(10, 297, 137, 39);
		contentPane.add(btnNewStigDirectory);
		
		JButton btnNewBiDirectory = new JButton("New BI Directory");
		btnNewBiDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewBiDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String biDropPath = "BI_Drop_Path";
				try {
					changeFilePath(biDropPath, db);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewBiDirectory.setBounds(171, 297, 137, 39);
		contentPane.add(btnNewBiDirectory);
		
		JButton btnNewAcasDirectory = new JButton("New ACAS Directory");
		btnNewAcasDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewAcasDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String acasDropPath = "ACAS_Drop_Path";
				try {
					changeFilePath(acasDropPath, db);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewAcasDirectory.setBounds(171, 347, 137, 39);
		contentPane.add(btnNewAcasDirectory);
		
		JButton btnNewAssetDirectory = new JButton("New Asset Directory");
		btnNewAssetDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewAssetDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String assetDropPath = "Asset_Drop_Path";
				try {
					changeFilePath(assetDropPath, db);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewAssetDirectory.setBounds(171, 397, 137, 39);
		contentPane.add(btnNewAssetDirectory);
		
		JButton btnChangeRunInterval = new JButton("Change Run Interval");
		btnChangeRunInterval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialog d = new JDialog();
				d.setTitle("Change Run Interval Time");
				d.getContentPane().setLayout(new FlowLayout());
				String text = "For Start time, enter number between 0-59. 0 means execute at the top of the hour. "
						+ "For interval time, enter a number that represents how many minutes after the start time to execute the workflow. If you enter 60, "
						+ "the program will run every hour after the start time."; 
				JTextArea textArea = new JTextArea(2, 15);
			    textArea.setText(text);
			    textArea.setWrapStyleWord(true);
			    textArea.setLineWrap(true);
			    textArea.setOpaque(false);
			    textArea.setEditable(false);
			    textArea.setFocusable(false);
				
			    JLabel lblStartingTime = new JLabel("Starting Time: "); 
				JTextField txtStartingTime = new JTextField(4);
	            
				JLabel lblIntervalTime = new JLabel("Interval Time: "); 
				JTextField txtIntervalTime = new JTextField(4);
				
				JButton btnUpdate = new JButton("Update");
				btnUpdate.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						if (threadsEnabled == false) {
							JOptionPane.showMessageDialog(null, "You Cannot Change These Values If Threads Are Disabled");							
						}
						else if (txtStartingTime.getText().isEmpty() || txtIntervalTime.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Both Fields Must Contain A Value, Try Again");							
						} else if (!txtStartingTime.getText().isEmpty() & !txtIntervalTime.getText().isEmpty() & (Integer.parseInt(txtIntervalTime.getText()) < 10 || Integer.parseInt(txtStartingTime.getText()) > 59)) {
							JOptionPane.showMessageDialog(null, "Starting Time Must Be between 0-59 And \n"
									+ "Interval Time Must Be Greater Than 10, Try Again");							
						}
							else if (!txtStartingTime.getText().isEmpty() & !txtIntervalTime.getText().isEmpty()) {
							try {
								int startingTimeD = Integer.parseInt(txtStartingTime.getText());
								int intervalTimeD = Integer.parseInt(txtIntervalTime.getText());
								db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE intervalTime IS NOT NULL OR startTime IS NOT NULL");
								db.createStatement().execute("INSERT INTO DBO.CONFIG (startTime) VALUES (" + startingTimeD + ")");
								db.createStatement().execute("INSERT INTO DBO.CONFIG (intervalTime) VALUES ("+ intervalTimeD + ")");
								ScheduledTasks.continueOrStopWhileLoop(false);
								ScheduledTasks.backupWorkflowLoop = false;
								Thread.sleep(2000);
								ScheduledTasks.destroyThread();
								ScheduledTasks.destroyThreadBackup();
								ScheduledTasks.continueOrStopWhileLoop(true);
								ScheduledTasks.backupWorkflowLoop = true;
								shutdownExecService();
								startExecutorService(db, stigUpdater, acasObject, bI);
								JOptionPane.showMessageDialog(null, "UPDATE SUCCESSFUL");
							} catch (SQLException | InterruptedException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "UPDATE UNSUCCESSFUL");
							}							
						} else {
							JOptionPane.showMessageDialog(null, "UPDATE UNSUCCESSFUL \n"
									+ "FOR UNKNOWN REASON");
						}
					}
				});
				btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 15));
				
				d.getContentPane().add(textArea);
				d.getContentPane().add(lblStartingTime); 
				d.getContentPane().add(txtStartingTime);
				
				d.getContentPane().add(lblIntervalTime);
				d.getContentPane().add(txtIntervalTime);
				
				d.getContentPane().add(btnUpdate);
				
	            d.pack();
	            d.setSize(225, 325); 
	            d.setResizable(false);
	            d.setLocationRelativeTo(contentPane);
	            d.setVisible(true); 
			}
		});
		btnChangeRunInterval.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnChangeRunInterval.setBounds(10, 146, 137, 39);
		contentPane.add(btnChangeRunInterval);
		
		progressBar = new JProgressBar();
		progressBar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setValue(0);
		progressBar.setString("Waiting");
		progressBar.setStringPainted(true);
		progressBar.setMaximum(9);
		progressBar.setBounds(71, 36, 176, 31);
		contentPane.add(progressBar);
		
		JButton btnRunNow = new JButton("Run Now");
		btnRunNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				execExecNow = Executors.newFixedThreadPool(1);
				execExecNow.execute(new Runnable() {
					public void run() {
						ScheduledTasks.immediateWorkflowExecution(db, stigUpdater, acasObject, bI);
						workflowStartingImmediate();
					}
				});
				execExecNow.shutdown();
			}
		});
		btnRunNow.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnRunNow.setBounds(10, 96, 137, 39);
		contentPane.add(btnRunNow);
		
		JLabel lblWorkflowStatus = new JLabel("Workflow Status");
		lblWorkflowStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblWorkflowStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblWorkflowStatus.setBounds(71, 11, 176, 25);
		contentPane.add(lblWorkflowStatus);
		
		JButton btnNewBackupDirectory = new JButton("New Backup Directory");
		btnNewBackupDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String backupDropPath = "DATABASE_BACKUP_DROP";
				try {
					changeFilePath(backupDropPath, db);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewBackupDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNewBackupDirectory.setBounds(93, 247, 137, 39);
		contentPane.add(btnNewBackupDirectory);
		
		JToggleButton tglbtnDisableAutomation = new JToggleButton("Disable Automation");
		if (threadsEnabled == false) {
			tglbtnDisableAutomation.setSelected(true);
			progressBar.setString("Not Running");
		}
		tglbtnDisableAutomation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tglbtnDisableAutomation.isSelected()) {
					ScheduledTasks.backupWorkflowLoop = false;
					ScheduledTasks.continueLoop = false;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					shutdownExecService();
					ScheduledTasks.destroyThread();
					ScheduledTasks.destroyThreadBackup();
					try {
						db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE THREADS_ENABLED IS NOT NULL");
						db.createStatement().execute("INSERT INTO DBO.CONFIG (THREADS_ENABLED) VALUES (false)");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					threadsEnabled = false;
					progressBar.setString("Not Running");
					JOptionPane.showMessageDialog(null, "Threads Are Stopped");
				} else if (!tglbtnDisableAutomation.isSelected()) {
					ScheduledTasks.backupWorkflowLoop = true;
					ScheduledTasks.continueLoop = true;
					try {
						db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE THREADS_ENABLED IS NOT NULL");
						db.createStatement().execute("INSERT INTO DBO.CONFIG (THREADS_ENABLED) VALUES (true)");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					threadsEnabled = true;
					startExecutorService(db, stigUpdater, acasObject, bI);
					progressBar.setString("Waiting");
					JOptionPane.showMessageDialog(null, "Threads Are Enabled");
				}
			}
		});
		tglbtnDisableAutomation.setFont(new Font("Tahoma", Font.PLAIN, 10));
		tglbtnDisableAutomation.setBounds(171, 146, 137, 39);
		contentPane.add(tglbtnDisableAutomation);
		
		JButton btnBackupDatabase = new JButton("Backup Database");
		btnBackupDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				execDBBackupNow = Executors.newFixedThreadPool(1); 
				execDBBackupNow.execute(new Runnable() {
					public void run() {
						try {
							backupDatabaseNow(db);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				execDBBackupNow.shutdown();
			}
		});
		btnBackupDatabase.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnBackupDatabase.setBounds(171, 96, 137, 39);
		contentPane.add(btnBackupDatabase);
		
		JButton btnDeleteStigs = new JButton("Delete STIG's");
		btnDeleteStigs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> stigList = new ArrayList<String>();
				try {
					ResultSet rs = db.createStatement().executeQuery("SELECT DISTINCT STIG FROM DBO.STIG_TABLE");
					while (rs.next()) {
						stigList.add(rs.getString("STIG"));
					}
					String[] stigListString = new String[stigList.size()];
					stigListString = stigList.toArray(stigListString);
					JComboBox cb = new JComboBox(stigListString);
					int input;
					input = JOptionPane.showConfirmDialog(null, cb,"Select STIG To Delete", JOptionPane.DEFAULT_OPTION);
					if (input == JOptionPane.OK_OPTION) {
						String str = (String) cb.getSelectedItem();
						int confirmBox = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Delete\n"
								+ str, " Delete This STIG", JOptionPane.YES_NO_OPTION);
						if (confirmBox == JOptionPane.YES_OPTION) {
							if (ScheduledTasks.workflowRunning == false) {
								ScheduledTasks.workflowRunning = true;
								db.createStatement().execute("DELETE FROM DBO.STIG_TABLE WHERE STIG = '" + str + "'");
								db.createStatement().execute("delete from dbo.Ongoing "
										+ "WHERE CUST_ID NOT IN ("
										+ "SELECT dbo.Ongoing.CUST_ID "
										+ "FROM dbo.Ongoing "
										+ "JOIN dbo.Stig_Table ON dbo.Stig_Table.V_ID = dbo.Ongoing.V_ID AND dbo.Stig_Table.STIG = dbo.Ongoing.STIG "
										+ "WHERE dbo.Stig_Table.V_ID IS NOT NULL AND dbo.Stig_Table.Stig IS NOT NULL)");
								
								db.createStatement().execute("delete from dbo.Completed "
										+ "WHERE CUST_ID NOT IN ("
										+ "SELECT dbo.Completed.CUST_ID "
										+ "FROM dbo.Completed "
										+ "JOIN dbo.Stig_Table ON dbo.Stig_Table.V_ID = dbo.Completed.V_ID AND dbo.Stig_Table.STIG = dbo.Completed.STIG "
										+ "WHERE dbo.Stig_Table.V_ID IS NOT NULL AND dbo.Stig_Table.Stig IS NOT NULL)");
								
								db.createStatement().execute("delete from dbo.Main_Table "
										+ "WHERE CUST_ID NOT IN ("
										+ "SELECT dbo.Main_Table.CUST_ID "
										+ "FROM dbo.Main_Table "
										+ "JOIN dbo.Stig_Table ON dbo.Stig_Table.V_ID = dbo.Main_Table.V_ID AND dbo.Stig_Table.STIG = dbo.Main_Table.STIG "
										+ "WHERE dbo.Stig_Table.V_ID IS NOT NULL AND dbo.Stig_Table.Stig IS NOT NULL)");
								ScheduledTasks.workflowRunning = false;
								JOptionPane.showMessageDialog(null, "Deletion Successful");
							} else if (ScheduledTasks.workflowRunning == true) {
								JOptionPane.showMessageDialog(null, "Workflow Currently Running, Try Again When It's Done");
							}
						}
						
					}	
				} catch (SQLException e) {
					ScheduledTasks.workflowRunning = false;
					JOptionPane.showMessageDialog(null, "Deletion Unsuccessful For Uknown Reason");
					e.printStackTrace();
				}
			}
		});
		btnDeleteStigs.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnDeleteStigs.setBounds(10, 197, 137, 39);
		contentPane.add(btnDeleteStigs);
		
		JButton btnStigChecker = new JButton("STIG Checker");
		btnStigChecker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				StigCheckManagerDialog dialog = new StigCheckManagerDialog(db);
				dialog.setVisible(true);
				dialog.setResizable(false);
				dialog.setLocationRelativeTo(contentPane);
			}
		});
		btnStigChecker.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnStigChecker.setBounds(171, 197, 137, 39);
		contentPane.add(btnStigChecker);
		
		// This executes the workflow thread
		startExecutorService(db, stigUpdater, acasObject, bI);
	}
	
	private void changeFilePath(String pathToUpdate, Connection db) throws SQLException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir").toString()));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		if (fileChooser.showSaveDialog(fileChooser) == fileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile().toString());
			if (file.canWrite()) {
				db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE " + pathToUpdate + " = '' OR " + pathToUpdate + " IS NOT NULL");
				db.createStatement().execute("INSERT INTO DBO.CONFIG ("+ pathToUpdate +") VALUES ('" + file.toString() + "')");
				JOptionPane.showMessageDialog(null, "UPDATE WAS SUCCESSFUL");
			} else if (!file.canWrite()) {
				JOptionPane.showMessageDialog(null, "Your profile does not have permission to access this location,\n"
						+ "Or this location does not exist.");
			} 
		}
	}
	private void startExecutorService(Connection db, StigUpdater stigUpdater, ACAS acasObject, BiExporter bI) {
		exec = Executors.newFixedThreadPool(2);
		exec.execute(new Runnable() {
			public void run() {
				try {
					ScheduledTasks.executeTasks(db, stigUpdater, acasObject, bI, threadsEnabled);
					
				} catch (InterruptedException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
		exec.execute(new Runnable() {
			public void run() {
				try {
					ScheduledTasks.backupDatabase(db, threadsEnabled);
				} catch (SQLException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		exec.shutdown();
	}
	private void shutdownExecService() {
		exec.shutdownNow();
	}
	public static void addProgress() {
		progressCount++;
		switch (progressCount) {
		case 1: progressBar.setString("Creating Folders"); progressBar.setValue(progressCount); break;
		case 2: progressBar.setString("Importing Assets"); progressBar.setValue(progressCount); break;
		case 3: progressBar.setString("Updating STIG's"); progressBar.setValue(progressCount); break;
		case 4: progressBar.setString("Parsing CKL's"); progressBar.setValue(progressCount); break;
		case 5: progressBar.setString("Parsing XCCDF's"); progressBar.setValue(progressCount); break;
		case 6: progressBar.setString("Running Queries"); progressBar.setValue(progressCount); break;
		case 7: progressBar.setString("Parsing ACAS"); progressBar.setValue(progressCount); break;
		case 8: progressBar.setString("Exporting BI Files"); progressBar.setValue(progressCount); break;
		case 9: progressBar.setString("Done"); progressBar.setValue(progressCount); break;
		}	
	}
	public static void resetProgress() {
		progressCount = 0;
		progressBar.setValue(0);
		if (threadsEnabled == false) {
			progressBar.setString("Not Running");
		} else if (threadsEnabled == true) {
			progressBar.setString("Waiting");
		}
	}
	public static void workflowStartingImmediate() {
		if (ScheduledTasks.workflowRunning == true) {
			JOptionPane.showMessageDialog(null, "Not Started: Queries Currently Running");
		} else if (ScheduledTasks.workflowRunning == false) {
		}
	}
	private static void backupDatabaseNow(Connection db) throws InterruptedException {
		while (true) {
	    	if (ScheduledTasks.workflowRunning == false) {
	    		ScheduledTasks.workflowRunning = true;
	    		try {
					db.createStatement().execute("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE('" + CreateFolderStructure.backupDirectoryLocation.toString() + "')");
					ScheduledTasks.workflowRunning = false;
					JOptionPane.showMessageDialog(null, "Database Backup Successful");
					break;
				} catch (SQLException e) {
					ScheduledTasks.workflowRunning = false;
					JOptionPane.showMessageDialog(null, "Database Backup Unsuccessful");
					e.printStackTrace();
				}
	    	}
			Thread.sleep(2000);		
	    }
	}
}





