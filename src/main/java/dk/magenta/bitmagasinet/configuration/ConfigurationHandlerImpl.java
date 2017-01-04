package dk.magenta.bitmagasinet.configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import dk.magenta.bitmagasinet.Constants;

public class ConfigurationHandlerImpl implements ConfigurationHandler {

	private Map<String, RepositoryConfiguration> repositoryMap;
	private Path localConfigurationFolder;
	
	public ConfigurationHandlerImpl() {
		repositoryMap = new TreeMap<String, RepositoryConfiguration>();
		localConfigurationFolder = Paths.get(Constants.LOCAL_CONFIGURATION_FOLDER);
		File folder = localConfigurationFolder.toFile();
		if (!folder.isDirectory()) {
			folder.mkdir();
			Path repoConf = localConfigurationFolder.resolve(Constants.REPOCONF_FOLDER);
			repoConf.toFile().mkdir();
		}
	}
	
	public ConfigurationHandlerImpl(Path localeConfigurationFolder) {
		this.localConfigurationFolder = localeConfigurationFolder;
		repositoryMap = new TreeMap<String, RepositoryConfiguration>();
	}
	
	@Override
	public Path getPathToLocalConfigurationFolder() {
		return localConfigurationFolder;
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
	
	@Override
	public Path getPathToRepositoryConfigurations() {
		return getPathToLocalConfigurationFolder().resolve(Constants.REPOCONF_FOLDER);
	}
}
