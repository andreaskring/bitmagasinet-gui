package dk.magenta.bitmagasinet.configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface ConfigurationHandler {

	public RepositoryConfiguration getRepositoryConfiguration(String name) throws InvalidArgumentException;
	
	/**
	 * Get the RepositoryConfigurations from the repoConf folder
	 * @return
	 * @throws InvalidArgumentException 
	 * @throws IOException 
	 */
	public Map<String, RepositoryConfiguration> getRepositoryConfigurations() throws IOException, InvalidArgumentException;
	
	/**
	 * Get the RepositoryConfiguration names (i.e. the file basename (before .conf)) from the repoConf folder 
	 * @return
	 * @throws InvalidArgumentException 
	 * @throws IOException 
	 */
	public List<String> getRepositoryConfigurationNames() throws IOException, InvalidArgumentException;
	
	/**
	 * Pre-condition: repositoryConfiguration must not be null
	 * @param repositoryConfiguration
	 * @throws IllegalArgumentException if the name of the repositoryConfiguration already exists
	 * @throws InvalidArgumentException 
	 */
	public void addRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws InvalidArgumentException;
	
	public boolean containsRepositoryConfiguration(String name);
	
	/**
	 * 
	 * @param name
	 * @throws IllegalArgumentException if the name does not exists
	 * @throws InvalidArgumentException 
	 */
	public void deleteRepositoryConfiguration(String name) throws InvalidArgumentException;
	
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
