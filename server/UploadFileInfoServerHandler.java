import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import protocol.Event;

public class UploadFileInfoServerHandler {
	public UploadFileInfoServerHandler(DataInputStream in, DataOutputStream out, List<FileInfoServer> fileInfoList, int port) throws IOException {
		String ip = in.readUTF();
		String fileName = in.readUTF();
		int maxChunk = Integer.parseInt(in.readUTF());
		peerPortToSharingFile peerInfo = new peerPortToSharingFile(ip, port);
		
		boolean isInList = false;
		
		for (FileInfoServer info : fileInfoList) {
			if (fileName.equals(info.fileName)) {
				isInList = true;
				break;
			}
		}
		
		if (!isInList) {
			fileInfoList.add(new FileInfoServer(fileName, maxChunk, peerInfo));
			System.out.println(fileName);
			System.out.println(maxChunk);
			System.out.println(peerInfo.ip + " " + peerInfo.port);
		}
		//out.writeUTF(Integer.toString(peerInfo.port));
		out.writeInt(peerInfo.port);
	}
}
