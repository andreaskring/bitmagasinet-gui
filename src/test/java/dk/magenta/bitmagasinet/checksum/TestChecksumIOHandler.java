package dk.magenta.bitmagasinet.checksum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.bitrepository.common.utils.Base16Utils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestChecksumIOHandler {

	private static Path localeConfigurationFolder;
	private File checksumFile;

	/*
	@BeforeClass
	public static void setUp() throws Exception {
		localeConfigurationFolder = Paths.get(System.getProperty("java.io.tmpdir")).resolve("checksumDir");
		

//		File checksumFile = new File(TestChecksumIOHandler.class.getResource("/checkSumTestList.txt").getFile());
//		FileReader reader = new FileReader(checksumFile);
//		BufferedReader r = new BufferedReader(reader);
//		String line = r.readLine();
//		while (line != null) {
//			System.out.println(line);
//			line = r.readLine();
//		}
//		reader.close();
	}
	*/
	
//	@AfterClass
//	public static void tearDown() throws IOException {
//		FileUtils.deleteDirectory(localeConfigurationFolder.toFile());
//	}
	
	@Before
	public void setUp2() {
		checksumFile = new File(TestChecksumIOHandler.class.getResource("/checkSumTestList.txt").getFile());
	}

	@Test
	public void shouldReadChecksumFileCorrectly() throws IOException, InvalidChecksumFileException {
		// ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(localeConfigurationFolder);
		List<FileChecksum> fileChecksumList = ChecksumIOHandler.readChecksumList(checksumFile);
		
		FileChecksum fileChecksum = fileChecksumList.get(0);
		assertEquals("file1.bin", fileChecksum.getFilename());
		assertTrue(Arrays.equals(Base16Utils.encodeBase16("64"), fileChecksum.getSalt()));
		assertEquals("0f2dd3c5a366a1e860e2f8a1afc29987", fileChecksum.getLocalChecksum());
		
		fileChecksum = fileChecksumList.get(1);
		assertEquals("file2.bin", fileChecksum.getFilename());
		assertTrue(Arrays.equals(Base16Utils.encodeBase16("54"), fileChecksum.getSalt()));
		assertEquals("ef44d3c5a366a1e860e2f8a1afc2da21", fileChecksum.getLocalChecksum());

		fileChecksum = fileChecksumList.get(2);
		assertEquals("file3.bin", fileChecksum.getFilename());
		assertTrue(Arrays.equals(Base16Utils.encodeBase16("44"), fileChecksum.getSalt()));
		assertEquals("c100d3c5a366a1e860e2f8a1afc21110", fileChecksum.getLocalChecksum());

	}
	
}
