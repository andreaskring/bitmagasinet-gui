package dk.magenta.bitmagasinet;

import java.io.IOException;
import java.util.List;

import dk.magenta.bitmagasinet.configuration.ConfigurationHandler;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;

public class GUIFacadeImpl implements GUIFacade {

	private ConfigurationHandler configurationHandler;
	
	public GUIFacadeImpl(ConfigurationHandler configurationHandler) {
		this.configurationHandler = configurationHandler;
	}
	
	@Override
	public List<String> getRepositoryConfigurationNames() {
		return configurationHandler.getRepositoryConfigurationNames();
	}

	@Override
	public void writeRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
