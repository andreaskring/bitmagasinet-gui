package dk.magenta.bitmagasinet.remote;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public interface BitrepositoryConnectionResult {

	public FileChecksum getFileChecksum();
	public ThreadStatus getStatus();
	
}
