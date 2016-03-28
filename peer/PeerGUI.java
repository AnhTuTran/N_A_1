import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import protocol.Event;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JLabel;

public class PeerGUI {

	private JFrame frame;

	File file;
	Peer peer = new Peer();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PeerGUI window = new PeerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public PeerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				peer.connectToServer(Event.ConnectingToServer);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnConnect, 44, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnConnect, 44, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(btnConnect);
		
		
		JButton btnChooseFileTo = new JButton("Choose file");
		btnChooseFileTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				file = chooser.getSelectedFile();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnChooseFileTo, 30, SpringLayout.SOUTH, btnConnect);
		springLayout.putConstraint(SpringLayout.WEST, btnChooseFileTo, 0, SpringLayout.WEST, btnConnect);
		frame.getContentPane().add(btnChooseFileTo);
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					peer.fileInfo = new FileInfoPeer(file.getName(), file.getParent(), true);
					
					peer.connectToServer(Event.UploadFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				//System.out.println(file.getPath());
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnUpload, 22, SpringLayout.SOUTH, btnChooseFileTo);
		springLayout.putConstraint(SpringLayout.WEST, btnUpload, 0, SpringLayout.WEST, btnConnect);
		frame.getContentPane().add(btnUpload);
		
		JTextArea textArea = new JTextArea();
		springLayout.putConstraint(SpringLayout.NORTH, textArea, 0, SpringLayout.NORTH, btnConnect);
		springLayout.putConstraint(SpringLayout.WEST, textArea, 20, SpringLayout.EAST, btnChooseFileTo);
		springLayout.putConstraint(SpringLayout.SOUTH, textArea, 0, SpringLayout.SOUTH, btnUpload);
		springLayout.putConstraint(SpringLayout.EAST, textArea, 268, SpringLayout.EAST, btnConnect);
		frame.getContentPane().add(textArea);
		
		JButton btnListFile = new JButton("List file ");
		btnListFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				peer.connectToServer(Event.ListFilesCanDownload);
				textArea.setText(null);
				for (String str : peer.filesCanDownload) {
					textArea.append(str + "\n");;
				}
				///////////
				JList list = new JList(peer.filesCanDownload.toArray());
				
				list.addListSelectionListener(new ListSelectionListener() {
					@SuppressWarnings("deprecation")
					public void valueChanged(ListSelectionEvent event) {
						if (event.getValueIsAdjusting()) { 
							int fileIndex = list.getSelectedIndex();
							System.out.println("Downloading file: " + peer.filesCanDownload.get(fileIndex));
							peer.fileNameToDownload = peer.filesCanDownload.get(fileIndex);
						//	try {
								
								//peer.fileInfo = new FileInfoPeer(peer.fileNameToDownload, System.getProperty("user.dir"));
						//	} catch (IOException e) {
						//		// TODO Auto-generated catch block
						//		e.printStackTrace();
						//	}
							peer.connectToServer(Event.DownloadFile);
							
						}
						
					}
				});
				
				frame.getContentPane().add(list, BorderLayout.CENTER);
								
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnListFile, 19, SpringLayout.SOUTH, textArea);
		springLayout.putConstraint(SpringLayout.WEST, btnListFile, 0, SpringLayout.WEST, textArea);
		frame.getContentPane().add(btnListFile);
		
		JButton btnNewButton = new JButton("Download");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 0, SpringLayout.NORTH, btnListFile);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton, 0, SpringLayout.EAST, textArea);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblPeer = new JLabel("Peer");
		springLayout.putConstraint(SpringLayout.WEST, lblPeer, 0, SpringLayout.WEST, btnConnect);
		springLayout.putConstraint(SpringLayout.SOUTH, lblPeer, -6, SpringLayout.NORTH, btnConnect);
		frame.getContentPane().add(lblPeer);
		
				
	}
}
