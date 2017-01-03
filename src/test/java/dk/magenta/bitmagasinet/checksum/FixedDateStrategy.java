package dk.magenta.bitmagasinet.checksum;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class FixedDateStrategy implements DateStrategy {

	private Date date;
	
	public FixedDateStrategy(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, minute, second);
		date = calendar.getTime();
	}
	
	@Override
	public Date getDate() {
		return DateUtils.truncate(date, Calendar.SECOND);
	}

}
