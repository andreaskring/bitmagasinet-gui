package dk.magenta.bitmagasinet.checksum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dk.magenta.bitmagasinet.Constants;

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

	public static void writeResultFiles(Path path) {
		writeChecksumList(path);
		writeHeaderFile(path);
	}

	private static void writeChecksumList(Path path) {
		
		
	}
	
	private static void writeHeaderFile(Path path) {
		// TODO Auto-generated method stub
		
	}
	
	static Path getRelativePathToResultChecksumFile() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(1879, Calendar.MARCH, 14, 12, 0, 0);
		Date d = calendar.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kkmmss_z");
		
		return Paths.get(Constants.CHECKSUMFILE_PREFIX + sdf.format(d));
	}

	
}
