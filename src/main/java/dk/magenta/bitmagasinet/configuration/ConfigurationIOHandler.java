package dk.magenta.bitmagasinet.configuration;

import java.io.IOException;

interface ConfigurationIOHandler {

	RepositoryConfiguration readRepositoryConfiguration(String name) throws IOException, InvalidArgumentException;
	
	/**
	 * Pre-condition: repositoryConfiguration must not be null
	 * @param repositoryConfiguration
	 */
	void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IOException;
}
