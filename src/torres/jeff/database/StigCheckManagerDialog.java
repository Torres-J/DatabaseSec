package torres.jeff.database;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;
import org.springframework.util.FileSystemUtils;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class StigCheckManagerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private boolean stigCheckerEnabled;
	private JTextField numOfThreadsTxtField;
	private JTextField daysSinceLastScanTxtField;
	private static int programsRunningCount;
	private static int programsTotalCount;
	private static JLabel lblCurrentRunning;
	
	/**
	 * Launch the application.
	 */
	public static void start(Connection db, SocketsServer socket) {
		try {
			StigCheckManagerDialog dialog = new StigCheckManagerDialog(db, socket);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @throws URISyntaxException 
	 */
	public StigCheckManagerDialog(Connection db, SocketsServer socket) throws URISyntaxException {
		setTitle("AutoSTIG Manager");
		ImageIcon img = new ImageIcon(this.getClass().getResource("/images/logo.png"));	
		setIconImage(img.getImage());
		setModal(true);
		setBounds(100, 100, 327, 417);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setForeground(new Color(240, 255, 255));
		contentPanel.setBorder(new LineBorder(Color.RED, 2));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		numOfThreadsTxtField = new JTextField();
		numOfThreadsTxtField.setHorizontalAlignment(SwingConstants.CENTER);
		numOfThreadsTxtField.setToolTipText("");
		numOfThreadsTxtField.setBounds(52, 113, 30, 20);
		contentPanel.add(numOfThreadsTxtField);
		numOfThreadsTxtField.setColumns(2);
		
		daysSinceLastScanTxtField = new JTextField();
		daysSinceLastScanTxtField.setHorizontalAlignment(SwingConstants.CENTER);
		daysSinceLastScanTxtField.setColumns(10);
		daysSinceLastScanTxtField.setBounds(220, 113, 30, 20);
		contentPanel.add(daysSinceLastScanTxtField);
		
		JToggleButton stigDisabled = new JToggleButton("Disable STIG Checks");
		stigDisabled.setFont(new Font("Tahoma", Font.PLAIN, 10));
		stigDisabled.setBounds(79, 328, 147, 39);
		contentPanel.add(stigDisabled);
		
		// gets result values
	    try {
	    	ResultSet threads = db.createStatement().executeQuery("SELECT Threads FROM dbo.Config WHERE Threads IS NOT NULL");
	    	ResultSet daysSinceLastScan = db.createStatement().executeQuery("SELECT DAYS_SINCE_SCAN FROM dbo.Config WHERE DAYS_SINCE_SCAN IS NOT NULL");
	    	ResultSet stigCheckerEnabledBool = db.createStatement().executeQuery("SELECT Stig_Checker_Enabled FROM dbo.Config WHERE Stig_Checker_Enabled IS NOT NULL");
	    	threads.next();
			int numOfThreads = threads.getInt("Threads");
			numOfThreadsTxtField.setText(String.valueOf(numOfThreads));
			daysSinceLastScan.next();
			int daysSinceLastScanInt = daysSinceLastScan.getInt("DAYS_SINCE_SCAN");
			daysSinceLastScanTxtField.setText(String.valueOf(daysSinceLastScanInt));
			
			JLabel lblNumberOfThreads = new JLabel("Number of Threads");
			lblNumberOfThreads.setHorizontalAlignment(SwingConstants.CENTER);
			lblNumberOfThreads.setBounds(10, 88, 111, 14);
			contentPanel.add(lblNumberOfThreads);
			
			JLabel lblDaysSince = new JLabel("Days Since Last Scan");
			lblDaysSince.setHorizontalAlignment(SwingConstants.CENTER);
			lblDaysSince.setBounds(167, 88, 134, 14);
			contentPanel.add(lblDaysSince);
			
			JLabel lblNewLabel = new JLabel("Programs Configuration");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(10, 203, 291, 14);
			contentPanel.add(lblNewLabel);
			
			JButton btnDeletePrograms = new JButton("Delete Programs");
			btnDeletePrograms.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ArrayList<String> array = new ArrayList<String>();
					try {
						ResultSet rs = db.createStatement().executeQuery("SELECT DISTINCT Stig_Checker_Hostnames FROM DBO.CONFIG WHERE Stig_Checker_Hostnames IS NOT NULL");
						while (rs.next()) {
							array.add(rs.getString("Stig_Checker_Hostnames"));
						}
						String[] stigListString = new String[array.size()];
						stigListString = array.toArray(stigListString);
						JComboBox cb = new JComboBox(stigListString);
						int input;
						input = JOptionPane.showConfirmDialog(null, cb,"Select Program To Delete", JOptionPane.DEFAULT_OPTION);
						if (input == JOptionPane.OK_OPTION) {
							String str = (String) cb.getSelectedItem();
							int confirmBox = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Delete\n"
									+ str, " Delete This Program", JOptionPane.YES_NO_OPTION);
							if (confirmBox == JOptionPane.YES_OPTION) {
								if (!(str == "null")) {
									String[] filePathSplit = str.split(",");
									socket.sendShutdown(filePathSplit[0]);
									Thread.sleep(4000);
									if (socket.targetShutdown()) {
										socket.setTargetShutdown(false);
										File file = new File(filePathSplit[1]);
										if (file.exists()) {
											boolean success = FileSystemUtils.deleteRecursively(file);
											if (success) {
												db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE Stig_Checker_Hostnames = '" + str + "'");
												JOptionPane.showMessageDialog(null, "Deletion Successful");
											} else if (!success) {
												JOptionPane.showMessageDialog(null, "Deletion Unsuccessful");
											}
										} else if (!file.exists()) {
											db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE Stig_Checker_Hostnames = '" + str + "'");
											JOptionPane.showMessageDialog(null, "Files On Record Do Not Exist");
										}
										
										
									} else if (!socket.targetShutdown()) {
										socket.setTargetShutdown(false);
										File file = new File(filePathSplit[1]);
										if (file.exists()) {
											boolean success = FileSystemUtils.deleteRecursively(file);
											if (success) {
												db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE Stig_Checker_Hostnames = '" + str + "'");
												JOptionPane.showMessageDialog(null, "Deletion Successful");
											} else if (!success) {
												JOptionPane.showMessageDialog(null, "Deletion Unsuccessful");
											}
										}else if (!file.exists()) {
											db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE Stig_Checker_Hostnames = '" + str + "'");
											JOptionPane.showMessageDialog(null, "Files On Record Do Not Exist");
										}
									}
									
								}
							}
						}	
					} catch (SQLException e) {
						socket.setTargetShutdown(false);
						JOptionPane.showMessageDialog(null, "Deletion Unsuccessful For Uknown Reason");
						e.printStackTrace();
					} catch (InterruptedException e) {
						socket.setTargetShutdown(false);
						JOptionPane.showMessageDialog(null, "Task Could Not Be Killed On Remote Server");
					}
				}
			});
			btnDeletePrograms.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnDeletePrograms.setBounds(179, 228, 122, 39);
			contentPanel.add(btnDeletePrograms);
			
			JButton btnHostnames = new JButton("Add Programs");
			btnHostnames.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					String hostname;
					String path;
					JTextField userNameField = new JTextField();
					JTextField passwordField = new JPasswordField();
					
					String hostnameInput = JOptionPane.showInputDialog("Enter HostName of Device to Conduct STIG Checks");

					if (hostnameInput != null) {
						if (!hostnameInput.isEmpty()) {
							hostname = hostnameInput.toUpperCase();
							String userInput = JOptionPane.showInputDialog("Enter Program Path of Device to Conduct STIG Checks,\n"
									+ "Folder Will be Called AutoSTIG Automatically, EXAMPLE:\n"
									+ "C:\\Program Files\\CommandSoftware\n\n"
									+ "*Note Paths That Require UAC Authentication Will Fail*");
							if (userInput != null) {
								if (!userInput.isEmpty()) {
									path = "\\\\" + hostname + "\\" + userInput + "\\AutoSTIG";
									path = path.replace(":", "$");
									File file = new File(path);
									if (!file.exists()) {
										file.mkdirs();
										if (file.exists()) {
											if (file.canWrite()) {
												try {
													while (true) {

														String[] buttons = { "Service Account", "Manual"};    
														int returnValue = JOptionPane.showOptionDialog(null, "Service Account - This Option will require a username and password and enterprise admin\n"
																+ "privilages to run the AutoSTIG program. A scheduled task will be created on the\n"
																+ "target host\n\n"
																+ "Manual - This Option will not require a service account, however the end user requires\n"
																+ "administrative privilages on the endpoint running the AutoSTIG and\n"
																+ "all hosts that will be scanned. They must also start the program manually", "Option Pane ",
														        JOptionPane.PLAIN_MESSAGE, 0, null, buttons, buttons);
														
														if (returnValue == 0) {
															Object[] message = {
																    "Username:", userNameField,
																    "Password:", passwordField
																};
															int option = JOptionPane.showConfirmDialog(null, message, "AutoSTIG Specifications", JOptionPane.OK_CANCEL_OPTION);
															if (option == JOptionPane.OK_OPTION) {
															    if (!userNameField.getText().isEmpty() & !passwordField.getText().isEmpty()) {
															    	transportJar(path);
															    	String scheduledTaskPath = hostname + "," + "\\\\" + hostname + "\\" + userInput + "\\AutoSTIG";
															    	scheduledTaskPath = scheduledTaskPath.replace(":", "$");
																	db.createStatement().execute("INSERT INTO DBO.CONFIG (Stig_Checker_Hostnames, Stig_Checker_Finished) VALUES ('" + scheduledTaskPath + "','yes')");
																    String pathForSchedTask = userInput + "\\AutoSTIG\\AutoSTIG.exe";
																	Process createScheduledTask = Runtime.getRuntime().exec("cmd.exe /c SCHTASKS /Create /S "+ hostname + " /RU " + userNameField.getText() + " /RP " + passwordField.getText()
																	+ " /SC ONSTART /TN AutoSTIG /TR \"" + pathForSchedTask + "\"");
																	InputStream stdIn = createScheduledTask.getInputStream();
																	InputStreamReader isr = new InputStreamReader(stdIn);
																	BufferedReader br = new BufferedReader(isr);
																	String line = null;
																	//
																	// WIP HAVE TO CREATE THE LOGIC TO ENSURE THAT THE CREATION OF THE TASK SUCCEEDED
																	//
																	while ((line = br.readLine()) != null) {
																		System.out.println(line);
																	}
																	JOptionPane.showMessageDialog(null, "Program Added");
																	break;
															    } else {
																	JOptionPane.showMessageDialog(null, "UserName And Password Field Cannot Be Empty");
																} 												
															} else {
																break;
															}
														} else if (returnValue == 1) {
															transportJar(path);
															String scheduledTaskPath = hostname + "," + "\\\\" + hostname + "\\" + userInput + "\\AutoSTIG";
													    	scheduledTaskPath = scheduledTaskPath.replace(":", "$");
															db.createStatement().execute("INSERT INTO DBO.CONFIG (Stig_Checker_Hostnames, Stig_Checker_Finished) VALUES ('" + scheduledTaskPath + "','yes')");
															JOptionPane.showMessageDialog(null, "Program Added, Remember to Start The Program\n"
																	+ "At The Endpoint Specified Below:\n" + scheduledTaskPath);

															break;
															//
															//
															//
															//
															//
															//
															//
														} else if (returnValue == -1) {
															break;
														}
													}
												} catch (SQLException | IOException e) {
													
													JOptionPane.showMessageDialog(null, "Error(193): Something Went Wrong");
												}
											}
										} else if (!file.exists() || !file.canWrite()) {
											JOptionPane.showMessageDialog(null, "Your profile does not have permission to access this location,\n"
													+ "Or this location does not exist.");
										}
									} else if (file.exists()) {
										JOptionPane.showMessageDialog(null, "Program Already Exists");
									}
									/* else if (file.canWrite()) {
										try {
											String input = hostname + "," + path;
											db.createStatement().execute("INSERT INTO DBO.CONFIG (Stig_Checker_Hostnames) VALUES ('" + input + "')");
											transportJar(input);
											JOptionPane.showMessageDialog(null, "Program Added");
										} catch (SQLException | URISyntaxException | IOException e) {
										}
									}*/
									else if (!file.canWrite()){
										JOptionPane.showMessageDialog(null, "Your profile does not have permission to access this location,\n"
												+ "Or this location does not exist.");
									}
								} else if (userInput.isEmpty()) {
									JOptionPane.showMessageDialog(null, "You Must Enter a Valid Path");
								}
							}
						} else if (hostnameInput.isEmpty()) {
							JOptionPane.showMessageDialog(null, "You Must Enter a HostName");
						}
					}							
				}
			});
			btnHostnames.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnHostnames.setBounds(10, 228, 122, 39);
			contentPanel.add(btnHostnames);
			
			JLabel lblProgramsRunning = new JLabel("Programs Running");
			lblProgramsRunning.setHorizontalAlignment(SwingConstants.CENTER);
			lblProgramsRunning.setBounds(99, 23, 111, 14);
			contentPanel.add(lblProgramsRunning);
			
			lblCurrentRunning = new JLabel("None");
			lblCurrentRunning.setHorizontalAlignment(SwingConstants.CENTER);
			lblCurrentRunning.setBounds(99, 49, 111, 14);
			contentPanel.add(lblCurrentRunning);
			stigCheckerEnabledBool.next();
			boolean stigsEnabled = stigCheckerEnabledBool.getBoolean("Stig_Checker_Enabled");
			stigCheckerEnabled = stigsEnabled;
			lblCurrentRunning.setText(setProgramRunningInfo());
			
			JButton btnImportHosts = new JButton("Import Targets");
			btnImportHosts.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(null, "Choose a CSV with HostNames for AutoSTIG to scan.\n"
							+ "HostNames are appended to the current list.\n"
							+ "HostNames should be in the first column.");
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir").toString()));
					FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV File", "csv");
					fileChooser.setFileFilter(filter);
					if (fileChooser.showSaveDialog(fileChooser) == fileChooser.APPROVE_OPTION) {
						File file = new File(fileChooser.getSelectedFile().toString());
						BufferedReader csvReader = null;
				    	PreparedStatement pS;
						try {
							pS = db.prepareStatement("INSERT INTO dbo.AutoSTIG_Targets (Host_Name, Latest_Version) VALUES (?,?)");
							csvReader = new BufferedReader(new FileReader(file));	
							String row;
							//db.createStatement().execute("DELETE FROM DBO.AutoSTIG_Targets");
							while ((row = csvReader.readLine()) != null) {
								String hostName = row.toUpperCase();
								pS.setString(1, hostName);
								pS.setString(2, "No");
								pS.addBatch();
							}
							pS.executeBatch();
							csvReader.close();
							JOptionPane.showMessageDialog(null, "HostNames Added");
							//file.delete();
					} catch (Exception e) {
						try {
							csvReader.close();
							JOptionPane.showMessageDialog(null, "Error Adding HostNames Ensure CSV Format is Correct");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						e.printStackTrace();
						}
					}
				}
			});
			btnImportHosts.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnImportHosts.setBounds(10, 278, 122, 39);
			contentPanel.add(btnImportHosts);
			
			JButton btnRemoveTargets = new JButton("Remove Targets");
			btnRemoveTargets.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String[] buttons = { "Yes", "No"};
					int returnValue = JOptionPane.showOptionDialog(null, "Delete All AutoSTIG Assets?", "Option Pane ", JOptionPane.PLAIN_MESSAGE, 0, null, buttons, buttons);
					if (returnValue == 0) {
						try {
							db.createStatement().execute("DELETE FROM DBO.AutoSTIG_Targets");
							JOptionPane.showMessageDialog(null, "HostNames Removed");
						} catch (SQLException e) {
							JOptionPane.showMessageDialog(null, "Error Removing HostNames");
						}
					} 					
				}
			});
			btnRemoveTargets.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnRemoveTargets.setBounds(179, 278, 122, 39);
			contentPanel.add(btnRemoveTargets);
			
			JButton btnUpdate = new JButton("Update");
			btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnUpdate.setBounds(99, 112, 104, 20);
			contentPanel.add(btnUpdate);
			
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    if (stigCheckerEnabled == true) {
	    	stigDisabled.setSelected(false);
	    } else if (stigCheckerEnabled == false) {
	    	stigDisabled.setSelected(true);
	    }
	    stigDisabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (stigDisabled.isSelected()) {
					stigCheckerEnabled = false;
					try {
						db.createStatement().execute("UPDATE DBO.CONFIG SET Stig_Checker_Enabled = false WHERE Stig_Checker_Enabled = true");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if (!stigDisabled.isSelected()) {
					stigCheckerEnabled = true;
					try {
						db.createStatement().execute("UPDATE DBO.CONFIG SET Stig_Checker_Enabled = true WHERE Stig_Checker_Enabled = false");
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			}	
	    });
	}
	
	public void transportJar(String path) {
		try {
			CopyOption[] options = new CopyOption[] {
					StandardCopyOption.COPY_ATTRIBUTES,
					StandardCopyOption.REPLACE_EXISTING 
					};
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream in = classLoader.getResourceAsStream("RunnableJars/AutoSTIG.exe");	
			String realTarget = path + "\\AutoSTIG.exe";
			FileOutputStream out = new FileOutputStream(new File(realTarget));
			IOUtils.copy(in, out);
			in.close();
			out.close();
			

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			JOptionPane.showMessageDialog(null, exceptionAsString);
		}
		}
	
			
	private static String getPsExecLocation() throws URISyntaxException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource("PSExec/PsExec.exe");
		File file = new File(url.toURI().getPath());
		return file.toString();
	}
	public static void setMaxHostsRunning(int i) {	
		programsTotalCount = i;
	}
	public static void setTotalRunning() {
		programsRunningCount++;
	}
	public static void resetRunningPrograms() {
		programsRunningCount = 0;
		programsTotalCount = 0;
	}
	public static String setProgramRunningInfo() {
		String running = String.valueOf(programsRunningCount);
		String total = String.valueOf(programsTotalCount);
		String retValue = running + " / " + total;
		lblCurrentRunning.setText(retValue);
		return retValue;		
	}
}














