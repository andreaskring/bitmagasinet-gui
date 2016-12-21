package dk.magenta.bitmagasinet.remote;

public interface ThreadStatusObserver {
	
	public void update(BitrepositoryConnectionResult bitrepositoryConnectionResult);
	
	/**
	 * If this method is called there was an error closing the bitrepository message bus
	 */
	public void messageBusErrorCallback();
	
}
