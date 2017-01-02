package dk.magenta.bitmagasinet;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectorStub;
import dk.magenta.bitmagasinet.remote.ThreadStatus;

public class TestProcessHandlerImpl {

	private ProcessHandlerImpl processHandler;
	private List<FileChecksum> fileChecksums;
	private FileChecksum fileChecksum1;
	private FileChecksum fileChecksum2;
	private FileChecksum fileChecksum3;
	private BitrepositoryConnector bitrepositoryConnector;

	@Before
	public void setUp() {
		fileChecksums = new ArrayList<FileChecksum>();
	}

	@Test
	public void shouldReturnSizeZeroWhenRemainingFileChecksumListEmpty() {
		List<FileChecksum> fileChecksums = new ArrayList<FileChecksum>();
		ProcessHandler controller = new ProcessHandlerImpl(fileChecksums, null, false);
		assertEquals(0, controller.getRemainingFileChecksums().size());
	}

	@Test
	public void shouldReturnSizeOneWhenRemainingFileChecksumListHasOneElement() {
		addFileChecksum1();
		processHandler = new ProcessHandlerImpl(fileChecksums, null, false);
		assertEquals(1, processHandler.getRemainingFileChecksums().size());
		assertEquals("file1.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());
	}

	@Test
	public void shouldReturnSizeTwoWhenRemainingFileChecksumListHasTwoElement() {
		addFileChecksum1();
		addFileChecksum2();
		processHandler = new ProcessHandlerImpl(fileChecksums, null, false);

		assertEquals(2, processHandler.getRemainingFileChecksums().size());
		assertEquals("file1.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", processHandler.getRemainingFileChecksums().get(1).getFilename());
	}
	
	@Test
	public void shouldReturnSize0ForProcessedFileChecksumsWhenNoneHaveBeenProcessed() {
		addFileChecksum1();
		processHandler = new ProcessHandlerImpl(fileChecksums, null, false);
		assertEquals(0, processHandler.getProcessedFileChecksums().size());
	}
	
	@Test
	public void shouldReturnSize1ForProcessedFileChecksumsWhenOneHaveBeenProcessed() throws InterruptedException {
		addFileChecksum1();
		addFileChecksum2();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		// Two FileChecksums are now in the remaining list
		
		processFileChecksum();
		
		assertEquals(1, processHandler.getProcessedFileChecksums().size());
		assertEquals("file1.bin", processHandler.getProcessedFileChecksums().get(0).getFilename());
	}
	
	@Test
	public void whenFirstFileChecksumIsProcessedItShouldBeRemovedFromTheRemainingList() throws InterruptedException {
		addFileChecksum1();
		addFileChecksum2();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		// Two FileChecksums are now in the remaining list

		processFileChecksum();

		assertEquals(1, processHandler.getRemainingFileChecksums().size());
		assertEquals("file2.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());

	}

	@Test
	public void shouldProcess3FileChecksumsCorrectly() throws InterruptedException {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		// Three FileChecksums are now in the remaining list
		
		assertEquals(3, processHandler.getRemainingFileChecksums().size());
		assertEquals("file1.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", processHandler.getRemainingFileChecksums().get(1).getFilename());
		assertEquals("file3.bin", processHandler.getRemainingFileChecksums().get(2).getFilename());

		processFileChecksum();
		
		assertEquals(2, processHandler.getRemainingFileChecksums().size());
		assertEquals("file2.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file3.bin", processHandler.getRemainingFileChecksums().get(1).getFilename());
		assertEquals(1, processHandler.getProcessedFileChecksums().size());
		assertEquals("file1.bin", processHandler.getProcessedFileChecksums().get(0).getFilename());
		
		processFileChecksum();

		assertEquals(1, processHandler.getRemainingFileChecksums().size());
		assertEquals("file3.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());
		assertEquals(2, processHandler.getProcessedFileChecksums().size());
		assertEquals("file1.bin", processHandler.getProcessedFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", processHandler.getProcessedFileChecksums().get(1).getFilename());
		
		processFileChecksum();
		
		assertEquals(0, processHandler.getRemainingFileChecksums().size());
		assertEquals(3, processHandler.getProcessedFileChecksums().size());
		assertEquals("file1.bin", processHandler.getProcessedFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", processHandler.getProcessedFileChecksums().get(1).getFilename());
		assertEquals("file3.bin", processHandler.getProcessedFileChecksums().get(2).getFilename());

	}

	@Test
	public void shouldProcessAllFileChecksumsCorrectly() {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, true);
		bitrepositoryConnector.addObserver(processHandler);
		
		// Three FileChecksums are now in the remaining list
		
		assertEquals(3, processHandler.getRemainingFileChecksums().size());
		assertEquals("file1.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", processHandler.getRemainingFileChecksums().get(1).getFilename());
		assertEquals("file3.bin", processHandler.getRemainingFileChecksums().get(2).getFilename());
	
		processFileChecksum();
		
		assertEquals(0, processHandler.getRemainingFileChecksums().size());
		assertEquals(3, processHandler.getProcessedFileChecksums().size());
		assertEquals("file1.bin", processHandler.getProcessedFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", processHandler.getProcessedFileChecksums().get(1).getFilename());
		assertEquals("file3.bin", processHandler.getProcessedFileChecksums().get(2).getFilename());
		
	}
	
	@Test
	public void shouldPutFileChecksumBackInLineInCaseOfError() {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.ERROR);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		processFileChecksum();
		
		assertEquals(3, processHandler.getRemainingFileChecksums().size());
		assertEquals("file2.bin", processHandler.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file3.bin", processHandler.getRemainingFileChecksums().get(1).getFilename());
		assertEquals("file1.bin", processHandler.getRemainingFileChecksums().get(2).getFilename());
		assertEquals(0, processHandler.getProcessedFileChecksums().size());
	}
	
	@Test
	public void shouldReturnProgressHandler() {
		addFileChecksum1();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		assertNotNull(processHandler.getProgressHandler());
	}
	
	@Test
	public void progressShouldBeZeroWhenNoFilesAreProcessed() {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		assertEquals(0, processHandler.getProgressHandler().getProgress());
	}

	@Test
	public void progressShouldBe33WhenOneFileIsProcessed() {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		processFileChecksum();
		assertEquals(33, processHandler.getProgressHandler().getProgress());
	}

	@Test
	public void progressShouldBe66WhenOneFileIsProcessed() {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		processFileChecksum();
		processFileChecksum();
		assertEquals(66, processHandler.getProgressHandler().getProgress());
	}

	@Test
	public void progressShouldBe100WhenAllFilesAreProcessed() {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		bitrepositoryConnector = getBitrepositoryConnector(fileChecksum1, ThreadStatus.SUCCESS);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		processFileChecksum();
		processFileChecksum();
		processFileChecksum();
		assertEquals(100, processHandler.getProgressHandler().getProgress());
	}
	

	@Test
	public void progressShouldBeHandlesCorrectlyInCaseOfErrors() {
		addFileChecksum1();
		addFileChecksum2();
		addFileChecksum3();
		BitrepositoryConnectorStub bitrepositoryConnector = new BitrepositoryConnectorStub(fileChecksum1, ThreadStatus.ERROR);
		processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, false);
		bitrepositoryConnector.addObserver(processHandler);
		
		processFileChecksum();
		assertEquals(0, processHandler.getProgressHandler().getProgress());

		bitrepositoryConnector.setFileChecksum(fileChecksum2);
		bitrepositoryConnector.setThreadStatus(ThreadStatus.SUCCESS);
		processFileChecksum();
		assertEquals(33, processHandler.getProgressHandler().getProgress());

		bitrepositoryConnector.setFileChecksum(fileChecksum3);
		processFileChecksum();
		assertEquals(66, processHandler.getProgressHandler().getProgress());
		
		bitrepositoryConnector.setFileChecksum(fileChecksum1);
		processFileChecksum();
		assertEquals(100, processHandler.getProgressHandler().getProgress());
	}

	
	private void addFileChecksum1() {
		fileChecksum1 = new FileChecksumImpl("file1.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum1.setRemoteChecksum("9e5aae9572765f8bec9bca8c818188da");
		fileChecksums.add(fileChecksum1);
	}
	
	private void addFileChecksum2() {
		fileChecksum2 = new FileChecksumImpl("file2.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum2.setRemoteChecksum("00ffae9572765f8bec9bca8c81812211");
		fileChecksums.add(fileChecksum2);
	}

	private void addFileChecksum3() {
		fileChecksum3 = new FileChecksumImpl("file3.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum3.setRemoteChecksum("9e5aae9572765f8bec9bca8c818188da");
		fileChecksums.add(fileChecksum3);
	}
	
	private BitrepositoryConnector getBitrepositoryConnector(FileChecksum fileChecksum, ThreadStatus status) {
		BitrepositoryConnector bitrepositoryConnector = new BitrepositoryConnectorStub(fileChecksum, status);
		return bitrepositoryConnector;
	}
	
	private void processFileChecksum() {
		processHandler.processNext();
		try {
			Thread.sleep(50); // To make sure that the separate thread will finish
		} catch (InterruptedException e) {}

	}

	// handle messagebus error

}
