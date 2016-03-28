import java.util.ArrayList;
import java.util.List;

public class FileInfoServer {
	String fileName;
	int maxChunk;
	List<peerPortToSharingFile> peersHaveFile = new ArrayList<peerPortToSharingFile>();
	
	public FileInfoServer(String name, int MaxChunk, peerPortToSharingFile peerInfo) {
		fileName = name;
		maxChunk = MaxChunk;
		boolean isInList = false;
		
		for (peerPortToSharingFile p : peersHaveFile) {
			if (p.ip.equals(peerInfo.ip)) {
				isInList = false;
				break;
			}
		}
		
		if (!isInList) {
			peersHaveFile.add(peerInfo);
		}
	}
}
