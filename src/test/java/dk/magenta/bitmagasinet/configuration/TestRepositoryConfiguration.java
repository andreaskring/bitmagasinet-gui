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
import org.junit.Ignore;
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
	public void setUp() {
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
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAllowBlankName() {
		new RepositoryConfigurationImpl(" ");
	}
	
	@Test
	public void shouldStoreItsNameAsName2() {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("name2");
		assertEquals("name2", repositoryConfiguration.getName());
	}

	@Test
	public void shouldSetNameToName3() {
		repositoryConfiguration.setName("name3");
		assertEquals("name3", repositoryConfiguration.getName());
	}
	 
	
	@Test(expected = IllegalArgumentException.class)
	public void nameCannotBeNull() {
		repositoryConfiguration.setName(null);
	}
	
	@Test
	public void nameShouldBeTrimmed() {
		repositoryConfiguration.setName("   abc   ");
		assertEquals("abc", repositoryConfiguration.getName());
	}
	
	@Test
	public void shouldHaveCollectionIdC1() {
		repositoryConfiguration.setCollectionId("c1");
		assertEquals("c1", repositoryConfiguration.getCollectionId());
	}
	
	@Test
	public void shouldHaveCollectionIdC2() {
		repositoryConfiguration.setCollectionId("c2");
		assertEquals("c2", repositoryConfiguration.getCollectionId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAllowBlankCollectionId() {
		repositoryConfiguration.setCollectionId(null);
	}
	
	@Test
	public void shouldHavePathToCertificate1() {
		repositoryConfiguration.setPathToCertificate(certificate);
		assertEquals(tmp + "/certificate.pem", repositoryConfiguration.getPathToCertificate().toString());
	}
	
	@Test
	public void shouldHavePathToCertificate2() {
		repositoryConfiguration.setPathToCertificate(certificate2);
		assertEquals(tmp + "/certificate2.pem", repositoryConfiguration.getPathToCertificate().toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void certificateMustBeFile() {
		Path path = Paths.get("/does/not/exists/cert.pem");
		repositoryConfiguration.setPathToCertificate(path);
	}

	@Test(expected = IllegalArgumentException.class)
	public void extensionMustBePEM() {
		repositoryConfiguration.setPathToCertificate(certificate3);
	}
	
	@Test
	public void shouldHavePathToChecksumList_tmp() {
		repositoryConfiguration.setPathToChecksumList(certificate);
		assertEquals("Just using a random file...", tmp + "/certificate.pem", repositoryConfiguration.getPathToChecksumList().toString());
	}

	@Test
	public void shouldHavePathToChecksumList_tmp_folder1() {
		repositoryConfiguration.setPathToChecksumList(certificate2);
		assertEquals("Just using a random file...", tmp + "/certificate2.pem", repositoryConfiguration.getPathToChecksumList().toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void checksumFileMustBeFile() {
		Path path = Paths.get("/does/not/exists/checksum.txt");
		repositoryConfiguration.setPathToChecksumList(path);
	}

	@Test(expected = IllegalArgumentException.class)
	public void checksumFileMustNotBeFolder() {
		Path path = Paths.get(tmp);
		repositoryConfiguration.setPathToChecksumList(path);
	}

	@Test
	public void shouldHavePathToSettingsFiles_tmp_folder1() throws IOException {
		File repositorySettings = folder2.resolve("RepositorySettings.xml").toFile();
		repositorySettings.createNewFile();
		File referenceSettings = folder2.resolve("ReferenceSettings.xml").toFile();
		referenceSettings.createNewFile();
		repositoryConfiguration.setPathToSettingsFiles(folder2);
		assertEquals(tmp + "/folder1", repositoryConfiguration.getPathToSettingsFiles().toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void pathToSettingsFilesMustBeAFolder() {
		repositoryConfiguration.setPathToSettingsFiles(Paths.get("/does/not/exist"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void pathToSettingsMustContainRepositorySettingsAndReferenceSettings() {
		repositoryConfiguration.setPathToSettingsFiles(Paths.get(tmp));
	}
	
	@Test
	public void shouldHavePillarIdP1() {
		repositoryConfiguration.setPillarId("p1");
		assertEquals("p1", repositoryConfiguration.getPillarId());
	}

	@Test
	public void shouldHavePillarIdP2() {
		repositoryConfiguration.setPillarId("p2");
		assertEquals("p2", repositoryConfiguration.getPillarId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void pillardIdMustNotBeBlank() {
		repositoryConfiguration.setPillarId("");
	}
	
	@Test
	public void pillarIdMustBeTrimmed() {
		repositoryConfiguration.setPillarId(" id ");
		assertEquals("id", repositoryConfiguration.getPillarId());
	}
	
	@Test
	public void collectionIdMustBeTrimmed() {
		repositoryConfiguration.setCollectionId(" id");
		assertEquals("id", repositoryConfiguration.getCollectionId());
	}
	
	@Test
	public void shouldChangeNameCorrectly() {
		RepositoryConfiguration r = new RepositoryConfigurationImpl("repo");
		assertEquals("repo", r.getName());
		r.setName("newName");
		assertEquals("newName", r.getName());
	}
	
	@Test
	public void shouldChangeNameCorrectly2() {
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
	public void shouldBeFalseIfCollectionIdIsDifferent() {
		other.setCollectionId("notId");
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseWhenNamesAreDifferent() {
		other.setName("otherName");
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfCertPathDifferent() {
		other.setPathToCertificate(certificate2);
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfChecksumPathDifferent() {
		other.setPathToChecksumList(certificate2);
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfSettingsFilesPathDifferent() throws IOException {
		// These two file need to be in folder2
		File repositorySettings = folder2.resolve("RepositorySettings.xml").toFile();
		repositorySettings.createNewFile();
		File referenceSettings = folder2.resolve("ReferenceSettings.xml").toFile();
		referenceSettings.createNewFile();
		other.setPathToSettingsFiles(folder2);
		assertFalse(self.equals(other));
	}
	
	@Test
	public void shouldBeFalseIfPillarIDsDifferent() {
		other.setPillarId("notId");
		assertFalse(self.equals(other));
	}

	
}

