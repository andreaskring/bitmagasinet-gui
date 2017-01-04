package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
	private Path bitMagGUI;

	@Before
	public void setUp() {
		bitMagGUI = Paths.get(System.getProperty("java.io.tmpdir")).resolve("BitMagGUI");
		configurationHandler = new ConfigurationHandlerImpl(bitMagGUI);
		repositoryConfiguration1 = new RepositoryConfigurationImpl("r1");
	}
	
	@After
	public void tearDown() throws IOException {
		FileUtils.deleteDirectory(bitMagGUI.toFile());
	}
	
	@Test
	public void shouldGetRepositoryConfigurationWithNameR1() {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		assertEquals("r1", configurationHandler.getRepositoryConfiguration("r1").getName());
	}
	
	@Test
	public void shouldGetRepositoryConfigurationWithNameR2() {
		configurationHandler.addRepositoryConfiguration(new RepositoryConfigurationImpl("r2"));
		assertEquals("r2", configurationHandler.getRepositoryConfiguration("r2").getName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAllowNullName() {
		configurationHandler.getRepositoryConfiguration(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAllowEmptykName() {
		configurationHandler.getRepositoryConfiguration("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotAllowBlankName() {
		configurationHandler.getRepositoryConfiguration(" ");
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenAddedTwoRepositoryConfigurationsWithTheSameName() {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
	}
	
	@Test
	public void shouldReturnFalseWhenRepoConfigNotInMap() {
		assertFalse(configurationHandler.containsRepositoryConfiguration("r1"));
	}

	@Test
	public void shouldReturnTrueWhenRepoConfigIsInMap() {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		assertTrue(configurationHandler.containsRepositoryConfiguration("r1"));
	}
	
	@Test
	public void shouldDeleteRepositoryConfiguration() {
		configurationHandler.addRepositoryConfiguration(repositoryConfiguration1);
		configurationHandler.deleteRepositoryConfiguration(repositoryConfiguration1.getName());
		assertFalse(configurationHandler.containsRepositoryConfiguration(repositoryConfiguration1.getName()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenTryingToDeleteUnknownRepoConfig() {
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
	
}
