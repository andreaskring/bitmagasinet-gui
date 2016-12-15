package dk.magenta.bitmagasinet.configuration;

import java.nio.file.Path;

interface RepositoryConfiguration {

	/**
	 * Get the collectionID
	 * @return the collectionID
	 */
	String getCollectionId();
	
	/**
	 * Get the name of the repository configuration
	 * @return the name of the repository configuration
	 */
	String getName();
	
	/**
	 * Get the path to the PEM certificate used to connect to the bitrepository
	 * @return the path to the certificate
	 */
	Path getPathToCertificate();
	
	/**
	 * Get the path to the file containing the checksum list
	 * @return the path to the file containing the checksum list
	 */
	Path getPathToChecksumList();
	
	/**
	 * Get the path to the RepositorySettings.xml and ReferenceSettings.xml 
	 * @return the path to the RepositorySettings.xml and ReferenceSettings.xml
	 */
	Path getPathToSettingsFiles();
	
	/**
	 * Get the ID of the pillar to calculate checksums from
	 * @return the ID of the pillar
	 */
	String getPillarId();
	
	/**
	 * Set the collectionID
	 * @param collectionId The collectionID
	 */
	void setCollectionId(String collectionId) throws IllegalArgumentException;
	
	/**
	 * Set the name of the repository configuration
	 * @param name The name of the repository configuration
	 */
	void setName(String name) throws IllegalArgumentException;
	
	/**
	 * Set the path to the PEM certificate used to connect to the bitrepository
	 * @param path The path to the certificate
	 */
	void setPathToCertificate(Path path) throws IllegalArgumentException ;
	
	/**
	 * Set the path to the file containing the checksum list
	 * @param path The path to the file containing the checksum list
	 */
	void setPathToChecksumList(Path path);
	
	/**
	 * Set the path to the RepositorySettings.xml and ReferenceSettings.xml
	 * @param path The path to the RepositorySettings.xml and ReferenceSettings.xml
	 */
	void setPathToSettingsFiles(Path path);

	/**
	 * Set the pillar ID
	 * @param pillarId the pillar ID
	 */
	void setPillarId(String pillarId);
}
