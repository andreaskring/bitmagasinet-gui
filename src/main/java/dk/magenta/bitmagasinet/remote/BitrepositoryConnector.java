package dk.magenta.bitmagasinet.remote;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

/**
 * A new BitrepositoryConnector is created for each request to the bitrepository
 * @author andreas
 *
 */
public interface BitrepositoryConnector extends Runnable {

	public void addObserver(ThreadStatusObserver observer);
	public void setFileChecksum(FileChecksum fileChecksum);
}
