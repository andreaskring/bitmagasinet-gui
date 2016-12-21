package dk.magenta.bitmagasinet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectorStub;

public class TestControllerImpl {

	private ControllerImpl controller;
	private List<FileChecksum> fileChecksums;
	private FileChecksum fileChecksum1;

	@Before
	public void setUp() {
		controller = new ControllerImpl();
		fileChecksums = new ArrayList<FileChecksum>();
		addFileChecksum1();
	}

	@Test
	public void shouldReturnSizeZeroWhenRemainingFileChecksumListEmpty() {
		assertEquals(0, controller.getRemainingFileChecksums().size());
	}

	@Test
	public void shouldReturnSizeOneWhenRemainingFileChecksumListHasOneElement() {
		controller.addFileChecksumsToRemainingList(fileChecksums);
		assertEquals(1, controller.getRemainingFileChecksums().size());
		assertEquals("file1.bin", controller.getRemainingFileChecksums().get(0).getFilename());
	}

	@Test
	public void shouldReturnSizeTwoWhenRemainingFileChecksumListHasTwoElement() {
		addFileChecksum2();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		assertEquals(2, controller.getRemainingFileChecksums().size());
		assertEquals("file1.bin", controller.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", controller.getRemainingFileChecksums().get(1).getFilename());
	}
	
	@Test
	public void shouldReturnSize0ForProcessedFileChecksumsWhenNoneHaveBeenProcessed() {
		assertEquals(0, controller.getProcessedFileChecksums().size());
	}
	
	@Test
	public void shouldReturnSize1ForProcessedFileChecksumsWhenOneHaveBeenProcessed() throws InterruptedException {
		addFileChecksum2();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		
		// Two FileChecksums are now in the remaining list
		
		BitrepositoryConnector bitrepositoryConnector = new BitrepositoryConnectorStub(fileChecksum1);
		bitrepositoryConnector.addObserver(controller);

		controller.processNext(bitrepositoryConnector);
		Thread.sleep(1000); // To make sure that the separate thread will finish
		
		assertEquals(1, controller.getProcessedFileChecksums().size());
		assertEquals("file1.bin", controller.getProcessedFileChecksums().get(0).getFilename());
	}
	
	@Test
	public void whenFirstFileChecksumIsProcessedItShouldBeRemovedFromTheRemainingList() throws InterruptedException {
		addFileChecksum2();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		
		// Two FileChecksums are now in the remaining list
		
		BitrepositoryConnector bitrepositoryConnector = new BitrepositoryConnectorStub(fileChecksum1);
		bitrepositoryConnector.addObserver(controller);

		controller.processNext(bitrepositoryConnector);
		Thread.sleep(1000); // To make sure that the separate thread will finish

		assertEquals(1, controller.getRemainingFileChecksums().size());
		assertEquals("file2.bin", controller.getRemainingFileChecksums().get(0).getFilename());

	}
	
	private void addFileChecksum1() {
		fileChecksum1 = new FileChecksumImpl("file1.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum1.setRemoteChecksum("9e5aae9572765f8bec9bca8c818188da");
		fileChecksums.add(fileChecksum1);
	}
	
	private void addFileChecksum2() {
		FileChecksum fileChecksum2 = new FileChecksumImpl("file2.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum2.setRemoteChecksum("00ffae9572765f8bec9bca8c81812211");
		fileChecksums.add(fileChecksum2);
	}
	
	// when processed one should be removed form the other list
	// should handle ERROR response
	// should handle INTERRUPTED response
	// should handled when two file are processed
	// show hold BitrepositoProgressHandler

}
