package dk.magenta.bitmagasinet;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import dk.magenta.bitmagasinet.configuration.ConfigurationHandlerImpl;

public class TestGUIFacadeImpl {

	@Test
	public void shouldReturnTwoRepoConfsWhenTwoFilesInRepoConf() throws IOException {
		Path bitMagGUI = Paths.get(System.getProperty("java.io.tmpdir")).resolve("BitMagGUI");
		GUIFacade guiFacade = new GUIFacadeImpl(new ConfigurationHandlerImpl(bitMagGUI));
		
		Path repo1 = bitMagGUI.resolve("repoConf").resolve("repo1.conf");
		Path repo2 = bitMagGUI.resolve("repoConf").resolve("repo2.conf");
		repo1.toFile().createNewFile();
		repo2.toFile().createNewFile();
		
		assertEquals(2, guiFacade.getRepositoryConfigurationNames().size());
		assertTrue("repo1", guiFacade.getRepositoryConfigurationNames().contains("repo1"));
		assertTrue("repo2", guiFacade.getRepositoryConfigurationNames().contains("repo2"));

		FileUtils.deleteDirectory(bitMagGUI.toFile());
		
	}
	
}
