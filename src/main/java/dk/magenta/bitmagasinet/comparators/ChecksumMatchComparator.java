package dk.magenta.bitmagasinet.comparators;

import java.util.Comparator;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class ChecksumMatchComparator implements Comparator<FileChecksum> {

	@Override
	public int compare(FileChecksum fileChecksum1, FileChecksum fileChecksum2) {
		return -1 * Boolean.compare(fileChecksum1.checksumsMatch(), fileChecksum2.checksumsMatch());
	}

}
