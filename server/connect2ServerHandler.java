import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class connect2ServerHandler {
	connect2ServerHandler (List<PeerStatus> list, DataInputStream in) throws IOException {
		boolean isInList = false;
		String ip = in.readUTF();
		for (PeerStatus p : list) {
			if (ip.equals(p.peerIP)) {
				isInList = true;
				break;
			}
		}
		
		if (!isInList) {
			list.add(new PeerStatus(ip));
			System.out.println(ip);
		}
		else {
			System.out.println("Connected");
		}
	}
}
