package dk.magenta.bitmagasinet.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.magenta.bitmagasinet.process.TimeEstimate;

public class TestTimeEstimate {

	@Test
	public void shouldGet10WhenHoursIs10() {
		TimeEstimate t = new TimeEstimate(10, 0, 0);
		assertEquals(10, t.getHours());
	}
	
	@Test
	public void shouldGet20WhenHoursIs20() {
		TimeEstimate t = new TimeEstimate(20, 0, 0);
		assertEquals(20, t.getHours());
	}

	@Test
	public void shouldGet20WhenMinutesIs20() {
		TimeEstimate t = new TimeEstimate(20, 20, 0);
		assertEquals(20, t.getMinutes());
	}

	@Test
	public void shouldGet30WhenMinutesIs30() {
		TimeEstimate t = new TimeEstimate(20, 30, 0);
		assertEquals(30, t.getMinutes());
	}

	@Test
	public void shouldGet30WhenSecondsIs30() {
		TimeEstimate t = new TimeEstimate(20, 30, 30);
		assertEquals(30, t.getSeconds());
	}

	@Test
	public void shouldGet40WhenSecondsIs40() {
		TimeEstimate t = new TimeEstimate(20, 30, 40);
		assertEquals(40, t.getSeconds());
	}
}
