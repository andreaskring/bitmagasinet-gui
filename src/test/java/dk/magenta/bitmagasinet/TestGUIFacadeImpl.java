package dk.magenta.bitmagasinet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import dk.magenta.bitmagasinet.configuration.ConfigurationHandlerImpl;
import dk.magenta.bitmagasinet.configuration.InvalidArgumentException;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;
import dk.magenta.bitmagasinet.configuration.RepositoryConfigurationImpl;

public class TestGUIFacadeImpl {

	private Path bitMagGUI;

	@Rule
	public TemporaryFolder bitMagGUITempFolder = new TemporaryFolder();
	
	@Rule
	public TemporaryFolder settings = new TemporaryFolder();

	
	@Before
	public void setUp() {
		bitMagGUI = Paths.get(System.getProperty("java.io.tmpdir")).resolve("BitMagGUI");
	}
	
//	@After
//	public void tearDown() throws IOException {
//		FileUtils.deleteDirectory(bitMagGUI.toFile());
//	}
	
	@Test
	public void shouldReturnTwoRepoConfsWhenTwoFilesInRepoConf() throws IOException {
		// Path bitMagGUIPath = bi
		GUIFacade guiFacade = new GUIFacadeImpl(new ConfigurationHandlerImpl(bitMagGUI));
		
		Path repo1 = bitMagGUI.resolve("repoConf").resolve("repo1.conf");
		Path repo2 = bitMagGUI.resolve("repoConf").resolve("repo2.conf");
		repo1.toFile().createNewFile();
		repo2.toFile().createNewFile();
		
		assertEquals(2, guiFacade.getRepositoryConfigurationNames().size());
		assertTrue("repo1", guiFacade.getRepositoryConfigurationNames().contains("repo1"));
		assertTrue("repo2", guiFacade.getRepositoryConfigurationNames().contains("repo2"));
	}
	
	/**
	 * Using the "new" way to create temp folder and files
	 * @throws IOException 
	 * @throws InvalidArgumentException 
	 * @throws InterruptedException 
	 */
	@Test
	public void shouldWriteRepositoryConfigurationCorrectly() throws IOException, InvalidArgumentException {
		
		// Create necessary folders and files
		settings.newFile("RepositorySettings.xml");
		settings.newFile("ReferenceSettings.xml");
		File certificate = settings.newFile("cert.pem");
		
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("repo");
		repositoryConfiguration.setPathToSettingsFiles(settings.getRoot().toPath());
		repositoryConfiguration.setPathToCertificate(certificate.toPath());
		repositoryConfiguration.setCollectionId("2");
		repositoryConfiguration.setPillarId("test");
		repositoryConfiguration.setPathToChecksumList(certificate.toPath()); // Just using a random file
		
		GUIFacade guiFacade = new GUIFacadeImpl(new ConfigurationHandlerImpl(bitMagGUITempFolder.getRoot().toPath()));
		
		guiFacade.writeRepositoryConfiguration(repositoryConfiguration);
		Path pathToRepoConfFile = bitMagGUITempFolder.getRoot().toPath().resolve("repoConf").resolve("repo.conf");
		
		assertTrue(pathToRepoConfFile.toFile().isFile());
		
		
	}
	
}
