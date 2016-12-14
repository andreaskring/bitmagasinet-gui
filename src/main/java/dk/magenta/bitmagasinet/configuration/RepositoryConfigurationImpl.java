package dk.magenta.bitmagasinet.configuration;

import java.nio.file.Path;

class RepositoryConfigurationImpl implements RepositoryConfiguration {

	private String name;
	private String collectionId;
	private Path certificateFile;
	private Path checksumListFile;
	private Path settingsFolder;
	private String pillarId;
	
	RepositoryConfigurationImpl(String name) {
		this.name = name;
	}

	@Override
	public String getCollectionId() {
		return collectionId;
	}

	@Override
	public String getName() {
		return name;
	}

	public Path getPathToCertificate() {
		return certificateFile;
	}

	public Path getPathToChecksumList() {
		return checksumListFile;
	}

	public Path getPathToSettingsFiles() {
		return settingsFolder;
	}

	public String getPillarId() {
		return pillarId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPathToCertificate(Path path) {
		certificateFile = path;
	}

	public void setPathToChecksumList(Path path) {
		checksumListFile = path;
	}

	public void setPathToSettingsFiles(Path path) {
		settingsFolder = path;
	}

	public void setPillarId(String pillarId) {
		this.pillarId = pillarId;
	}

}
