package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRepositoryConfiguration {

	private RepositoryConfiguration repositoryConfiguration;
	private static Path certificate;
	private static Path certificate2;
	private static Path certificate3;
	private static Path folder1;
	private static Path folder2;
	private static String tmp;
	
	@BeforeClass
	public static void createTestCertificate() throws IOException {
		tmp = System.getProperty("java.io.tmpdir");
		certificate = Paths.get(tmp, "certificate.pem");
		certificate2 = Paths.get(tmp, "certificate2.pem");
		certificate3 = Paths.get(tmp, "certificate2.err");
		certificate.toFile().createNewFile();
		certificate2.toFile().createNewFile();
		certificate3.toFile().createNewFile();
		folder1 = Paths.get(tmp);
		folder2 = Paths.get(tmp, "folder1");
		folder2.toFile().mkdir();
	}
	
	@Before
	public void setUp() {
		repositoryConfiguration = new RepositoryConfigurationImpl("name1");
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
}

