package dk.magenta.bitmagasinet.remote;

public interface BitrepositoryConnector extends Runnable {

	public void addObserver(ThreadStatusObserver observer);
}
