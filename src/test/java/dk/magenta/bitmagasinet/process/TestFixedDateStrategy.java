package dk.magenta.bitmagasinet.process;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import dk.magenta.bitmagasinet.process.DateStrategy;

public class TestFixedDateStrategy {
	
	private Calendar calendar;
	
	@Before
	public void setUp() {
		calendar = Calendar.getInstance();
	}
	
	@Test
	public void shouldReturn1879_03_14_120000() {
		calendar.set(1879, Calendar.MARCH, 14, 12, 0, 0);
		Date d = calendar.getTime();
		
		DateStrategy dateStrategy = new FixedDateStrategy(1879, Calendar.MARCH, 14, 12, 0, 0);
		assertEquals(DateUtils.truncate(d, Calendar.SECOND), dateStrategy.getDate());
	}
	
	@Test
	public void shouldReturn1979_05_14_120000() {
		calendar.set(1979, Calendar.MAY, 14, 12, 0, 0);
		Date d = calendar.getTime();

		DateStrategy dateStrategy = new FixedDateStrategy(1979, Calendar.MAY, 14, 12, 0, 0);
		assertEquals(DateUtils.truncate(d, Calendar.SECOND), dateStrategy.getDate());
	}

}
