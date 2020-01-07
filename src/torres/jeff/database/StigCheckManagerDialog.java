package torres.jeff.database;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

public class StigCheckManagerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private boolean stigCheckerEnabled;
	private JTextField numOfThreadsTxtField;
	private JTextField daysSinceLastScanTxtField;
	private FileDialog fc;

	/**
	 * Launch the application.
	 */
	public static void start(Connection db) {
		try {
			StigCheckManagerDialog dialog = new StigCheckManagerDialog(db);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public StigCheckManagerDialog(Connection db) {
		setBounds(100, 100, 327, 417);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
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
			
			JLabel lblNewLabel = new JLabel("Amount of Programs to Run");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(10, 141, 291, 14);
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
									db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE Stig_Checker_Hostnames = '" + str + "'");
									String[] filePathSplit = str.split(",");
									File file = new File(filePathSplit[1]);
									if (file.isDirectory()) {
										for (File c : file.listFiles()) {
											c.delete();
										}
									}
									file.delete();
									JOptionPane.showMessageDialog(null, "Deletion Successful");
							}
						}	
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "Deletion Unsuccessful For Uknown Reason");
						e.printStackTrace();
					}
				}
			});
			btnDeletePrograms.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnDeletePrograms.setBounds(179, 166, 122, 39);
			contentPanel.add(btnDeletePrograms);
			
			JButton btnHostnames = new JButton("Add Programs");
			btnHostnames.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String hostname;
					String path;
					String hostnameInput = JOptionPane.showInputDialog("Enter HostName of Device to Conduct STIG Checks");
					if (hostnameInput != null) {
						if (!hostnameInput.isEmpty()) {
							hostname = hostnameInput.toUpperCase();
							String userInput = JOptionPane.showInputDialog("Enter Program Path of Device to Conduct STIG Checks,\n"
									+ "Folder Will be Called AutoSTIG Automatically");
							if (userInput != null) {
								if (!userInput.isEmpty()) {
									path = userInput + "\\AutoSTIG";
									File file = new File(path);
									if (!file.exists()) {
										file.mkdirs();
										if (file.exists()) {
											if (file.canWrite()) {
												try {
													String input = hostname + "," + path;
													db.createStatement().execute("INSERT INTO DBO.CONFIG (Stig_Checker_Hostnames) VALUES ('" + input + "')");
													transportJar(input);
													
													JOptionPane.showMessageDialog(null, "Program Added");
												} catch (SQLException | URISyntaxException | IOException e) {
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
			btnHostnames.setBounds(10, 166, 122, 39);
			contentPanel.add(btnHostnames);
			stigCheckerEnabledBool.next();
			boolean stigsEnabled = stigCheckerEnabledBool.getBoolean("Stig_Checker_Enabled");
			stigCheckerEnabled = stigsEnabled;
			
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
	
	private static void transportJar(String path) throws URISyntaxException, IOException {
		CopyOption[] options = new CopyOption[] {
				StandardCopyOption.COPY_ATTRIBUTES,
				StandardCopyOption.REPLACE_EXISTING
				};
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource("RunnableJars/AutoSTIG.exe");
		File file = new File(url.toURI().getPath());
		if (file.exists()) {
			Path source = file.toPath();
			String[] target = path.split(",");
			String realTarget = target[1] + "\\AutoSTIG.exe";
			Path destination = new File(realTarget).toPath();
			Files.copy(source, destination, options);
			}
		}
	private static String getPsExecLocation() throws URISyntaxException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource("PSExec/PsExec.exe");
		File file = new File(url.toURI().getPath());
		return file.toString();
	}
}














