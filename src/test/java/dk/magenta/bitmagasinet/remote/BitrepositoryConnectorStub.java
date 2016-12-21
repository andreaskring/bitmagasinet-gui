package dk.magenta.bitmagasinet.remote;

import java.util.ArrayList;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class BitrepositoryConnectorStub implements BitrepositoryConnector {

	private List<ThreadStatusObserver> observers;
	private FileChecksum returnFileChecksum;

	public BitrepositoryConnectorStub(FileChecksum returnFileChecksum) {
		this.returnFileChecksum = returnFileChecksum;
		observers = new ArrayList<ThreadStatusObserver>();
	}

	@Override
	public void run() {
		BitrepositoryConnectionResult bitrepositoryConnectionResult = new BitrepositoryConnectionResultImpl(
				ThreadStatus.SUCCESS, returnFileChecksum);
		notifyObservers(bitrepositoryConnectionResult);
	}

	@Override
	public void addObserver(ThreadStatusObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		for (ThreadStatusObserver observer : observers) {
			observer.update(bitrepositoryConnectionResult);
		}
	}

}
