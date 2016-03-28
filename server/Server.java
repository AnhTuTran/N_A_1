import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import protocol.*;

public class Server {	
	public static void main(String[] args) {		
		try {
			ServerSocket serverSocket = new ServerSocket(ServerInfo.serverPort);
			List<PeerStatus> peerList = new ArrayList<PeerStatus>();				// synchronize
			List<FileInfoServer> fileInfoList = new ArrayList<FileInfoServer>();	// synchronize
			ServerInfo serverInfo = new ServerInfo();								// synchronize
			while (true) {
				Socket socket4Peer = serverSocket.accept();
				Runnable r = new ServerHandler(socket4Peer, peerList, fileInfoList, serverInfo);
				Thread t = new Thread(r);
				t.start();
				//System.out.println("end");
				//t.join();
				
			//	System.out.println(peerList.get(0).peerIP);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		
	}
}
