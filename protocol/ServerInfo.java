package protocol;

public class ServerInfo {
	public static final String serverName = "localhost";
	public static final int serverPort = 6666;
	private int i = 0;
	private int peerPort = 6667;
	
	public int getPeerPort() {
		peerPort = i + 6667;
		i = (i + 1) % 1000;
		return peerPort;
	}

}
