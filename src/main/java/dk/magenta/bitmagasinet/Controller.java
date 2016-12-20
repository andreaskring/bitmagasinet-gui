package dk.magenta.bitmagasinet;

import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public interface Controller {
	
	public void process();
	public List<FileChecksum> getRemainingFileChecksums();
	public List<FileChecksum> getProcessedFileChecksums();
	// public void addFileChecksumToRemainingList(FileChecksum fileChecksum);
	public void addFileChecksumsToRemainingList(List<FileChecksum> fileChecksums);
	public void addFileCheckSumToProcessedList(FileChecksum fileChecksum);
	public void getCheckSumsFromFile();
	public void abortCurrentRepositoryProcess();
	public void abortAllRepositoryProcesses();
	
	public void update();
	
	
}
