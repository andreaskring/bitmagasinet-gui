package dk.magenta.bitmagasinet.remote;

public class BitrepositoryConnectionResultImpl implements BitrepositoryConnectionResult {
	
	private String checksum;
	private String filename;
	
	/**
	 * @param filename
	 */
	public BitrepositoryConnectionResultImpl(String filename, String checksum) throws RuntimeException {
		if (filename == null) {
			throw new RuntimeException();
		}
		this.filename = filename;
		this.checksum = checksum;
	}
	
	@Override
	public String getChecksum() {
		return checksum;
	}

	@Override
	public String getFilename() {
		return filename;
	}

}
