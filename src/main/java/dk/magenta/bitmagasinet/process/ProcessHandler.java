package dk.magenta.bitmagasinet.process;

import java.util.Date;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public interface ProcessHandler {
	
	public void processNext();
	public List<FileChecksum> getRemainingFileChecksums();
	public List<FileChecksum> getProcessedFileChecksums();
	public BitrepositoryProgressHandler getProgressHandler();
	public void addObserver(ProcessHandlerObserver processHandlerObserver);
	public void notifyObservers();
	public Date getStartDate();
	public Date getEndDate();
}
