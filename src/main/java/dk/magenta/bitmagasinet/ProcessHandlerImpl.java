package dk.magenta.bitmagasinet;

import java.util.ArrayList;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectionResult;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandler;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandlerImpl;
import dk.magenta.bitmagasinet.remote.ThreadStatus;
import dk.magenta.bitmagasinet.remote.ThreadStatusObserver;

public class ProcessHandlerImpl implements ProcessHandler, ThreadStatusObserver {

	private List<FileChecksum> remainingFileChecksums;
	private List<FileChecksum> processedFileChecksums;
	private BitrepositoryProgressHandler bitrepositoryProgressHandler;

	public ProcessHandlerImpl(List<FileChecksum> fileChecksums) {
		remainingFileChecksums = fileChecksums;
		processedFileChecksums = new ArrayList<FileChecksum>();
		bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
	}

	@Override
	public void processNext(BitrepositoryConnector bitrepositoryConnector) {
		Thread t = new Thread(bitrepositoryConnector);
		t.start();
	}

	@Override
	public List<FileChecksum> getRemainingFileChecksums() {
		return remainingFileChecksums;
	}

	@Override
	public List<FileChecksum> getProcessedFileChecksums() {
		return processedFileChecksums;
	}

	@Override
	public void update(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		remainingFileChecksums.remove(0);
		if (bitrepositoryConnectionResult.getStatus() == ThreadStatus.SUCCESS) {
			processedFileChecksums.add(bitrepositoryConnectionResult.getFileChecksum());
			bitrepositoryProgressHandler.fileCompleted();
		} else {
			remainingFileChecksums.add(bitrepositoryConnectionResult.getFileChecksum());
		}
	}

	@Override
	public BitrepositoryProgressHandler getProgressHandler() {
		return bitrepositoryProgressHandler;
	}
	
	@Override
	public void messageBusErrorCallback() {
		// TODO Auto-generated method stub
	}

}