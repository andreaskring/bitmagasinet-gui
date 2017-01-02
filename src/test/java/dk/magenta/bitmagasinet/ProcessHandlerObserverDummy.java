package dk.magenta.bitmagasinet;

public class ProcessHandlerObserverDummy implements ProcessHandlerObserver {

	private boolean done = false;
	private ProcessHandler processHandler;
	
	@Override
	public void update(ProcessHandler processHandler) {
		done = true;
		this.processHandler = processHandler;
	}
	
	public boolean getDone() {
		return done;
	}
	
	public ProcessHandler getProcessHandler() {
		return processHandler;
	}

}
