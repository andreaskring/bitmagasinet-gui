package dk.magenta.bitmagasinet.configuration;

import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesHandler {

	public static Properties convertRepositoryConfigurationToProperties(RepositoryConfiguration repositoryConfiguration) {
		Properties properties = new Properties();
		properties.setProperty("collectionID", repositoryConfiguration.getCollectionId());
		properties.setProperty("name", repositoryConfiguration.getName());
		properties.setProperty("pathToCertificate", repositoryConfiguration.getPathToCertificate().toString());
		properties.setProperty("pathToChecksumList", repositoryConfiguration.getPathToChecksumList().toString());
		properties.setProperty("pathToSettingsFiles", repositoryConfiguration.getPathToSettingsFiles().toString());
		properties.setProperty("pillarID", repositoryConfiguration.getPillarId().toString());
		return properties;
	}

	public static RepositoryConfiguration convertPropertiesToRepositoryConfiguration(Properties properties) throws InvalidArgumentException {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl(properties.getProperty("name"));
		repositoryConfiguration.setCollectionId(properties.getProperty("collectionID"));
		repositoryConfiguration.setPathToCertificate(Paths.get(properties.getProperty("pathToCertificate")));
		repositoryConfiguration.setPathToChecksumList(Paths.get(properties.getProperty("pathToChecksumList")));
		repositoryConfiguration.setPathToSettingsFiles(Paths.get(properties.getProperty("pathToSettingsFiles")));
		repositoryConfiguration.setPillarId(properties.getProperty("pillarID"));
		return repositoryConfiguration;
	}
}
