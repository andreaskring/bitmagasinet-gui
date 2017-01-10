package dk.magenta.bitmagasinet.remote;

import org.bitrepository.client.eventhandler.OperationEvent;
import org.bitrepository.commandline.output.OutputHandler;

public class DNAOutputHandler implements OutputHandler {

	public void debug(String debug) {
	}

	public void startupInfo(String info) {
	}

	public void completeEvent(String msg, OperationEvent event) {
	}

	public void warn(String warning) {
	}

	public void error(String error) {
	}

	public void error(String error, Throwable e) {
	}

	public void resultLine(String line) {
		System.out.println("############################ RESULT ##############################");
		System.out.println(line);
	}

	public void resultHeader(String header) {
	}

	public void setVerbosity(boolean verbose) {
	}

}
