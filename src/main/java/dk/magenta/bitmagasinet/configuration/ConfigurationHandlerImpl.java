package dk.magenta.bitmagasinet.configuration;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class ConfigurationHandlerImpl implements ConfigurationHandler {

	private Map<String, RepositoryConfiguration> repositoryMap;
	private Path localeConfigurationFolder;
	
	public ConfigurationHandlerImpl(Path localeConfigurationFolder) {
		this.localeConfigurationFolder = localeConfigurationFolder;
		repositoryMap = new TreeMap<String, RepositoryConfiguration>();
	}
	
	@Override
	public Path getPathToLocalConfigurationFolder() {
		return localeConfigurationFolder;
	}
	
	@Override
	public RepositoryConfiguration getRepositoryConfiguration(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Navn på konfiguration må ikke være blank");
		}
		return repositoryMap.get(name);
	}

	@Override
	public void addRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IllegalArgumentException {
		if (repositoryMap.containsKey(repositoryConfiguration.getName())) {
			throw new IllegalArgumentException("Konfiguration med dette navn findes allerede");
		}
		repositoryMap.put(repositoryConfiguration.getName(), repositoryConfiguration);
	}

	@Override
	public void deleteRepositoryConfiguration(String name) throws IllegalArgumentException {
		if (!containsRepositoryConfiguration(name)) {
			throw new IllegalArgumentException("Ingen konfiguration med dette navn");
		}
		repositoryMap.remove(name);
	}

	@Override
	public boolean containsRepositoryConfiguration(String name) {
		return repositoryMap.containsKey(name);
	}

}
