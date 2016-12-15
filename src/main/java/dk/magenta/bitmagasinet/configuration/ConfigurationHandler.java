package dk.magenta.bitmagasinet.configuration;

import java.nio.file.Path;

public interface ConfigurationHandler {

	public RepositoryConfiguration getRepositoryConfiguration(String name);
	
	/**
	 * Pre-condition: repositoryConfiguration must not be null
	 * @param repositoryConfiguration
	 * @throws IllegalArgumentException if the name of the repositoryConfiguration already exists
	 */
	public void addRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IllegalArgumentException;
	
	public boolean containsRepositoryConfiguration(String name);
	
	/**
	 * 
	 * @param name
	 * @throws IllegalArgumentException if the name does not exists
	 */
	public void deleteRepositoryConfiguration(String name) throws IllegalArgumentException;
	
	/**
	 * Get the path to the folder containing the local configuration files 
	 * @return
	 */
	public Path getPathToLocalConfigurationFolder();
	
	/**
	 * Get the path to the repository configurations within the local configuration folder
	 * @return
	 */
	public Path getPathToRepositoryConfigurations();
}
