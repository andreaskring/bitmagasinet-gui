package dk.magenta.bitmagasinet.remote;

import java.util.ArrayList;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class BitrepositoryConnectorStub implements BitrepositoryConnector {

	private List<ThreadStatusObserver> observers;
	private FileChecksum returnFileChecksum;
	private ThreadStatus threadStatus;

	public BitrepositoryConnectorStub(FileChecksum returnFileChecksum, ThreadStatus threadStatus) {
		this.returnFileChecksum = returnFileChecksum;
		this.threadStatus = threadStatus;
		observers = new ArrayList<ThreadStatusObserver>();
	}

	@Override
	public void setFileChecksum(FileChecksum fileChecksum) {
		returnFileChecksum = fileChecksum;
	}
	
	@Override
	public void run() {
		BitrepositoryConnectionResult bitrepositoryConnectionResult = new BitrepositoryConnectionResultImpl(
				threadStatus, returnFileChecksum);
		notifyObservers(bitrepositoryConnectionResult);
	}

	@Override
	public void addObserver(ThreadStatusObserver observer) {
		observers.add(observer);
	}

	protected void notifyObservers(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		for (ThreadStatusObserver observer : observers) {
			observer.update(bitrepositoryConnectionResult);
		}
	}
	
	public void setThreadStatus(ThreadStatus threadStatus) {
		this.threadStatus = threadStatus;
	}

	protected FileChecksum getReturnFileChecksum() {
		return returnFileChecksum;
	}
	
	protected ThreadStatus getThreadStatus() {
		return threadStatus;
	}
}
