package dk.magenta.bitmagasinet.configuration;

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

public class TestConfigurationIOHandler {

	private static Path certificate;
	private static Path settingsFolder;
	private static String tmp;
	private static ConfigurationHandler configurationHandler;

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
		FileUtils.deleteDirectory(configurationHandler.getPathToRepositoryConfigurations().toFile());
	}
	
	@Test
	public void writeFollowedByReadShouldBeConsistent() throws IOException, InvalidArgumentException {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("repo");
		repositoryConfiguration.setCollectionId("id");
		repositoryConfiguration.setPathToCertificate(certificate);
		repositoryConfiguration.setPathToChecksumList(certificate);
		repositoryConfiguration.setPathToSettingsFiles(settingsFolder);
		repositoryConfiguration.setPillarId("id");

		configurationHandler = new ConfigurationHandlerImpl(Paths.get(tmp));
		ConfigurationIOHandler configurationIOHandler = new ConfigurationIOHandlerImpl(configurationHandler);
		
		configurationIOHandler.writeRepositoryConfiguration(repositoryConfiguration);
		RepositoryConfiguration roundTrip = configurationIOHandler.readRepositoryConfiguration("repo");
		
		assertTrue(repositoryConfiguration.equals(roundTrip));
		
	}
}
