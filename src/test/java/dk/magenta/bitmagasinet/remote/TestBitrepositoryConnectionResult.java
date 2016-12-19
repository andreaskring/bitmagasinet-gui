package dk.magenta.bitmagasinet.remote;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestBitrepositoryConnectionResult {

	private final String checksum1 = "9e5aae9572765f8bec9bca8c818188da";
	private final String checksum2 = "0ace08766cafa051844a7b5b15b675c9";

	@Test
	public void shouldReturnChecksum1() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl("file1.bin", checksum1);
		assertEquals(checksum1, result.getChecksum());
	}
	
	@Test
	public void shouldReturnChecksum2() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl("file1.bin", checksum2);
		assertEquals(checksum2, result.getChecksum());
	}
	
	@Test
	public void shouldReturnFilename1() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl("file1.bin", checksum1);
		assertEquals("file1.bin", result.getFilename());
	}

	@Test
	public void shouldReturnFilename2() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl("file2.bin", checksum1);
		assertEquals("file2.bin", result.getFilename());
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowRuntimeExceptionIfFilenameNull() {
		new BitrepositoryConnectionResultImpl(null, checksum1);
	}
}
