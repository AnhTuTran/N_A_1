import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

import protocol.Event;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

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
	 */
	public PeerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
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
					peer.fileInfo = new FileInfoPeer(file.getName(), file.getParent());
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
	}
}
