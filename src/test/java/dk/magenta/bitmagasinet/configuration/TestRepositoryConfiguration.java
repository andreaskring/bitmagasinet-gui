package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;
import dk.magenta.bitmagasinet.configuration.RepositoryConfigurationImpl;

public class TestRepositoryConfiguration {

	@Test
	public void shouldStoreItsName() {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("name");
		assertEquals("name", repositoryConfiguration.getName());
	}
}

