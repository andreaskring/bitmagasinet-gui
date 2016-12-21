package dk.magenta.bitmagasinet.remote;

public class BitrepositoryConnectionResultImpl implements BitrepositoryConnectionResult {
	
	private String checksum;
	private ThreadStatus status;
	
	public BitrepositoryConnectionResultImpl(ThreadStatus status, String checksum) {
		this.status = status;
		this.checksum = checksum;
	}
	
	@Override
	public String getChecksum() {
		return checksum;
	}

	@Override
	public ThreadStatus getStatus() {
		return status;
	}

}
