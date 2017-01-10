package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRepositoryConfiguration {

	private RepositoryConfiguration repositoryConfiguration;
	private RepositoryConfiguration self;
	private RepositoryConfiguration other;
	private static Path certificate;
	private static Path certificate2;
	private static Path certificate3;
	private static Path folder2;
	private static String tmp;
	private static Path settingsFolder;
	
	@BeforeClass
	public static void createTestCertificate() throws IOException {
		tmp = System.getProperty("java.io.tmpdir");
		certificate = Paths.get(tmp, "certificate.pem");
		certificate2 = Paths.get(tmp, "certificate2.pem");
		certificate3 = Paths.get(tmp, "certificate2.err");
		certificate.toFile().createNewFile();
		certificate2.toFile().createNewFile();
		certificate3.toFile().createNewFile();
		folder2 = Paths.get(tmp, "folder1");
		folder2.toFile().mkdir();
		
		settingsFolder = Paths.get(tmp).resolve("settingsFolder");
		settingsFolder.toFile().mkdir();
		File repositorySettings = settingsFolder.resolve("RepositorySettings.xml").toFile();
		repositorySettings.createNewFile();
		File referenceSettings = settingsFolder.resolve("ReferenceSettings.xml").toFile();
		referenceSettings.createNewFile();
	}
	
	@AfterClass
	public static void tearDown() throws IOException {
		certificate.toFile().delete();
		certificate2.toFile().delete();
		certificate3.toFile().delete();
		FileUtils.deleteDirectory(folder2.toFile());
		FileUtils.deleteDirectory(settingsFolder.toFile());
	}
	
	@Before
	public void setUp() throws InvalidArgumentException {
		repositoryConfiguration = new RepositoryConfigurationImpl("name1");
		
		self = new RepositoryConfigurationImpl("repo");
		self.setCollectionId("id");
		self.setPathToCertificate(certificate);
		self.setPathToChecksumList(certificate);
		self.setPathToSettingsFiles(settingsFolder);
		self.setPillarId("id");
		
		other = new RepositoryConfigurationImpl("repo");
		other.setCollectionId("id");
		other.setPathToCertificate(certificate);
		other.setPathToChecksumList(certificate);
		other.setPathToSettingsFiles(settingsFolder);
		other.setPillarId("id");

	}
	
	@Test
	public void shouldStoreItsNameAsName1() {
		assertEquals("name1", repositoryConfiguration.getName());
	}
	
	@Test(expected = InvalidArgumentException.class)
	public void shouldNotAllowBlankName() throws InvalidArgumentException {
		new RepositoryConfigurationImpl(" ");
	}
	
	@Test
	public void shouldStoreItsNameAsName2() throws InvalidArgumentException {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("name2");
		assertEquals("name2", repositoryConfiguration.getName());
	}

	@Test
	public void shouldSetNameToName3() throws InvalidArgumentException {
		repositoryConfiguration.setName("name3");
		assertEquals("name3", repositoryConfiguration.getName());
	}
	 
	
	@Test(expected = InvalidArgumentException.class)
	public void nameCannotBeNull() throws InvalidArgumentException {
		repositoryConfiguration.setName(null);
	}
	
	@Test
	public void nameShouldBeTrimmed() throws InvalidArgumentException {
		repositoryConfiguration.setName("   abc   ");
		assertEquals("abc", repositoryConfiguration.getName());
	}
	
	@Test
	public void shouldHaveCollectionIdC1() throws InvalidArgumentException {
		repositoryConfiguration.setCollectionId("c1");
		assertEquals("c1", repositoryConfiguration.getCollectionId());
	}
	
	@Test
	public void shouldHaveCollectionIdC2() throws InvalidArgumentException {
		repositoryConfiguration.setCollectionId("c2");
		assertEquals("c2", repositoryConfiguration.getCollectionId());
	}

	@Test(expected = InvalidArgumentException.class)
	public void shouldNotAllowBlankCollectionId() throws InvalidArgumentException {
		repositoryConfiguration.setCollectionId(null);
	}
	
	@Test
	public void shouldHavePathToCertificate1() throws InvalidArgumentException {
		repositoryConfiguration.setPathToCertificate(certificate);
		Path certificate = Paths.get(tmp).resolve("certificate.pem");
		assertEquals(certificate.toString(), repositoryConfiguration.getPathToCertificate().toString());
	}
	
	@Test
	public void shouldHavePathToCertificate2() throws InvalidArgumentException {
		repositoryConfiguration.setPathToCertificate(certificate2);
		Path certificate2 = Paths.get(tmp).resolve("certificate2.pem");
		assertEquals(certificate2.toString(), repositoryConfiguration.getPathToCertificate().toString());
	}

	@Test(expected = InvalidArgumentException.class)
	public void certificateMustBeFile() throws InvalidArgumentException {
		Path path = Paths.get("/does/not/exists/cert.pem");
		repositoryConfiguration.setPathToCertificate(path);
	}

	@Test(expected = InvalidArgumentException.class)
	public void extensionMustBePEM() throws InvalidArgumentException {
		repositoryConfiguration.setPathToCertificate(certificate3);
	}
	
	@Test
	public void shouldHavePathToChecksumList_tmp() throws InvalidArgumentException {
		repositoryConfiguration.setPathToChecksumList(certificate);
		Path certificate = Paths.get(tmp).resolve("certificate.pem");
		assertEquals("Just using a random file...", certificate.toString(), repositoryConfiguration.getPathToChecksumList().toString());
	}

	@Test
	public void shouldHavePathToChecksumList_tmp_folder1() throws InvalidArgumentException {
		repositoryConfiguration.setPathToChecksumList(certificate2);
		Path certificate2 = Paths.get(tmp).resolve("certificate2.pem");
		assertEquals("Just using a random file...", certificate2.toString(), repositoryConfiguration.getPathToChecksumList().toString());
	}

	@Test(expected = InvalidArgumentException.class)
	public void checksumFileMustBeFile() throws InvalidArgumentException {
		Path path = Paths.get("/does/not/exists/checksum.txt");
		repositoryConfiguration.setPathToChecksumList(path);
	}

	@Test(expected = InvalidArgumentException.class)
	public void checksumFileMustNotBeFolder() throws InvalidArgumentException {
		Path path = Paths.get(tmp);
		repositoryConfiguration.setPathToChecksumList(path);
	}

	@Test
	public void shouldHavePathToSettingsFiles_tmp_folder1() throws IOException, InvalidArgumentException {
		File repositorySettings = folder2.resolve("RepositorySettings.xml").toFile();
		repositorySettings.createNewFile();
		File referenceSettings = folder2.resolve("ReferenceSettings.xml").toFile();
		referenceSettings.createNewFile();
		repositoryConfiguration.setPathToSettingsFiles(folder2);
		Path folder1 = Paths.get(tmp).resolve("folder1");
		assertEquals(folder1.toString(), repositoryConfiguration.getPathToSettingsFiles().toString());
	}

	@Test(expected = InvalidArgumentException.class)
	public void pathToSettingsFilesMustBeAFolder() throws InvalidArgumentException {
		repositoryConfiguration.setPathToSettingsFiles(Paths.get("/does/not/exist"));
	}
	
	@Test(expected = InvalidArgumentException.class)
	public void pathToSettingsMustContainRepositorySettingsAndReferenceSettings() throws InvalidArgumentException {
		repositoryConfiguration.setPathToSettingsFiles(Paths.get(tmp));
	}
	
	@Test
	public void shouldHavePillarIdP1() throws InvalidArgumentException {
		repositoryConfiguration.setPillarId("p1");
		assertEquals("p1", repositoryConfiguration.getPillarId());
	}

	@Test
	public void shouldHavePillarIdP2() throws InvalidArgumentException {
		repositoryConfiguration.setPillarId("p2");
		assertEquals("p2", repositoryConfiguration.getPillarId());
	}

	@Test(expected = InvalidArgumentException.class)
	public void pillardIdMustNotBeBlank() throws InvalidArgumentException {
		repositoryConfiguration.setPillarId("");
	}
	
	@Test
	public void pillarIdMustBeTrimmed() throws InvalidArgumentException {
		repositoryConfiguration.setPillarId(" id ");
		assertEquals("id", repositoryConfiguration.getPillarId());
	}
	
	@Test
	public void collectionIdMustBeTrimmed() throws InvalidArgumentException {
		repositoryConfiguration.setCollectionId(" id");
		assertEquals("id", repositoryConfiguration.getCollectionId());
	}
	
	@Test
	public void shouldChangeNameCorrectly() throws InvalidArgumentException {
		RepositoryConfiguration r = new RepositoryConfigurationImpl("repo");
		assertEquals("repo", r.getName());
		r.setName("newName");
		assertEquals("newName", r.getName());
	}
	
	@Test
	public void shouldChangeNameCorrectly2() throws InvalidArgumentException {
		assertEquals("name1", repositoryConfiguration.getName());
		repositoryConfiguration.setName("newName");
		assertEquals("newName", repositoryConfiguration.getName());
	}
	
	// Testing the equals method
	
	@Test
	public void shouldBeTrueIfOtherIsThis() {
		assertTrue(repositoryConfiguration.equals(repositoryConfiguration));
	}
	
	@Test
	public void shouldBeFalseIfOtherIsNull() {
		assertFalse(repositoryConfiguration.equals(null));
	}
	
	@Test
	public void shouldBeFalseIfOtherIsADifferentClass() {
		assertFalse(repositoryConfiguration.equals(new Integer(0)));
	}

	@Test
	public void shouldBeFalseIfCollectionIdIsDifferent() throws InvalidArgumentException {
		other.setCollectionId("notId");
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseWhenNamesAreDifferent() throws InvalidArgumentException {
		other.setName("otherName");
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfCertPathDifferent() throws InvalidArgumentException {
		other.setPathToCertificate(certificate2);
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfChecksumPathDifferent() throws InvalidArgumentException {
		other.setPathToChecksumList(certificate2);
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfSettingsFilesPathDifferent() throws IOException, InvalidArgumentException {
		// These two file need to be in folder2
		File repositorySettings = folder2.resolve("RepositorySettings.xml").toFile();
		repositorySettings.createNewFile();
		File referenceSettings = folder2.resolve("ReferenceSettings.xml").toFile();
		referenceSettings.createNewFile();
		other.setPathToSettingsFiles(folder2);
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfPillarIDsDifferent() throws InvalidArgumentException {
		other.setPillarId("notId");
		assertFalse(self.equals(other));
	}

	
}

