package dk.magenta.bitmagasinet.checksum;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestChecksumFileValidator {

	@Test
	public void shouldReturnTrueIfLineIsCorrectlyFormated() {
		String line = "file1.bin\t64\t0f2dd3c5a366a1e860e2f8a1afc29987";
		assertTrue(ChecksumFileValidator.isLineValid(line));
	}

	@Test
	public void shouldReturnTrueIfLineIsCorrectlyFormated2() {
		String line = "file1.bin\t645\t0f2dd3c5a366a1e860e2f8a1afc29987";
		assertFalse(ChecksumFileValidator.isLineValid(line));
	}

	@Test
	public void shouldReturnTrueIfLineIsCorrectlyFormated3() {
		String line = "file1.bin\tff\t0f2dd3c5a366a1e860e2f8a1afc29987";
		assertTrue(ChecksumFileValidator.isLineValid(line));
	}

	@Test
	public void shouldReturnTrueIfLineIsCorrectlyFormated4() {
		String line = "file1.bin\t64\t0f2dd3c5a366a1e860e2f8a1afc29987";
		assertTrue(ChecksumFileValidator.isLineValid(line));
	}

	
	@Test
	public void shouldReturnFalseIfLineContainsLessThanThreeElements() {
		String line = "45\t0f2dd3c5a366a1e860e2f8a1afc29987";
		assertFalse(ChecksumFileValidator.isLineValid(line));
	}
	
	@Test
	public void filenameCannotBeBlank() {
		String line = "\t45\t0f2dd3c5a366a1e860e2f8a1afc29987";
		assertFalse(ChecksumFileValidator.isLineValid(line));
	}

	@Test
	public void shouldReturnFalseIfMd5sumNot32Long() {
		String line = "file1.bin\tff\t0f2dd3c5a366a1e860e28a1afc29987";
		assertFalse(ChecksumFileValidator.isLineValid(line));
	}

	@Test
	public void shouldReturnFalseIfMd5sumNot32Long2() {
		String line = "file1.bin\tff\t0f2dd3c5a366a1e860e28a1afc29987  ";
		assertFalse(ChecksumFileValidator.isLineValid(line));
	}

	//TODO: must check filename
}
