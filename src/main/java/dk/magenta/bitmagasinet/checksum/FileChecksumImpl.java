package dk.magenta.bitmagasinet.checksum;

import org.bitrepository.common.utils.Base16Utils;

public class FileChecksumImpl implements FileChecksum {

	private String filename;
	private String localChecksum;
	private String remoteChecksum;
	private byte[] salt;
	
	public FileChecksumImpl(String filename, String localChecksum, String salt) {
		this.filename = filename;
		setLocalChecksum(localChecksum);
		setSalt(salt);
	}
	
	@Override
	public boolean checksumsMatch() {
		return localChecksum.equals(remoteChecksum);
	}

	@Override
	public String getFilename() {
		return filename;
	}
	
	@Override
	public String getLocalChecksum() {
		return localChecksum;
	}

	@Override
	public String getRemoteChecksum() {
		return remoteChecksum;
	}

	@Override
	public byte[] getSalt() {
		return salt;
	}

	@Override
	public void setLocalChecksum(String checksum) {
		localChecksum = checksum;
	}

	@Override
	public void setRemoteChecksum(String checksum) {
		remoteChecksum = checksum;
	}

	@Override
	public void setSalt(String salt) {
		this.salt = Base16Utils.encodeBase16(salt);
	}

}
