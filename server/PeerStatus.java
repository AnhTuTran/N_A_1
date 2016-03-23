public class PeerStatus {
	String peerIP;
	boolean status = false;
	
	public PeerStatus(String ip) {
		peerIP = ip;
		status = true;
	}
	
	void peerOnline() {
		status = true;
	}
	void peerOffline() {
		peerIP = null;
		status = false;
	}
		
}
