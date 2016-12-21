package dk.magenta.bitmagasinet;

import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;

public interface Controller {
	
	public void processNext(BitrepositoryConnector bitrepositoryConnector);
	public List<FileChecksum> getRemainingFileChecksums();
	public List<FileChecksum> getProcessedFileChecksums();
	// public void addFileChecksumToRemainingList(FileChecksum fileChecksum);
	public void addFileChecksumsToRemainingList(List<FileChecksum> fileChecksums);
	// public void addFileCheckSumToProcessedList(FileChecksum fileChecksum);
	public void getCheckSumsFromFile();
	public void abortCurrentRepositoryProcess();
	public void abortAllRepositoryProcesses();

}
