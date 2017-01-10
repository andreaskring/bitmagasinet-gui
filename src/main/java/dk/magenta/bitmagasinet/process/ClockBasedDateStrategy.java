package dk.magenta.bitmagasinet.process;

import java.util.Date;

public class ClockBasedDateStrategy implements DateStrategy {

	@Override
	public Date getDate() {
		return new Date();
	}

}
