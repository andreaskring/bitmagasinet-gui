package dk.magenta.bitmagasinet;

public class ProcessHandlerObserverDummy implements ProcessHandlerObserver {

	private boolean done = false;
	
	@Override
	public void update(ProcessHandler processHandler) {
		done = true;
	}
	
	public boolean getDone() {
		return done;
	}

}
