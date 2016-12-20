package dk.magenta.bitmagasinet.remote;

import java.util.Date;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class BitrepositoryProgressHandlerImpl implements BitrepositoryProgressHandler {

	private int totalNumberOfFiles;
	private int filesCompleted;
	
	public BitrepositoryProgressHandlerImpl(List<FileChecksum> fileChecksums) {
		filesCompleted = 0;
		totalNumberOfFiles = fileChecksums.size();
	}
	
	@Override
	public void fileCompleted() {
		filesCompleted += 1;
	}
	
	@Override
	public Date getEstimatedTimeLeft() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getProgress() {
		double percentage = ((double) filesCompleted)/((double) totalNumberOfFiles); 
		return (int) (100*percentage);
	}
	
}
