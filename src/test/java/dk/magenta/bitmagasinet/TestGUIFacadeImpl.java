package dk.magenta.bitmagasinet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.magenta.bitmagasinet.configuration.ConfigurationHandler;
import dk.magenta.bitmagasinet.configuration.ConfigurationHandlerImpl;
import dk.magenta.bitmagasinet.configuration.ConfigurationIOHandler;
import dk.magenta.bitmagasinet.configuration.ConfigurationIOHandlerImpl;
import dk.magenta.bitmagasinet.configuration.InvalidArgumentException;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;
import dk.magenta.bitmagasinet.configuration.RepositoryConfigurationImpl;

public class TestGUIFacadeImpl {

	private Path bitMagGUI;
	private RepositoryConfiguration repositoryConfiguration2;
	private RepositoryConfiguration repositoryConfiguration3;
	
	@Before
	public void setUp() {
		bitMagGUI = Paths.get(System.getProperty("java.io.tmpdir")).resolve("BitMagGUI");
	}
	
	@After
	public void tearDown() throws IOException {
		FileUtils.deleteDirectory(bitMagGUI.toFile());
	}
	
	@Test
	public void shouldReturnTwoRepoConfsWhenTwoFilesInRepoConf() throws IOException, InvalidArgumentException {
		
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(bitMagGUI);
		// repoConf folder now created
		
		setUpRepositoryConfigurations();
		
		ConfigurationIOHandler configurationIOHandler = new ConfigurationIOHandlerImpl(configurationHandler);
		configurationIOHandler.writeRepositoryConfiguration(repositoryConfiguration2);
		configurationIOHandler.writeRepositoryConfiguration(repositoryConfiguration3);

		GUIFacade guiFacade = new GUIFacadeImpl(configurationHandler);
		
		assertEquals(2, guiFacade.getRepositoryConfigurationNames().size());
		assertTrue("repo", guiFacade.getRepositoryConfigurationNames().contains("repo"));
		assertTrue("repo2", guiFacade.getRepositoryConfigurationNames().contains("repo2"));
		
//		assertEquals(2, guiFacade.get)
	}
	
	
	/**
	 * Using the "new" way to create temp folder and files
	 * @throws IOException 
	 * @throws InvalidArgumentException 
	 * @throws InterruptedException 
	 */
	@Ignore
	@Test
	public void shouldWriteRepositoryConfigurationCorrectly() throws IOException, InvalidArgumentException {
		
		// Create necessary folders and files
//		settings.newFile("RepositorySettings.xml");
//		settings.newFile("ReferenceSettings.xml");
//		File certificate = settings.newFile("cert.pem");
//		
//		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("repo");
//		repositoryConfiguration.setPathToSettingsFiles(settings.getRoot().toPath());
//		repositoryConfiguration.setPathToCertificate(certificate.toPath());
//		repositoryConfiguration.setCollectionId("2");
//		repositoryConfiguration.setPillarId("test");
//		repositoryConfiguration.setPathToChecksumList(certificate.toPath()); // Just using a random file
//		
//		GUIFacade guiFacade = new GUIFacadeImpl(new ConfigurationHandlerImpl(bitMagGUITempFolder.getRoot().toPath()));
//		
//		guiFacade.writeRepositoryConfiguration(repositoryConfiguration);
//		Path pathToRepoConfFile = bitMagGUITempFolder.getRoot().toPath().resolve("repoConf").resolve("repo.conf");
//		
//		assertTrue(pathToRepoConfFile.toFile().isFile());
		
		
	}
	
	private void setUpRepositoryConfigurations() throws IOException, InvalidArgumentException {
		Path settings = bitMagGUI.resolve("settings");
		settings.toFile().mkdir();
		settings.resolve("RepositorySettings.xml").toFile().createNewFile();
		settings.resolve("ReferenceSettings.xml").toFile().createNewFile();
		Path certificate = settings.resolve("cert.pem");
		certificate.toFile().createNewFile();

		repositoryConfiguration2 = new RepositoryConfigurationImpl("repo");
		repositoryConfiguration2.setPathToSettingsFiles(settings);
		repositoryConfiguration2.setPathToCertificate(certificate);
		repositoryConfiguration2.setCollectionId("2");
		repositoryConfiguration2.setPillarId("test");
		repositoryConfiguration2.setPathToChecksumList(certificate); // Just using a random file

		repositoryConfiguration3 = new RepositoryConfigurationImpl("repo2");
		repositoryConfiguration3.setPathToSettingsFiles(settings);
		repositoryConfiguration3.setPathToCertificate(certificate);
		repositoryConfiguration3.setCollectionId("2");
		repositoryConfiguration3.setPillarId("test");
		repositoryConfiguration3.setPathToChecksumList(certificate); // Just using a random file
	}

	
}
