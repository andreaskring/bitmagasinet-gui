package dk.magenta.bitmagasinet.comparators;

import java.util.Comparator;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class FilenameComparator implements Comparator<FileChecksum> {

	@Override
	public int compare(FileChecksum fileChecksum1, FileChecksum fileChecksum2) {
		return fileChecksum1.getFilename().compareTo(fileChecksum2.getFilename());
	}

}
