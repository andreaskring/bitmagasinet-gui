package dk.magenta.bitmagasinet.remote;

class TimeEstimate {

	private int hours;
	private int minutes;
	private int seconds;
	
	public TimeEstimate(int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	int getHours() {
		return hours;
	}
	
	int getMinutes() {
		return minutes;
	}
	
	int getSeconds() {
		return seconds;
	}
	
}
