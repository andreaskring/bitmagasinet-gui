package dk.magenta.bitmagasinet.checksum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ChecksumIOHandler {

	public static List<FileChecksum> readChecksumList(File file) throws IOException, InvalidChecksumFileException {
		
		List<FileChecksum> fileChecksumList = new ArrayList<FileChecksum>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file)); 
		try {
			String line = reader.readLine();
			while (line != null) {
				if (!ChecksumFileValidator.isLineValid(line)) {
					throw new InvalidChecksumFileException("Formatet af checksumfilen er ikke korrekt");
				}
				
				String[] values = line.split("\\t");
				String filename = values[0];
				String salt = values[1];
				String checksum = values[2].substring(0, 32);
				
				FileChecksum fileChecksum = new FileChecksumImpl(filename, checksum, salt);
				fileChecksumList.add(fileChecksum);
				
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			reader.close();
		}
		
		return fileChecksumList;
	};

}
