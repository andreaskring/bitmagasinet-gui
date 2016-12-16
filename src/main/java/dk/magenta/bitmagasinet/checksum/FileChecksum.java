package dk.magenta.bitmagasinet.checksum;

public interface FileChecksum {

	/**
	 * Pre-condition for all methods: checksums and salt are valid 
	 **/
	
	public boolean checksumsMatch();
	public String getLocalChecksum();
	public String getRemoteChecksum();
	public byte[] getSalt(); 
	public void setLocalChecksum(String checksum);
	public void setRemoteChecksum(String checksum);
	public void setSalt(String salt);
	
}
