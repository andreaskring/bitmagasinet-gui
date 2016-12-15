package dk.magenta.bitmagasinet.configuration;

import java.nio.file.Path;

public interface PathCheckerStrategy {
	
	public boolean isFile(Path path);
	
	public boolean isDirectory(Path path);
	
	/**
	 * Check if the path contains the RepositorySetting.xml and the ReferenceSettings.xml
	 * @param path
	 * @return
	 */
	public boolean containsSettingsFiles(Path path);
}
