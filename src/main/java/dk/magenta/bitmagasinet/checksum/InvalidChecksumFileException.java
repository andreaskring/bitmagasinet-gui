package dk.magenta.bitmagasinet.checksum;

public class InvalidChecksumFileException extends Exception {

	public InvalidChecksumFileException() {
		super();
	};
	
	public InvalidChecksumFileException(String message) {
		super(message);
	}
	
}
