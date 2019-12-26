package torres.jeff.database;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class Gui extends JFrame {

	private JPanel contentPane;
	private static ACAS acas;
	

	/**
	 * Launch the application.
	 */
	public static void run(Connection db, StigUpdater stigUpdater, BiExporter bI, ACAS acasObject) {
		acas = acasObject;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui(db, stigUpdater, bI, acasObject);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui(Connection db, StigUpdater stigUpdater, BiExporter bI, ACAS acasObject) {
		setBounds(100, 100, 337, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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
		btnNewXccdfDirectory.setBounds(10, 431, 137, 39);
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
		btnNewCklDirectory.setBounds(10, 381, 137, 39);
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
		btnNewStigDirectory.setBounds(10, 331, 137, 39);
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
		btnNewBiDirectory.setBounds(171, 331, 137, 39);
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
		btnNewAcasDirectory.setBounds(171, 381, 137, 39);
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
		btnNewAssetDirectory.setBounds(171, 431, 137, 39);
		contentPane.add(btnNewAssetDirectory);
	}
	
	public void changeFilePath(String pathToUpdate, Connection db) throws SQLException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		if (fileChooser.showSaveDialog(fileChooser) == fileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile().toString());
			if (file.canWrite()) {
				db.createStatement().execute("DELETE FROM DBO.CONFIG WHERE " + pathToUpdate + " = '' OR " + pathToUpdate + " IS NOT NULL");
				db.createStatement().execute("INSERT INTO DBO.CONFIG (XCCDF_DROP_PATH) VALUES ('" + file.toString() + "')");
				JOptionPane.showMessageDialog(null, "UPDATE WAS SUCCESSFUL");
			} else if (!file.canWrite()) {
				JOptionPane.showMessageDialog(null, "Your profile does not have permission to access this location,\n"
						+ "Or this location does not exist.");
			} 
		}
	}
}
