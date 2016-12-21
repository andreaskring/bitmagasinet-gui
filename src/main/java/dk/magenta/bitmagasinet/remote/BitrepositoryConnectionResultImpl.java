package dk.magenta.bitmagasinet.remote;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class BitrepositoryConnectionResultImpl implements BitrepositoryConnectionResult {
	
	private FileChecksum fileChecksum;
	private ThreadStatus status;
	
	public BitrepositoryConnectionResultImpl(ThreadStatus status, FileChecksum checksum) {
		this.status = status;
		this.fileChecksum = checksum;
	}
	
	@Override
	public FileChecksum getFileChecksum() {
		return fileChecksum;
	}

	@Override
	public ThreadStatus getStatus() {
		return status;
	}

}
