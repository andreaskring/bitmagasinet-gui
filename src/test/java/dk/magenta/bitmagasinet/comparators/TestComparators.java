package dk.magenta.bitmagasinet.comparators;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;

public class TestComparators {

	private FileChecksum fileChecksum1;
	private FileChecksum fileChecksum2;
	private FileChecksum fileChecksum3;
	private FileChecksum fileChecksum4;
	private List<FileChecksum> list;
	
	@Before
	public void setUp() {
		fileChecksum1 = new FileChecksumImpl("a", "xca", "64");
		fileChecksum2 = new FileChecksumImpl("b", "cb", "64");
		fileChecksum3 = new FileChecksumImpl("c", "cc", "64");
		fileChecksum4 = new FileChecksumImpl("d", "cd", "64");
		fileChecksum1.setRemoteChecksum("xca");
		fileChecksum2.setRemoteChecksum("cb");
		fileChecksum3.setRemoteChecksum("does not match1");
		fileChecksum4.setRemoteChecksum("does not match2");
		list = new ArrayList<FileChecksum>();
	}
	
	@Test
	public void shouldSortOrderDBCACorrectlyByFileName() {
		list.add(fileChecksum4);
		list.add(fileChecksum2);
		list.add(fileChecksum3);
		list.add(fileChecksum1);
		
		Comparator<FileChecksum> comparator = new FilenameComparator();
		Collections.sort(list, comparator);
		
		assertEquals("a", list.get(0).getFilename());
		assertEquals("b", list.get(1).getFilename());
		assertEquals("c", list.get(2).getFilename());
		assertEquals("d", list.get(3).getFilename());
	}
	
	@Test
	public void shouldSortOrderDCABCorrectlyByFileName() {
		list.add(fileChecksum4);
		list.add(fileChecksum3);
		list.add(fileChecksum1);
		list.add(fileChecksum2);
		
		Comparator<FileChecksum> comparator = new FilenameComparator();
		Collections.sort(list, comparator);
		
		assertEquals("a", list.get(0).getFilename());
		assertEquals("b", list.get(1).getFilename());
		assertEquals("c", list.get(2).getFilename());
		assertEquals("d", list.get(3).getFilename());
	}

	@Test
	public void shouldSortOrderTFFTCorrectlyByChecksumMatch() {
		list.add(fileChecksum1);
		list.add(fileChecksum3);
		list.add(fileChecksum4);
		list.add(fileChecksum2);
		
		Comparator<FileChecksum> comparator = new ChecksumMatchComparator();
		Collections.sort(list, comparator);
		
		assertEquals(true, list.get(0).checksumsMatch());
		assertEquals(true, list.get(1).checksumsMatch());
		assertEquals(false, list.get(2).checksumsMatch());
		assertEquals(false, list.get(3).checksumsMatch());
	}

	@Test
	public void shouldSortOrderFTFTCorrectlyByChecksumMatch() {
		list.add(fileChecksum3);
		list.add(fileChecksum1);
		list.add(fileChecksum4);
		list.add(fileChecksum2);
		
		Comparator<FileChecksum> comparator = new ChecksumMatchComparator();
		Collections.sort(list, comparator);
		
		assertEquals(true, list.get(0).checksumsMatch());
		assertEquals(true, list.get(1).checksumsMatch());
		assertEquals(false, list.get(2).checksumsMatch());
		assertEquals(false, list.get(3).checksumsMatch());
	}

	@Test
	public void shouldSortCorrectlyAccordingToLocalChecksum() {
		list.add(fileChecksum4);
		list.add(fileChecksum3);
		list.add(fileChecksum2);
		list.add(fileChecksum1);
		
		Comparator<FileChecksum> comparator = new ChecksumComparator(ChecksumType.LOCAL);
		Collections.sort(list, comparator);
		
		assertEquals("cb", list.get(0).getLocalChecksum());
		assertEquals("cc", list.get(1).getLocalChecksum());
		assertEquals("cd", list.get(2).getLocalChecksum());
		assertEquals("xca", list.get(3).getLocalChecksum());
	}

	@Test
	public void shouldSortCorrectlyAccordingToRemoteChecksum() {
		list.add(fileChecksum4);
		list.add(fileChecksum3);
		list.add(fileChecksum2);
		list.add(fileChecksum1);
		
		Comparator<FileChecksum> comparator = new ChecksumComparator(ChecksumType.REMOTE);
		Collections.sort(list, comparator);
		
		assertEquals("cb", list.get(0).getRemoteChecksum());
		assertEquals("does not match1", list.get(1).getRemoteChecksum());
		assertEquals("does not match2", list.get(2).getRemoteChecksum());
		assertEquals("xca", list.get(3).getRemoteChecksum());
	}
}
