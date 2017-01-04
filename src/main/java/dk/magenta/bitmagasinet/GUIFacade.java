package dk.magenta.bitmagasinet;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;

public interface GUIFacade {
	
	public List<String> getRepositoryConfigurationNames();
	public void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IOException;
}
