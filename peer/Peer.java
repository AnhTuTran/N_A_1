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
	
	boolean connectToServer(Event event) {
		try {
			client = new Socket(ServerInfo.serverName, ServerInfo.serverPort);
			
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
					filesCanDownloadHandler(in);
					break;
				case DownloadFile:
					System.out.println("DownloadFile");
					//filesCanDownloadHandler(in);
					break;
				default:
					System.out.println("Nop");
				}
				return true;
			}				
				
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	
	}
	
	public static void main(String[] args) {
		Peer p1 = new Peer();
		p1.connectToServer(Event.ConnectingToServer);
	}
	
	void connectToSeverAction(DataInputStream in, DataOutputStream out) throws IOException {
		
		out.writeUTF((client.getLocalAddress().toString().substring(1)));
		System.out.println("Sending host ip to server");
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
			
			out.writeUTF((client.getLocalAddress().toString().substring(1)));
			out.writeUTF(fileInfo.fileName);
			out.writeUTF(Integer.toString(fileInfo.numberOfChunks));
			
			System.out.println(fileInfo.fileName);
			System.out.println(fileInfo.numberOfChunks);
			System.out.println("Sending info to server");
			
			
		}
		else {
			System.out.println("Find already in list");
		}
		
	}
	
	void filesCanDownloadHandler(DataInputStream in) throws NumberFormatException, IOException {
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
		
	
}
