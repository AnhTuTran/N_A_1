import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import protocol.*;

public class Peer {
	Socket client;
	List<FileInfoPeer> listFileInfo = new ArrayList<FileInfoPeer>();
	FileInfoPeer fileInfo;
	List<String> filesCanDownload = new ArrayList<String>();
	String fileNameToDownload;
	boolean isConnected = false;
	
	boolean connectToServer(Event event) {
		try {
			if (!isConnected && event == Event.ConnectingToServer) {
				client = new Socket(ServerInfo.serverName, ServerInfo.serverPort);
				isConnected = true;
			}
			
			OutputStream outToServer = client.getOutputStream();
			InputStream inFromServer = client.getInputStream();
			
			DataOutputStream out = new DataOutputStream(outToServer);
			DataInputStream in = new DataInputStream(inFromServer);
			
			out.writeUTF(Integer.toString(event.ordinal()));	// request server an event 
			
			if (Integer.parseInt(in.readUTF()) ==				// reading ack from server
					event.ordinal() + 1) {
				
				switch (event) {
				case ConnectingToServer:
					System.out.println("ConnectingToServer");
					connectToSeverAction(in, out);
					break;
				case UploadFile:
					System.out.println("UploadFile");
					upLoadFileAction(in, out);
					break;
				case ListFilesCanDownload:
					System.out.println("ListFilesCanDownload");
					filesCanDownloadHandler(in, out);
					break;
				case DownloadFile:
					System.out.println("DownloadFile");
					downloadFileAction(in,out);
					System.out.println("End download");
					break;
				default:
					System.out.println("Nop");
				}
				return true;
			}				
				
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Nop1");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Nop2");
		}
		return false;
	
	}
	
	void connectToSeverAction(DataInputStream in, DataOutputStream out) throws IOException {
		
		out.writeUTF((client.getLocalSocketAddress().toString().substring(1)));
		System.out.println("Sending host ip and port to server");
		//System.out.println(client.getLocalSocketAddress() + "  " + client.getRemoteSocketAddress());
	}
	
	void upLoadFileAction(DataInputStream in, DataOutputStream out) throws IOException {
		boolean isNameInList = false;
		
		for (FileInfoPeer fi : listFileInfo) {
			if (fi.fileName.equals(fileInfo.fileName)) {
				isNameInList = true;
				break;
			}
		}
		
		if (!isNameInList) {
			Arrays.fill(fileInfo.chunkStatus, true);
			listFileInfo.add(fileInfo);
			
			out.writeUTF((client.getLocalSocketAddress().toString().substring(1)));
			out.writeUTF(fileInfo.fileName);
			out.writeUTF(Integer.toString(fileInfo.numberOfChunks));
			
			System.out.println(fileInfo.fileName);
			System.out.println(fileInfo.numberOfChunks);
			System.out.println("Sending info to server");
			
			//System.out.println(in.readInt());
			
			Runnable r = new ListenToPeerHandler(fileInfo, in.readInt());
			Thread t = new Thread(r);
			t.start();
			
			
		}
		else {
			System.out.println("Find already in list");
		}
		
	}
	
	void filesCanDownloadHandler(DataInputStream in, DataOutputStream out) 
			throws NumberFormatException, IOException {

		int numberOfFiles = Integer.parseInt(in.readUTF());
		for (int i = 0; i < numberOfFiles; i++) {
			String str = in.readUTF();
			boolean isInList = false;
			
			for (String s : filesCanDownload) {
				if (s.equals(str)) {
					isInList = true;
					break;
				}
			}
			if (!isInList) {
				filesCanDownload.add(str);
			}
			
		}
	}
		
	void downloadFileAction (DataInputStream in, DataOutputStream out) {
		try {
			out.writeUTF(fileNameToDownload);
			fileInfo = new FileInfoPeer(in.readUTF(),System.getProperty("user.dir"), false);
			fileInfo.numberOfChunks = in.readInt();
			fileInfo.chunkStatus = new boolean[fileInfo.numberOfChunks];
			Arrays.fill(fileInfo.chunkStatus, false);
			
			boolean isNameInList = false;
			for (FileInfoPeer fi : listFileInfo) {
				if (fi.fileName.equals(fileInfo.fileName)) {
					isNameInList = true;
					break;
				}
			}
			
			if (!isNameInList) { 
				listFileInfo.add(fileInfo);
			}
			else {
				System.out.println("Find already in list");
			}
			
			System.out.println("Connect to peer server");
			int size = in.readInt();	//	 number of peers have file
			System.out.println(size + "size");
			for (FileInfoPeer fi : listFileInfo) {
				if (fi.fileName.equals(fileNameToDownload)) {
					for (int i = 0; i < size; i++) {
						Socket peerSocket = new Socket(in.readUTF(), in.readInt());
						Runnable r = new FileSharingHandler(fileInfo, peerSocket, FileSharingHandler.Download);
						Thread t = new Thread(r);
						t.start();
					}
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
