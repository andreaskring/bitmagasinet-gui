package dk.magenta.bitmagasinet.checksum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestChecksumIOHandler {

	private Path localeConfigurationFolder;

	@BeforeClass
	public static void setUp() throws Exception {
		// localeConfigurationFolder = Paths.get(System.getProperty("java.io.tmpdir")).resolve("checksumDir");
		File checksumFile = new File(TestChecksumIOHandler.class.getResource("/checkSumTestList.txt").getFile());
		FileReader reader = new FileReader(checksumFile);
		BufferedReader r = new BufferedReader(reader);
		String line = r.readLine();
		while (line != null) {
			System.out.println(line);
			line = r.readLine();
		}
		reader.close();
	}
	
	@AfterClass
	public static void tearDown() throws IOException {
		// FileUtils.deleteDirectory(localeConfigurationFolder.toFile());
	}
	
	@Test
	public void shouldReadChecksumFileCorrectly() {
		// ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(localeConfigurationFolder);
		// List<FileChecksum> fileChecksumList = ChecksumIOHandler.readChecksumList(path)
	}
	
}
