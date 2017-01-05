package dk.magenta.bitmagasinet;

import java.io.IOException;
import java.util.List;

import dk.magenta.bitmagasinet.configuration.InvalidArgumentException;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;

public interface GUIFacade {
	
	public List<String> getRepositoryConfigurationNames() throws IOException, InvalidArgumentException;
	public void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IOException;
}
