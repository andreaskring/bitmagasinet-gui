package dk.magenta.bitmagasinet.configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import dk.magenta.bitmagasinet.Constants;

public class ConfigurationHandlerImpl implements ConfigurationHandler {

	private Map<String, RepositoryConfiguration> repositoryMap;
	private Path localConfigurationFolder;
	
	public ConfigurationHandlerImpl() {
		System.out.println(Paths.get(System.getProperty("user.home")).resolve(Constants.LOCAL_CONFIGURATION_FOLDER));
		localConfigurationFolder = Paths.get(System.getProperty("user.home")).resolve(Constants.LOCAL_CONFIGURATION_FOLDER);
		setUpFoldersAndRepositoryMap();
	}
	
	public ConfigurationHandlerImpl(Path localeConfigurationFolder) {
		this.localConfigurationFolder = localeConfigurationFolder;
		setUpFoldersAndRepositoryMap();
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
	public List<String> getRepositoryConfigurationNames() {
		File[] repoConf = getPathToRepositoryConfigurations().toFile().listFiles();
		List<String> repoFiles = new ArrayList<String>();
		for (File file : repoConf) {
			String filename = file.toString();
			if (file.isFile() && FilenameUtils.getExtension(filename).equals("conf")) {
				repoFiles.add(FilenameUtils.getBaseName(filename));
			}
		}
		return repoFiles;
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

	private void setUpFoldersAndRepositoryMap() {
		repositoryMap = new TreeMap<String, RepositoryConfiguration>();
		File folder = localConfigurationFolder.toFile();
		if (!folder.isDirectory()) {
			folder.mkdir();
			Path repoConf = localConfigurationFolder.resolve(Constants.REPOCONF_FOLDER);
			repoConf.toFile().mkdir();
		}
		
	}
	
}
