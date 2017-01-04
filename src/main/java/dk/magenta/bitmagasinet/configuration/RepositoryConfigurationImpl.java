package dk.magenta.bitmagasinet.configuration;

import java.io.File;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class RepositoryConfigurationImpl implements RepositoryConfiguration {

	private String name;
	private String collectionId;
	private Path certificateFile;
	private Path checksumListFile;
	private Path settingsFolder;
	private String pillarId;
	
	public RepositoryConfigurationImpl(String name) throws InvalidArgumentException {
		setName(name);
	}

	@Override
	public String getCollectionId() {
		return collectionId;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Path getPathToCertificate() {
		return certificateFile;
	}

	@Override
	public Path getPathToChecksumList() {
		return checksumListFile;
	}

	@Override
	public Path getPathToSettingsFiles() {
		return settingsFolder;
	}

	@Override
	public String getPillarId() {
		return pillarId;
	}

	@Override
	public void setCollectionId(String collectionId) throws InvalidArgumentException {
		if (StringUtils.isBlank(collectionId)) {
			throw new InvalidArgumentException("CollectionID må ikke være blank");
		}
		this.collectionId = collectionId.trim();
	}

	@Override
	public void setName(String name) throws InvalidArgumentException {
		throwExceptionIfStringBlank(name, "Navn må ikke være blank");
		this.name = name.trim();
	}

	@Override
	public void setPathToCertificate(Path path) throws InvalidArgumentException {
		checkIfPathIsFile(path);
		if (!FilenameUtils.getExtension(path.toString().toLowerCase()).equals("pem")) {
			throw new InvalidArgumentException("Filen er ikke et PEM certifikat");
		}
		certificateFile = path;
	}

	@Override
	public void setPathToChecksumList(Path path) throws InvalidArgumentException {
		checkIfPathIsFile(path);
		checksumListFile = path;
	}

	@Override
	public void setPathToSettingsFiles(Path path) throws InvalidArgumentException {
		if (!path.toFile().isDirectory()) {
			throw new InvalidArgumentException("Stien henviser ikke til en mappe");
		}
		File repositorySettings = path.resolve("RepositorySettings.xml").toFile();
		File referenceSettings = path.resolve("ReferenceSettings.xml").toFile();
		if (!repositorySettings.exists() || !referenceSettings.exists()) {
			throw new InvalidArgumentException("Mappen indeholder ikke RepositorySettings.xml og/eller ReferenceSettings.xml");
		}
		settingsFolder = path;
	}

	@Override
	public void setPillarId(String pillarId) throws InvalidArgumentException {
		throwExceptionIfStringBlank(pillarId, "PillarID må ikke være blank");
		this.pillarId = pillarId.trim();
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) return true;
		if (otherObject == null) return false;
		if (getClass() != otherObject.getClass()) return false;
		
		RepositoryConfiguration other = (RepositoryConfiguration) otherObject;
		
		if (!getCollectionId().equals(other.getCollectionId())) return false;
		if (!getPathToCertificate().equals(other.getPathToCertificate())) return false;
		if (!getName().equals(other.getName())) return false;
		if (!getPathToChecksumList().equals(other.getPathToChecksumList())) return false;
		if (!getPathToSettingsFiles().equals(other.getPathToSettingsFiles())) return false;
		if (!getPillarId().equals(other.getPillarId())) return false;
		
		return true;
	}

	private void checkIfPathIsFile(Path path) throws InvalidArgumentException {
		if (!path.toFile().isFile()) {
			throw new InvalidArgumentException("Stien henviser ikke til en fil");
		}
	}
	
	private void throwExceptionIfStringBlank(String s, String message) throws InvalidArgumentException {
		if (StringUtils.isBlank(s)) {
			throw new InvalidArgumentException(message);
		}
	}
}
