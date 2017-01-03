package dk.magenta.bitmagasinet.checksum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bitrepository.common.utils.Base16Utils;

import dk.magenta.bitmagasinet.Constants;

// TODO: make interface
class ChecksumIOHandler {

	private DateStrategy dateStrategy;
	private static final String TAB = "\t"; 
	
	public ChecksumIOHandler(DateStrategy dateStrategy) {
		this.dateStrategy = dateStrategy;
	}
	
	public List<FileChecksum> readChecksumList(File file) throws IOException, InvalidChecksumFileException {
		
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

	public void writeResultFiles(Path path, List<FileChecksum> fileChecksums) throws IOException {
		writeChecksumList(path, fileChecksums);
		writeHeaderFile(path);
	}

	private void writeChecksumList(Path path, List<FileChecksum> fileChecksums) throws IOException {
		Path file = path.resolve(getRelativePathToResultChecksumFile());
		try(FileWriter writer = new FileWriter(file.toFile())) {
			for (FileChecksum fileChecksum : fileChecksums) {
				String line = new StringBuilder()
				.append(fileChecksum.getFilename())
				.append(TAB)
				.append(fileChecksum.checksumsMatch())
				.append(TAB)
				.append(Base16Utils.decodeBase16(fileChecksum.getSalt()))				
				.append(TAB)
				.append(fileChecksum.getLocalChecksum())
				.append(TAB)
				.append(fileChecksum.getRemoteChecksum())
				.append('\n')
				.toString();
				writer.write(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void writeHeaderFile(Path path) {
		// TODO Auto-generated method stub
		
	}
	
	Path getRelativePathToResultChecksumFile() {
		Date d = dateStrategy.getDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_kkmmss_z");
		
		return Paths.get(Constants.CHECKSUMFILE_PREFIX + simpleDateFormat.format(d) + Constants.CHECKSUMFILE_EXT);
	}

	
}
