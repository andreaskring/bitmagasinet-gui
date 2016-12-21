package dk.magenta.bitmagasinet.remote;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;

public class TestBitrepositoryConnectionResult {

	private final FileChecksum fileChecksum1 = new FileChecksumImpl("file1.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
	private final FileChecksum fileChecksum2 = new FileChecksumImpl("file2.bin", "0ace08766cafa051844a7b5b15b675c9", "64");

	
	@Test
	public void shouldReturnChecksum1() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.SUCCESS, fileChecksum1);
		assertEquals(fileChecksum1, result.getFileChecksum());
	}
	
	@Test
	public void shouldReturnChecksum2() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.SUCCESS, fileChecksum2);
		assertEquals(fileChecksum2, result.getFileChecksum());
	}
	
	@Test
	public void shouldReturnSuccessWhenStatusSuccess() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.SUCCESS, fileChecksum1);
		assertEquals(ThreadStatus.SUCCESS, result.getStatus());
	}
	
	@Test
	public void shouldReturnExceptionWhenStatusException() {
		BitrepositoryConnectionResult result = new BitrepositoryConnectionResultImpl(ThreadStatus.ERROR, fileChecksum1);
		assertEquals(ThreadStatus.ERROR, result.getStatus());
	}

}
