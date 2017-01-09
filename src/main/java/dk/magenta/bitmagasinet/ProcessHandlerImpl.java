package dk.magenta.bitmagasinet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.DateStrategy;
import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectionResult;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandler;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandlerImpl;
import dk.magenta.bitmagasinet.remote.ThreadStatusObserver;

public class ProcessHandlerImpl implements ProcessHandler, ThreadStatusObserver {

	private List<FileChecksum> remainingFileChecksums;
	private List<FileChecksum> processedFileChecksums;
	private List<ProcessHandlerObserver> observers;
	private BitrepositoryProgressHandler bitrepositoryProgressHandler;
	private BitrepositoryConnector bitrepositoryConnector;
	private DateStrategy dateStrategy;
	private boolean processAutomatically;
	private Date startDate;
	private Date endDate;

	public ProcessHandlerImpl(List<FileChecksum> fileChecksums, BitrepositoryConnector bitrepositoryConnector, DateStrategy dateStrategy,
			boolean processAutomatically) {
		this.bitrepositoryConnector = bitrepositoryConnector;
		this.dateStrategy = dateStrategy;
		this.processAutomatically = processAutomatically;
		remainingFileChecksums = fileChecksums;
		processedFileChecksums = new ArrayList<FileChecksum>();
		bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
		observers = new ArrayList<ProcessHandlerObserver>();
		startDate = dateStrategy.getDate();
	}

	@Override
	public void processNext() {
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
		processedFileChecksums.add(bitrepositoryConnectionResult.getFileChecksum());
		bitrepositoryProgressHandler.fileCompleted();
		if (!remainingFileChecksums.isEmpty()) {
			bitrepositoryConnector.setFileChecksum(remainingFileChecksums.get(0));
			if (processAutomatically) {
				processNext();
			}
		} else {
			endDate = dateStrategy.getDate();
			notifyObservers();
		}
	}

	@Override
	public BitrepositoryProgressHandler getProgressHandler() {
		return bitrepositoryProgressHandler;
	}

	@Override
	public void messageBusErrorCallback() {
		// Handled in the GUI
	}

	@Override
	public void addObserver(ProcessHandlerObserver processHandlerObserver) {
		observers.add(processHandlerObserver);
	}

	@Override
	public void notifyObservers() {
		if (!observers.isEmpty()) {
			for (ProcessHandlerObserver observer : observers) {
				observer.update(this);
			}
		}
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}
	
	@Override
	public Date getEndDate() {
		return endDate;
	}
	
}
