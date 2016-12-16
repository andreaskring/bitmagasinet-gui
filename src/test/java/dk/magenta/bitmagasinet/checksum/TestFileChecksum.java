package dk.magenta.bitmagasinet.checksum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.bitrepository.common.utils.Base16Utils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestFileChecksum {

	private final String checksum1 = "9e5aae9572765f8bec9bca8c818188da";
	private final String checksum2 = "0ace08766cafa051844a7b5b15b675c9";
	
	private FileChecksum fileChecksum1;
	private FileChecksum fileChecksum2;
	
	@Before
	public void setUp() {
		fileChecksum1 = new FileChecksumImpl(checksum1, "64");
		fileChecksum2 = new FileChecksumImpl(checksum2, "54");
	}
	
	@Test
	public void shouldReturnChecksum1ForLocalChecksumSetInConstructor() {
		assertEquals(checksum1, fileChecksum1.getLocalChecksum());
	}

	@Test
	public void shouldReturnChecksum2ForLocalChecksumSetInConstructor() {
		assertEquals(checksum2, fileChecksum2.getLocalChecksum());
	}

	@Test
	public void shouldReturnChecksum1ForRemoteChecksumSetInConstructor() {
		fileChecksum1.setRemoteChecksum(checksum2);
		assertEquals(checksum2, fileChecksum1.getRemoteChecksum());
	}

	@Test
	public void shouldReturnChecksum2ForRemoteChecksumSetInConstructor() {
		fileChecksum2.setRemoteChecksum(checksum1);
		assertEquals(checksum1, fileChecksum2.getRemoteChecksum());
	}
	
	@Test
	public void shouldSetLocaleChecksum1Correctly() {
		fileChecksum2.setLocalChecksum(checksum1);
		assertEquals(checksum1, fileChecksum2.getLocalChecksum());
	}
	
	@Test
	public void shouldReturnTrueIfChecksumsMatch() {
		fileChecksum1.setRemoteChecksum(checksum1);
		assertTrue(fileChecksum1.checksumsMatch());
	}
	
	@Test
	public void shouldReturnFalseIfChecksumsMatch() {
		fileChecksum1.setRemoteChecksum(checksum2);
		assertFalse(fileChecksum1.checksumsMatch());
	}
	
	@Test
	public void shouldReturnSalt64EncodedWhenSetInConstructor() {
		boolean arraysEqual = Arrays.equals(Base16Utils.encodeBase16("64"), fileChecksum1.getSalt()); 
		assertTrue(arraysEqual);
	}

	@Test
	public void shouldReturnSalt54EncodedWhenSetInConstructor() {
		boolean arraysEqual = Arrays.equals(Base16Utils.encodeBase16("54"), fileChecksum2.getSalt()); 
		assertTrue(arraysEqual);
	}

	@Test
	public void shouldReturnSalt11WhenSetTo11() {
		fileChecksum1.setSalt("11");
		boolean arraysEqual = Arrays.equals(Base16Utils.encodeBase16("11"), fileChecksum1.getSalt());
		assertTrue(arraysEqual);
	}
}
