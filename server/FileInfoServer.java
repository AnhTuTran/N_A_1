import java.util.ArrayList;
import java.util.List;

public class FileInfoServer {
	String fileName;
	int maxChunk;
	List<String> peersHaveFile = new ArrayList<String>();
	
	public FileInfoServer(String name, int MaxChunk, String ip) {
		fileName = name;
		maxChunk = MaxChunk;
		boolean isInList = false;
		
		for (String str : peersHaveFile) {
			if (str.equals(ip)) {
				isInList = false;
				break;
			}
		}
		
		if (!isInList) {
			peersHaveFile.add(ip);
		}
	}
}
