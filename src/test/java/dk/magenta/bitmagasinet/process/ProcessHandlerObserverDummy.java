package dk.magenta.bitmagasinet.process;

import dk.magenta.bitmagasinet.process.ProcessHandler;
import dk.magenta.bitmagasinet.process.ProcessHandlerObserver;

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
