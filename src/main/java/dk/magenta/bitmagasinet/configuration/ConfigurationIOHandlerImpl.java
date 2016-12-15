package dk.magenta.bitmagasinet.configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import dk.magenta.bitmagasinet.Constants;

class ConfigurationIOHandlerImpl implements ConfigurationIOHandler {

	private ConfigurationHandler configurationHandler;
	private Properties properties;
	
	public ConfigurationIOHandlerImpl(ConfigurationHandler configurationHandler) {
		this.configurationHandler = configurationHandler;
	}
	
	@Override
	public RepositoryConfiguration readRepositoryConfiguration(String name) throws IOException {

		Path path = configurationHandler.getPathToRepositoryConfigurations().resolve(name + Constants.REPOFILE_EXT);
		try {
			FileReader reader = new FileReader(path.toFile());
			properties.load(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} 
		return PropertiesHandler.convertPropertiesToRepositoryConfiguration(properties);
	}


	@Override
	public void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IOException {

		properties = PropertiesHandler.convertRepositoryConfigurationToProperties(repositoryConfiguration);
		
		Path pathToFolder = configurationHandler.getPathToRepositoryConfigurations();
		Path pathToFile = pathToFolder.resolve(repositoryConfiguration.getName() + Constants.REPOFILE_EXT);
		if (!pathToFolder.toFile().isDirectory()) {
			Files.createDirectory(pathToFolder);
		}
		try {
			FileWriter writer = new FileWriter(pathToFile.toFile());
			properties.store(writer, Constants.DO_NOT_EDIT_FILE);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} 
		

	}

}
