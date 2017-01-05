package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfigurationHandlerImpl {

	private ConfigurationHandler configurationHandler;
	private RepositoryConfiguration repositoryConfiguration1;
	private RepositoryConfiguration repositoryConfiguration2;
	private RepositoryConfiguration repositoryConfiguration3;
	private Path bitMagGUI;

	@Before
	public void setUp() throws InvalidArgumentException {
		bitMagGUI = Paths.get(System.getProperty("java.io.tmpdir")).resolve("BitMagGUI");
		configurationHandler = new ConfigurationHandlerImpl(bitMagGUI);
		repositoryConfiguration1 = new RepositoryConfigurationImpl("r1");
	}
	
	@After
	public void tearDown() throws IOException {
		FileUtils.deleteDirectory(bitMagGUI.toFile());
	}
	
	@Test
	public void shouldGetRepositoryConfigurationWithNameR1() throws InvalidArgumentException {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		assertEquals("r1", configurationHandler.getRepositoryConfiguration("r1").getName());
	}
	
	@Test
	public void shouldGetRepositoryConfigurationWithNameR2() throws IllegalArgumentException, InvalidArgumentException {
		configurationHandler.addRepositoryConfiguration(new RepositoryConfigurationImpl("r2"));
		assertEquals("r2", configurationHandler.getRepositoryConfiguration("r2").getName());
	}

	@Test(expected = InvalidArgumentException.class)
	public void shouldNotAllowNullName() throws InvalidArgumentException {
		configurationHandler.getRepositoryConfiguration(null);
	}

	@Test(expected = InvalidArgumentException.class)
	public void shouldNotAllowEmptykName() throws InvalidArgumentException{
		configurationHandler.getRepositoryConfiguration("");
	}

	@Test(expected = InvalidArgumentException.class)
	public void shouldNotAllowBlankName() throws InvalidArgumentException {
		configurationHandler.getRepositoryConfiguration(" ");
	}

	
	@Test(expected = InvalidArgumentException.class)
	public void shouldThrowExceptionWhenAddedTwoRepositoryConfigurationsWithTheSameName() throws InvalidArgumentException {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
	}
	
	@Test
	public void shouldReturnFalseWhenRepoConfigNotInMap() {
		assertFalse(configurationHandler.containsRepositoryConfiguration("r1"));
	}

	@Test
	public void shouldReturnTrueWhenRepoConfigIsInMap() throws InvalidArgumentException {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		assertTrue(configurationHandler.containsRepositoryConfiguration("r1"));
	}
	
	@Test
	public void shouldDeleteRepositoryConfiguration() throws InvalidArgumentException {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		configurationHandler.deleteRepositoryConfiguration(repositoryConfiguration1.getName());
		assertFalse(configurationHandler.containsRepositoryConfiguration(repositoryConfiguration1.getName()));
	}
	
	@Test(expected = InvalidArgumentException.class)
	public void shouldThrowExceptionWhenTryingToDeleteUnknownRepoConfig() throws InvalidArgumentException {
		configurationHandler.deleteRepositoryConfiguration("unknown");
	}
	
	@Test
	public void shouldReturnBitMagGUIWhenPathToLocalConfIsBitMagGUI() {
		Path localeConfigurationFolder = bitMagGUI;
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(localeConfigurationFolder);
		assertEquals(localeConfigurationFolder.toString(), configurationHandler.getPathToLocalConfigurationFolder().toString());
	}

	@Test
	public void shouldReturnRepoConfAsNameOfRepoConfFolder() {
		Path repoConf = bitMagGUI.resolve("repoConf");
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(bitMagGUI);
		assertEquals(repoConf.toString(), configurationHandler.getPathToRepositoryConfigurations().toString());
	}
	
	@Test
	public void shouldHaveLocalConfFolderProgramFiles() {
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl();
		Path path = Paths.get(System.getProperty("user.home")).resolve("BitMagGUI");
		assertEquals(path.toString(), configurationHandler.getPathToLocalConfigurationFolder().toString());
	}
	
	@Test
	public void theLocalConfFolderShouldExist() {
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl();
		assertTrue(configurationHandler.getPathToLocalConfigurationFolder().toFile().isDirectory());
	}
	
	@Test
	public void shouldReturnEmptyListWhenRepoConfIsEmpty() {
		assertTrue(configurationHandler.getRepositoryConfigurationNames().isEmpty());
	}

	@Test
	public void shouldReturnOneNameWhenRepoConfContainsOneFile() throws IOException {
		Path repo1 = bitMagGUI.resolve("repoConf").resolve("repo1.conf");
		repo1.toFile().createNewFile();
		assertEquals(1, configurationHandler.getRepositoryConfigurationNames().size());
		assertEquals("repo1", configurationHandler.getRepositoryConfigurationNames().get(0));
	}

	@Test
	public void shouldReturnTwoNamesWhenRepoConfContainsTwoFiles() throws IOException {
		Path repo1 = bitMagGUI.resolve("repoConf").resolve("repo1.conf");
		Path repo2 = bitMagGUI.resolve("repoConf").resolve("repo2.conf");
		repo1.toFile().createNewFile();
		repo2.toFile().createNewFile();
		assertEquals(2, configurationHandler.getRepositoryConfigurationNames().size());
		assertTrue("repo1", configurationHandler.getRepositoryConfigurationNames().contains("repo1"));
		assertTrue("repo2", configurationHandler.getRepositoryConfigurationNames().contains("repo2"));
	}


	@Test
	public void shouldReturnEmptyListWhenThereAreNoRepoConfsInTheRepoConfFolder() throws IOException, InvalidArgumentException {
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(bitMagGUI);
		assertTrue(configurationHandler.getRepositoryConfigurations().isEmpty());
	}
	
	@Test
	public void shouldReturnOneRepoConfWhenThereIsOneRepoConfInTheRepoConfFolder() throws IOException, InvalidArgumentException {

		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(bitMagGUI);
		// repoConf folder now created

		setUpRepositoryConfigurations();

		ConfigurationIOHandler configurationIOHandler = new ConfigurationIOHandlerImpl(configurationHandler);
		configurationIOHandler.writeRepositoryConfiguration(repositoryConfiguration2);

		assertEquals(1, configurationHandler.getRepositoryConfigurations().size());
		assertNotNull(configurationHandler.getRepositoryConfiguration("repo"));
	}

	@Test
	public void shouldReturnTwoRepoConfWhenThereAreTwoRepoConfInTheRepoConfFolder() throws IOException, InvalidArgumentException {

		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(bitMagGUI);
		// repoConf folder now created
		
		setUpRepositoryConfigurations();
		
		ConfigurationIOHandler configurationIOHandler = new ConfigurationIOHandlerImpl(configurationHandler);
		configurationIOHandler.writeRepositoryConfiguration(repositoryConfiguration2);
		configurationIOHandler.writeRepositoryConfiguration(repositoryConfiguration3);

		assertEquals(2, configurationHandler.getRepositoryConfigurations().size());
		assertNotNull(configurationHandler.getRepositoryConfiguration("repo"));
		assertNotNull(configurationHandler.getRepositoryConfiguration("repo2"));
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
