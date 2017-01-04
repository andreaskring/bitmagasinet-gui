package dk.magenta.bitmagasinet.configuration;

import java.io.IOException;
import java.nio.file.Path;

interface ConfigurationIOHandler {

	RepositoryConfiguration readRepositoryConfiguration(String name) throws IOException, InvalidArgumentException;
	
	RepositoryConfiguration readRepositoryConfiguration(Path path) throws IOException, InvalidArgumentException;
	
	/**
	 * Pre-condition: repositoryConfiguration must not be null
	 * @param repositoryConfiguration
	 */
	void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IOException;
}
