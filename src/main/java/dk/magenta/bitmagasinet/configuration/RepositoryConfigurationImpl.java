package dk.magenta.bitmagasinet.configuration;

import java.io.File;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

class RepositoryConfigurationImpl implements RepositoryConfiguration {

	private String name;
	private String collectionId;
	private Path certificateFile;
	private Path checksumListFile;
	private Path settingsFolder;
	private String pillarId;
	
	RepositoryConfigurationImpl(String name) throws IllegalArgumentException {
		setName(name);;
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

	public void setCollectionId(String collectionId) throws IllegalArgumentException {
		if (StringUtils.isBlank(collectionId)) {
			throw new IllegalArgumentException("CollectionID må ikke være blank");
		}
		this.collectionId = collectionId.trim();
	}

	public void setName(String name) throws IllegalArgumentException {
		throwExceptionIfStringBlank(name, "Navn må ikke være blank");
		this.name = name.trim();
	}

	public void setPathToCertificate(Path path) throws IllegalArgumentException {
		checkIfPathIsFile(path);
		if (!FilenameUtils.getExtension(path.toString().toLowerCase()).equals("pem")) {
			throw new IllegalArgumentException("Filen er ikke et PEM certifikat");
		}
		certificateFile = path;
	}

	public void setPathToChecksumList(Path path) {
		checkIfPathIsFile(path);
		checksumListFile = path;
	}

	public void setPathToSettingsFiles(Path path) throws IllegalArgumentException {
		if (!path.toFile().isDirectory()) {
			throw new IllegalArgumentException("Stien henviser ikke til en mappe");
		}
		File repositorySettings = path.resolve("RepositorySettings.xml").toFile();
		File referenceSettings = path.resolve("ReferenceSettings.xml").toFile();
		if (!repositorySettings.exists() || !referenceSettings.exists()) {
			throw new IllegalArgumentException("Mappen indeholder ikke RepositorySettings.xml og/eller ReferenceSettings.xml");
		}
		settingsFolder = path;
	}

	public void setPillarId(String pillarId) {
		throwExceptionIfStringBlank(pillarId, "PillarID må ikke være blank");
		this.pillarId = pillarId.trim();
	}

	private void checkIfPathIsFile(Path path) throws IllegalArgumentException {
		if (!path.toFile().isFile()) {
			throw new IllegalArgumentException("Stien henviser ikke til en fil");
		}
	}
	
	private void throwExceptionIfStringBlank(String s, String message) throws IllegalArgumentException {
		if (StringUtils.isBlank(s)) {
			throw new IllegalArgumentException(message);
		}
	}
}
