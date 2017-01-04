package dk.magenta.bitmagasinet;

import java.util.List;

import dk.magenta.bitmagasinet.configuration.ConfigurationHandler;

public class GUIFacadeImpl implements GUIFacade {

	private ConfigurationHandler configurationHandler;
	
	public GUIFacadeImpl(ConfigurationHandler configurationHandler) {
		this.configurationHandler = configurationHandler;
	}
	
	@Override
	public List<String> getRepositoryConfigurationNames() {
		return configurationHandler.getRepositoryConfigurationNames();
	}

}
