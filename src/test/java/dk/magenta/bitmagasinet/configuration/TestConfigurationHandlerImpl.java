package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileSystemUtils;
import org.junit.Before;
import org.junit.Test;

public class TestConfigurationHandlerImpl {

	private ConfigurationHandler configurationHandler;
	private RepositoryConfiguration repositoryConfiguration1;

	@Before
	public void setUp() {
		configurationHandler = new ConfigurationHandlerImpl(null);
		repositoryConfiguration1 = new RepositoryConfigurationImpl("r1");
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
	public void shouldReturn_tmp_WhenPathToLocalConfIs_tmp() {
		Path localeConfigurationFolder = FileSystems.getDefault().getPath("/tmp");
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(localeConfigurationFolder);
		assertEquals(localeConfigurationFolder.toString(), configurationHandler.getPathToLocalConfigurationFolder().toString());
	}

	@Test
	public void shouldReturn_tmp_a_WhenPathToLocalConfIs_tmp_a() {
		Path path = Paths.get("/tmp/a");
		Path localeConfigurationFolder = FileSystems.getDefault().getPath(path.toString());
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(localeConfigurationFolder);
		assertEquals(path.toString(), configurationHandler.getPathToLocalConfigurationFolder().toString());
	}
	
	@Test
	public void shouldReturnRepoConfAsNameOfRepoConfFolder() {
		Path tmp = Paths.get("tmp");
		Path repoConf = Paths.get("tmp/repoConf");
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(tmp);
		assertEquals(repoConf.toString(), configurationHandler.getPathToRepositoryConfigurations().toString());
	}
	
	@Test
	public void shouldHaveLocalConfFolderProgramFiles() {
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl();
		Path path = Paths.get("/Program Files/BitMagGUI");
		assertEquals(path.toString(), configurationHandler.getPathToLocalConfigurationFolder().toString());
	}
	
	@Test
	public void theLocalConfFolderShouldExist() {
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl();
		assertTrue(configurationHandler.getPathToLocalConfigurationFolder().toFile().isDirectory());
	}
}
