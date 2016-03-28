import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenToPeerHandler implements Runnable{
	int peerPort;
	FileInfoPeer fileInfo;
	public ListenToPeerHandler(FileInfoPeer fileInfo, int port) {
		peerPort = port;
		this.fileInfo = fileInfo;
		System.out.println("ListenToPeerHandler");
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(peerPort);
			
			while (true) {
				System.out.println("Peer server on");
				Socket peerSocket = serverSocket.accept();
				Runnable r = new FileSharingHandler(fileInfo, peerSocket, FileSharingHandler.Upload);
				Thread t = new Thread(r);
				t.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
