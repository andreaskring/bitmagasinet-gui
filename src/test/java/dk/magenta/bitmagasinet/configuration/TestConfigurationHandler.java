package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestConfigurationHandler {

	@Test
	public void shouldGetRepositoryConfigurationWithNameR1() {
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(null);
		assertEquals("r1", configurationHandler.getRepositoryConfiguration("r1").getName());
	}
	
	@Test
	public void shouldGetRepositoryConfigurationWithNameR2() {
		ConfigurationHandler configurationHandler = new ConfigurationHandlerImpl(null);
		assertEquals("r2", configurationHandler.getRepositoryConfiguration("r2").getName());
	}

}
