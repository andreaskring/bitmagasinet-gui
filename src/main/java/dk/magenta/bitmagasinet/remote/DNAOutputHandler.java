package dk.magenta.bitmagasinet.remote;

import org.bitrepository.client.eventhandler.OperationEvent;
import org.bitrepository.commandline.output.OutputHandler;

public class DNAOutputHandler implements OutputHandler {

	public void debug(String debug) {
		// TODO Auto-generated method stub

	}

	public void startupInfo(String info) {
		// TODO Auto-generated method stub

	}

	public void completeEvent(String msg, OperationEvent event) {
		// TODO Auto-generated method stub

	}

	public void warn(String warning) {
		// TODO Auto-generated method stub

	}

	public void error(String error) {
		// TODO Auto-generated method stub

	}

	public void error(String error, Throwable e) {
		// TODO Auto-generated method stub

	}

	public void resultLine(String line) {
		System.out.println("############################ RESULT ##############################");
		System.out.println(line);
	}

	public void resultHeader(String header) {
		// TODO Auto-generated method stub

	}

	public void setVerbosity(boolean verbose) {
		// TODO Auto-generated method stub

	}

}
