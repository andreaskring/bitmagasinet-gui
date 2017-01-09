package dk.magenta.bitmagasinet.checksum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bitrepository.common.utils.Base16Utils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import dk.magenta.bitmagasinet.configuration.InvalidArgumentException;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;
import dk.magenta.bitmagasinet.configuration.RepositoryConfigurationImpl;

public class TestChecksumIOHandler {

	private File checksumFile;
	private ChecksumIOHandler checksumIOHandler;
	private static Path path;

	/*
	@BeforeClass
	public static void setUp2() throws Exception {
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
	
	
	@BeforeClass
	public static void makeTestFolder() {
		String tmp = System.getProperty("java.io.tmpdir");
		path = Paths.get(tmp).resolve("temp");
		path.toFile().mkdir();
	}

	@AfterClass
	public static void tearDown() throws IOException {
		FileUtils.deleteDirectory(path.toFile());
	}
	
	@Before
	public void setUp() {
		checksumFile = new File(TestChecksumIOHandler.class.getResource("/checkSumTestList.txt").getFile());
		checksumIOHandler = new ChecksumIOHandler(new FixedDateStrategy(1879, Calendar.MARCH, 14, 12, 0, 0));
	}

	@Test
	public void shouldReadChecksumFileCorrectly() throws IOException, InvalidChecksumFileException {
		List<FileChecksum> fileChecksumList = checksumIOHandler.readChecksumList(checksumFile);
		
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
	
	@Test
	public void resultFileShouldHaveName_ChecksumCheckResult_1879_03_14_120000_CET() {
		assertEquals("ChecksumCheckResult-1879-03-14_120000_CET.txt", checksumIOHandler.getRelativePathToResultChecksumFile().toString());
	}

	@Test
	public void resultFileShouldHaveName_ChecksumCheckResult_1879_03_14_130000_CET() {
		ChecksumIOHandler checksumIOHandler = new ChecksumIOHandler(new FixedDateStrategy(1879, Calendar.MARCH, 14, 13, 0, 0));
		assertEquals("ChecksumCheckResult-1879-03-14_130000_CET.txt", checksumIOHandler.getRelativePathToResultChecksumFile().toString());
	}

	@Test
	public void headerFileShouldHaveName_Header_1879_03_14_120000_CET() {
		assertEquals("Header-1879-03-14_120000_CET.txt", checksumIOHandler.getRelativePathToHeaderFile().toString());
	}

	@Test
	public void headerFileShouldHaveName_Header_1879_03_14_130000_CET() {
		ChecksumIOHandler checksumIOHandler = new ChecksumIOHandler(new FixedDateStrategy(1879, Calendar.MARCH, 14, 13, 0, 0));
		assertEquals("Header-1879-03-14_130000_CET.txt", checksumIOHandler.getRelativePathToHeaderFile().toString());
	}

	@Test
	public void writeChecksumFileRoundTrip() throws IOException, InvalidChecksumFileException, InvalidArgumentException {
		List<FileChecksum> fileChecksumList = checksumIOHandler.readChecksumList(checksumFile);
		fileChecksumList.get(0).setRemoteChecksum("0f2dd3c5a366a1e860e2f8a1afc29987");
		fileChecksumList.get(1).setRemoteChecksum("0f2dd3c5a366a1e860e2f8a1afc29987");
		fileChecksumList.get(2).setRemoteChecksum("c100d3c5a366a1e860e2f8a1afc21110");
		assertEquals("c100d3c5a366a1e860e2f8a1afc21110", fileChecksumList.get(2).getRemoteChecksum());
		
		checksumIOHandler.writeResultFiles(path, fileChecksumList, new RepositoryConfigurationImpl("repo"), new Date(), new Date());
		
		Path outputFile = path.resolve(checksumIOHandler.getRelativePathToResultChecksumFile());
		List<String> lines = Files.readAllLines(outputFile);
		
		assertTrue(lines.get(0).matches("file1.bin\\ttrue\\t64\\t0f2dd3c5a366a1e860e2f8a1afc29987\\t0f2dd3c5a366a1e860e2f8a1afc29987"));
		assertTrue(lines.get(1).matches("file2.bin\\tfalse\\t54\\tef44d3c5a366a1e860e2f8a1afc2da21\\t0f2dd3c5a366a1e860e2f8a1afc29987"));
		assertTrue(lines.get(2).matches("file3.bin\\ttrue\\t44\\tc100d3c5a366a1e860e2f8a1afc21110\\tc100d3c5a366a1e860e2f8a1afc21110"));
	}

	// TODO: this test case is not complete - need to finish this
	@Ignore
	@Test
	public void writeHeaderFileRoundTrip() throws IOException, InvalidChecksumFileException, InvalidArgumentException {
		List<FileChecksum> fileChecksumList = checksumIOHandler.readChecksumList(checksumFile);
		fileChecksumList.get(0).setRemoteChecksum("0f2dd3c5a366a1e860e2f8a1afc29987");
		fileChecksumList.get(1).setRemoteChecksum("0f2dd3c5a366a1e860e2f8a1afc29987");
		fileChecksumList.get(2).setRemoteChecksum("c100d3c5a366a1e860e2f8a1afc21110");
		assertEquals("c100d3c5a366a1e860e2f8a1afc21110", fileChecksumList.get(2).getRemoteChecksum());
		
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("repo");
		repositoryConfiguration.setCollectionId("collectionID");
		repositoryConfiguration.setPillarId("pillarID");
		
		checksumIOHandler.writeResultFiles(path, fileChecksumList, repositoryConfiguration, null, null);
	}
	
}
