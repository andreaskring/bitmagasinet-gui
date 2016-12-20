package dk.magenta.bitmagasinet.remote;

import java.util.Date;

public interface BitrepositoryProgressHandler {

	public int getProgress();
	public Date getEstimatedTimeLeft();
	public void fileCompleted();
}
