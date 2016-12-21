package dk.magenta.bitmagasinet.remote;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestBitrepositoryConnectionResult {

	private final String checksum1 = "9e5aae9572765f8bec9bca8c818188da";
	private final String checksum2 = "0ace08766cafa051844a7b5b15b675c9";

	@Test
	public void shouldReturnChecksum1() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.SUCCESS, checksum1);
		assertEquals(checksum1, result.getChecksum());
	}
	
	@Test
	public void shouldReturnChecksum2() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.SUCCESS, checksum2);
		assertEquals(checksum2, result.getChecksum());
	}
	
	@Test
	public void shouldReturnSuccessWhenStatusSuccess() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.SUCCESS, checksum1);
		assertEquals(ThreadStatus.SUCCESS, result.getStatus());
	}
	
	@Test
	public void shouldReturnExceptionWhenStatusException() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.ERROR, checksum1);
		assertEquals(ThreadStatus.ERROR, result.getStatus());
	}

}
