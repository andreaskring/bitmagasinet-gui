package dk.magenta.bitmagasinet.checksum;

import java.util.Date;

public class ClockBasedDateStrategy implements DateStrategy {

	@Override
	public Date getDate() {
		return new Date();
	}

}
