package dk.magenta.bitmagasinet.remote;

/**
 * A new BitrepositoryConnector is created for each request to the bitrepository
 * @author andreas
 *
 */
public interface BitrepositoryConnector extends Runnable {

	public void addObserver(ThreadStatusObserver observer);
}
