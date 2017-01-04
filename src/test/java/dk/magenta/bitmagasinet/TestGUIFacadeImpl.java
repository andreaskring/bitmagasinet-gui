package dk.magenta.bitmagasinet;

import static org.junit.Assert.assertEquals;

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
		assertEquals("repo1", guiFacade.getRepositoryConfigurationNames().get(0));
		assertEquals("repo2", guiFacade.getRepositoryConfigurationNames().get(1));

		FileUtils.deleteDirectory(bitMagGUI.toFile());
		
	}
	
}
