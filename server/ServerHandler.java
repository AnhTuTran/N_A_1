import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.Request;

import protocol.Event;
import protocol.ServerInfo;

public class ServerHandler implements Runnable{
	Socket socket4Peer;
	List<PeerStatus> peerList;
	List<FileInfoServer> fileInfoList;
	ServerInfo serverInfo;
	
	public ServerHandler(Socket socket, List<PeerStatus> list, List<FileInfoServer> infoList, ServerInfo serverInfo) {
		socket4Peer = socket;
		peerList = list;
		fileInfoList = infoList;
		this.serverInfo = serverInfo;
	}
	
	public void run() {
		try {
			// server port defines in ServerInfo.java
			InputStream inFromPeer = socket4Peer.getInputStream();
			OutputStream outToPeer = socket4Peer.getOutputStream();
			
			DataInputStream in = new DataInputStream(inFromPeer);
			DataOutputStream out = new DataOutputStream(outToPeer);
			
			while (true) {
				
				int i = Integer.parseInt(in.readUTF());
				
				if (i == (Event.ConnectingToServer).ordinal()){
					out.writeUTF(Integer.toString((Event.ConnectingToServer).ordinal() + 1));
					System.out.println("Server ACK");
					connect2ServerHandler connect = new connect2ServerHandler(peerList,in);
					//System.out.println(socket4Peer.g);
				
				}
				else if (i == (Event.DownloadFile).ordinal()){
					out.writeUTF(Integer.toString((Event.DownloadFile).ordinal() + 1));
					System.out.println("DownloadFile ACK");
					
					String fileName = in.readUTF();
					for (FileInfoServer info : fileInfoList) {
						if (info.fileName.equals(fileName)) {
							out.writeUTF(info.fileName);
							out.writeInt(info.maxChunk);
							out.writeInt(info.peersHaveFile.size());
							for (peerPortToSharingFile peerPortInfo : info.peersHaveFile) {
								
								for (int j = 0; j < peerPortInfo.ip.length(); j++) {
									if (peerPortInfo.ip.charAt(j) == ':') {
										out.writeUTF(peerPortInfo.ip.substring(0, j));
										out.writeInt(peerPortInfo.port);
										break;
									}
								}
							
							}
							break;
						}
					}
					
					//System.out.println(in.readUTF());
				}
				else if (i == (Event.ListFilesCanDownload).ordinal()){
					System.out.println("ListFilesCanDownload ACK");
					out.writeUTF(Integer.toString((Event.ListFilesCanDownload).ordinal() + 1));
					
					out.writeUTF(Integer.toString(fileInfoList.size()));
					for (FileInfoServer info : fileInfoList) {
						out.writeUTF(info.fileName);
					}
					//System.out.println(socket4Peer.getRemoteSocketAddress());
				}
				else if (i == (Event.UploadFile).ordinal()){
					System.out.println("UploadFile ACK");
					out.writeUTF(Integer.toString((Event.UploadFile).ordinal() + 1));
					UploadFileInfoServerHandler upLoad = new UploadFileInfoServerHandler(in, out, fileInfoList, serverInfo.getPeerPort());
					//System.out.println(socket4Peer.getRemoteSocketAddress());
				}
				else {
					System.out.println("xxx");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		/*
			finally {
			System.out.println("close connection");
			try {
				socket4Peer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
		
	}
}
