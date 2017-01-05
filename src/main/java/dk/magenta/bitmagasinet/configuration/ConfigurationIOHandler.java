package dk.magenta.bitmagasinet.configuration;

import java.io.IOException;
import java.nio.file.Path;

public interface ConfigurationIOHandler {

	public RepositoryConfiguration readRepositoryConfiguration(String name) throws IOException, InvalidArgumentException;
	
	public RepositoryConfiguration readRepositoryConfiguration(Path path) throws IOException, InvalidArgumentException;
	
	/**
	 * Pre-condition: repositoryConfiguration must not be null
	 * @param repositoryConfiguration
	 */
	public void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IOException;
}
