import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class UploadFileServerHandler {
	public UploadFileServerHandler(DataInputStream in, List<FileInfoServer> fileInfoList) throws IOException {
		String ip = in.readUTF();
		String fileName = in.readUTF();
		int maxChunk = Integer.parseInt(in.readUTF());
		
		boolean isInList = false;
		
		for (FileInfoServer info : fileInfoList) {
			if (fileName.equals(info.fileName)) {
				isInList = true;
				break;
			}
		}
		
		if (!isInList) {
			fileInfoList.add(new FileInfoServer(fileName, maxChunk, ip));
			System.out.println(fileName);
			System.out.println(maxChunk);
			System.out.println(ip);
		}
		
				
	}
}
