package dk.magenta.bitmagasinet.configuration;

public interface ConfigurationHandler {

	public RepositoryConfiguration getRepositoryConfiguration(String name);
	
	public void addRepositoryConfiguration(String name);
	
	public void deleteRepositoryConfiguration(String name);
	
}
