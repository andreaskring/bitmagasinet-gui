package dk.magenta.bitmagasinet.checksum;

public class InvalidChecksumFileException extends Exception {

	private static final long serialVersionUID = -7976474452362720255L;

	public InvalidChecksumFileException() {
		super();
	};
	
	public InvalidChecksumFileException(String message) {
		super(message);
	}
	
}
