package dk.magenta.bitmagasinet.configuration;

interface ConfigurationIOHandler {

	RepositoryConfiguration readRepositoryConfiguration(String name);
	
	void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration);
}
