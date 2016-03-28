import java.io.*;
import java.net.Socket;

import protocol.Event;

public class FileSharingHandler implements Runnable{
	Socket peerSocket;
	FileInfoPeer fileInfo;
	public final static boolean Upload = true;
	public final static boolean Download = false;
	boolean upOrDown;	// upload = true, download = false
	
	public FileSharingHandler(FileInfoPeer fileInfo, Socket socket, Boolean upOrDown) {
		peerSocket = socket;
		this.fileInfo = fileInfo;
		this.upOrDown = upOrDown;
	}
	
	public void run() {
		try {
			InputStream inputStream = peerSocket.getInputStream();
			OutputStream outputStream = peerSocket.getOutputStream();
			
			DataInputStream in = new DataInputStream(inputStream);
			DataOutputStream out = new DataOutputStream(outputStream);
			
			int times = 0;
			byte [] buffer = new byte[FileInfoPeer.maxFileSize];
			
			while (true) {
				if (upOrDown) {	
					String request = in.readUTF();
					if (request.equals(Event.WantChunkTh.toString())) {
						int chunkTh = in.readInt(); 
						int size = fileInfo.splitChunkThFile(buffer, chunkTh);
						
						if (size == -1) {
							System.out.println("FileSharingHandler line 32");
						}
						else {
							//out.write(buffer);
							//String Size = Integer.toString(size);
							//out.writeUTF(Size);
							out.writeInt(size);
							out.write(buffer);
							System.out.println("Size " + size); 
						}
						
						if (times++ == 3) {
							times = 0;
							//out.writeUTF(Event.WantChunkTh.toString());
							System.out.println("Serverpeer want smt");
						}
					}
					else {
						System.out.println(Event.EndSharingFile.toString());
						break;
					}
				}
				else {
					int chunkTh = -1;
					for (int i = 0; i < fileInfo.numberOfChunks; i++) {
						if (!fileInfo.chunkStatus[i]) {
							chunkTh = i+1;
							fileInfo.chunkStatus[i] = true;
							break;
						}
					}
					if (chunkTh == -1) {
						out.writeUTF(Event.EndSharingFile.toString());
						fileInfo.mergeChunks(new File(fileInfo.path, fileInfo.fileName));
						System.out.println("Download completed");
						break;
					}
					else { 
						out.writeUTF(Event.WantChunkTh.toString());
						out.writeInt(chunkTh);
						
						//in.read(buffer);
						//String size = in.readUTF();
						int size = in.readInt();
						in.readFully(buffer);
						System.out.println("Size " + size );
						
						fileInfo.chunkStatus[chunkTh-1] = true;
						fileInfo.makeChunkTh(chunkTh, buffer, size);
						
						if (times++ == 3) {
							times = 0;
							System.out.println("Sending smt to serverpeer");
						}
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				peerSocket.close();
				System.out.println("Serverpeer closes");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
