package dk.magenta.bitmagasinet.comparators;

import java.util.Comparator;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class ChecksumComparator implements Comparator<FileChecksum> {

	private ChecksumType checksumType;
	
	public ChecksumComparator(ChecksumType checksumType) {
		this.checksumType = checksumType;
	}
	
	@Override
	public int compare(FileChecksum fileChecksum1, FileChecksum fileChecksum2) {
		if (checksumType == ChecksumType.LOCAL) {
			return fileChecksum1.getLocalChecksum().compareTo(fileChecksum2.getLocalChecksum());
		} else {
			return fileChecksum1.getRemoteChecksum().compareTo(fileChecksum2.getRemoteChecksum());
		}
	}

}
