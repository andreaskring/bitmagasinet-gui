package dk.magenta.bitmagasinet;

import java.util.Date;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandler;

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
