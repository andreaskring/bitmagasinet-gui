package dk.magenta.bitmagasinet;

import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandler;

public interface Controller {
	
	public void processNext(BitrepositoryConnector bitrepositoryConnector);
	public List<FileChecksum> getRemainingFileChecksums();
	public List<FileChecksum> getProcessedFileChecksums();
	public BitrepositoryProgressHandler getProgressHandler();
}
