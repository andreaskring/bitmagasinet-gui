package dk.magenta.bitmagasinet.configuration;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class ConfigurationHandlerImpl implements ConfigurationHandler {

	private Map<String, RepositoryConfiguration> repositoryMap;
	
	public ConfigurationHandlerImpl(Path localeConfigurationFolder) {
		repositoryMap = new TreeMap<String, RepositoryConfiguration>();
		repositoryMap.put("r1", new RepositoryConfigurationImpl("r1"));
		repositoryMap.put("r2", new RepositoryConfigurationImpl("r2"));
	}
	
	@Override
	public RepositoryConfiguration getRepositoryConfiguration(String name) {
		return repositoryMap.get(name);
	}

	@Override
	public void addRepositoryConfiguration(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRepositoryConfiguration(String name) {
		// TODO Auto-generated method stub

	}

}
