package dk.magenta.bitmagasinet.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import dk.magenta.bitmagasinet.gui.Constants;

public class ConfigurationHandlerImpl implements ConfigurationHandler {

	private Map<String, RepositoryConfiguration> repositoryMap;
	private Path localConfigurationFolder;
	private ConfigurationIOHandler configurationIOHandler;
	
	public ConfigurationHandlerImpl() {
		localConfigurationFolder = Paths.get(System.getProperty("user.home")).resolve(Constants.LOCAL_CONFIGURATION_FOLDER);
		setUpFoldersAndRepositoryMapAndIOHandler();
	}
	
	public ConfigurationHandlerImpl(Path localeConfigurationFolder) {
		this.localConfigurationFolder = localeConfigurationFolder;
		setUpFoldersAndRepositoryMapAndIOHandler();
	}
	
	@Override
	public Path getPathToLocalConfigurationFolder() {
		return localConfigurationFolder;
	}
	
	@Override
	public RepositoryConfiguration getRepositoryConfiguration(String name) throws InvalidArgumentException {
		if (StringUtils.isBlank(name)) {
			throw new InvalidArgumentException("Navn på konfiguration må ikke være blank");
		}
		return repositoryMap.get(name);
	}
	
	@Override
	public Map<String, RepositoryConfiguration> getRepositoryConfigurationsFromFolder() throws IOException, InvalidArgumentException {
		File[] repoConf = getPathToRepositoryConfigurations().toFile().listFiles();
		for (File file : repoConf) {
			String filename = file.toString();
			if (file.isFile() && FilenameUtils.getExtension(filename).equals("conf")) {
				String repoName = FilenameUtils.getBaseName(filename);
				RepositoryConfiguration repositoryConfiguration = configurationIOHandler.readRepositoryConfiguration(repoName);
				repositoryMap.put(repoName, repositoryConfiguration);
			}
		}
		return repositoryMap;
	}

	@Override
	public Map<String, RepositoryConfiguration> getRepositoryConfigurations() {
		return repositoryMap;
	}
	
	@Override
	public List<String> getRepositoryConfigurationNames() throws IOException, InvalidArgumentException {
		getRepositoryConfigurationsFromFolder();
		List<String> names = new ArrayList<String>();
		names.addAll(repositoryMap.keySet());
		return names;
	}

	
	@Override
	public void addRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) {
		repositoryMap.put(repositoryConfiguration.getName(), repositoryConfiguration);
	}

	@Override
	public void deleteRepositoryConfiguration(String name) throws InvalidArgumentException {
		if (!containsRepositoryConfiguration(name)) {
			throw new InvalidArgumentException("Ingen konfiguration med dette navn");
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

	private void setUpFoldersAndRepositoryMapAndIOHandler() {
		configurationIOHandler = new ConfigurationIOHandlerImpl(this);
		repositoryMap = new TreeMap<String, RepositoryConfiguration>();
		File folder = localConfigurationFolder.toFile();
		if (!folder.isDirectory()) {
			folder.mkdir();
			Path repoConf = localConfigurationFolder.resolve(Constants.REPOCONF_FOLDER);
			repoConf.toFile().mkdir();
		}
		
	}
	
}
