package dk.magenta.bitmagasinet.process;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;
import dk.magenta.bitmagasinet.process.BitrepositoryProgressHandler;
import dk.magenta.bitmagasinet.process.BitrepositoryProgressHandlerImpl;

public class TestBitrepositoryProgressHandler {

	private List<FileChecksum> fileChecksums;
	
	@Before
	public void setUp() {
		fileChecksums = new ArrayList<FileChecksum>();
	}
	
	@Test
	public void progressShouldBe0WhenNoFilesAreDone() {
		fileChecksums.add(new FileChecksumImpl("file1.bin", null, "64"));
		BitrepositoryProgressHandler bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
		assertEquals(0, bitrepositoryProgressHandler.getProgress());
	}
	
	@Test
	public void progressShouldBe100WhenOneFilesIsAddedAndDone() {
		fileChecksums.add(new FileChecksumImpl("file1.bin", null, "64"));
		BitrepositoryProgressHandler bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
		bitrepositoryProgressHandler.fileCompleted();
		assertEquals(100, bitrepositoryProgressHandler.getProgress());
	}

	@Test
	public void progressShouldBe50WhenTwoFilesAreAddedAnd1IsDone() {
		fileChecksums.add(new FileChecksumImpl("file1.bin", null, "64"));
		fileChecksums.add(new FileChecksumImpl("file2.bin", null, "64"));
		BitrepositoryProgressHandler bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
		assertEquals(0, bitrepositoryProgressHandler.getProgress());
		bitrepositoryProgressHandler.fileCompleted();
		assertEquals(50, bitrepositoryProgressHandler.getProgress());
	}

	@Test
	public void progressShouldBe66WhenThreeFilesAreAddedAnd2IsDone() {
		fileChecksums.add(new FileChecksumImpl("file1.bin", null, "64"));
		fileChecksums.add(new FileChecksumImpl("file2.bin", null, "64"));
		fileChecksums.add(new FileChecksumImpl("file3.bin", null, "64"));
		BitrepositoryProgressHandler bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
		assertEquals(0, bitrepositoryProgressHandler.getProgress());
		bitrepositoryProgressHandler.fileCompleted();
		assertEquals(33, bitrepositoryProgressHandler.getProgress());
		bitrepositoryProgressHandler.fileCompleted();
		assertEquals(66, bitrepositoryProgressHandler.getProgress());
	}

	@Test
	public void progressShouldBe100WhenThreeFilesAreAddedAnd3IsDone() {
		fileChecksums.add(new FileChecksumImpl("file1.bin", null, "64"));
		fileChecksums.add(new FileChecksumImpl("file2.bin", null, "64"));
		fileChecksums.add(new FileChecksumImpl("file3.bin", null, "64"));
		BitrepositoryProgressHandler bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
		assertEquals(0, bitrepositoryProgressHandler.getProgress());
		bitrepositoryProgressHandler.fileCompleted();
		bitrepositoryProgressHandler.fileCompleted();
		bitrepositoryProgressHandler.fileCompleted();
		assertEquals(100, bitrepositoryProgressHandler.getProgress());
	}

	
}
