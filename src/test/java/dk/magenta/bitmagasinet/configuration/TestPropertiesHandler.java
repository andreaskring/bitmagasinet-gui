package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestPropertiesHandler {

	private RepositoryConfiguration repositoryConfiguration;
	private PropertiesHandler propertiesHandler;
	private Properties properties;
	private static Path certificate;
	private static Path settingsFolder;
	private static String tmp;
	
	@BeforeClass
	public static void createFilesAndFolders() throws IOException {
		tmp = System.getProperty("java.io.tmpdir");
		certificate = Paths.get(tmp, "certificate.pem");
		certificate.toFile().createNewFile();
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
		FileUtils.deleteDirectory(settingsFolder.toFile());
	}
	
	@Before
	public void setUp() {
		repositoryConfiguration = new RepositoryConfigurationImpl("repo");
		repositoryConfiguration.setCollectionId("id");
		repositoryConfiguration.setPathToCertificate(certificate);
		repositoryConfiguration.setPathToChecksumList(certificate);
		repositoryConfiguration.setPathToSettingsFiles(settingsFolder);
		repositoryConfiguration.setPillarId("id");
		
		properties = new Properties();
		properties.setProperty("collectionID", "id");
		properties.setProperty("name", "name");
		properties.setProperty("pathToCertificate", certificate.toString());
		properties.setProperty("pathToChecksumList", certificate.toString());
		properties.setProperty("pathToSettingsFiles", settingsFolder.toString());
		properties.setProperty("pillarID", "id");
		
		propertiesHandler = new PropertiesHandler();
	}
	
	@Test
	public void shouldHaveCollectionIdProperty() {
		assertEquals("id", propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("collectionID"));
	}
	
	@Test
	public void shouldHaveCollectionIdProperty2() {
		repositoryConfiguration.setCollectionId("id2");
		assertEquals("id2", propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("collectionID"));
	}
	
	@Test
	public void shouldHaveNamePropertyXWhenNameIsX() {
		repositoryConfiguration.setName("x");
		assertEquals("x", propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("name"));
	}

	@Test
	public void shouldHaveNamePropertyYWhenNameIsY() {
		repositoryConfiguration.setName("y");
		assertEquals("y", propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("name"));
	}
	
	@Test
	public void shouldHavePathToCertificate() {
		assertEquals(tmp + "/certificate.pem", propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("pathToCertificate"));
	}
	
	@Test
	public void shouldHavePathToChecksumListFile() {
		assertEquals("Just using a random file...",
				tmp + "/certificate.pem",
				propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("pathToChecksumList"));
	}
	
	@Test
	public void shouldHavePathToSettingsFiles() {
		assertEquals(settingsFolder.toString(), propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("pathToSettingsFiles"));
	}
	
	@Test
	public void shouldHavePillarIdProperty() {
		assertEquals("id", propertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration).getProperty("pillarID"));
	}
	
	@Test
	public void PropertyToRepoCollectionId() {
		assertEquals("id", propertiesHandler.convertPropertiesToRepositoryConfiguration(properties).getCollectionId());
	}
	
	@Test
	public void PropertyToRepoName() {
		assertEquals("name", propertiesHandler.convertPropertiesToRepositoryConfiguration(properties).getName());
	}

	@Test
	public void PropertyToRepoPathToCertificate() {
		assertEquals(certificate.toString(), propertiesHandler.convertPropertiesToRepositoryConfiguration(properties).getPathToCertificate().toString());
	}

	@Test
	public void PropertyToRepoPathToChecksumFile() {
		assertEquals(certificate.toString(), propertiesHandler.convertPropertiesToRepositoryConfiguration(properties).getPathToChecksumList().toString());
	}

	@Test
	public void PropertyToRepoPathToSettingsFiles() {
		assertEquals(settingsFolder.toString(), propertiesHandler.convertPropertiesToRepositoryConfiguration(properties).getPathToSettingsFiles().toString());
	}

	@Test
	public void PropertyToRepoPillarId() {
		assertEquals("id", propertiesHandler.convertPropertiesToRepositoryConfiguration(properties).getPillarId());
	}
	
}
