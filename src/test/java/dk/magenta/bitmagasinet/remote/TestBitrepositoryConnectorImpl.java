package dk.magenta.bitmagasinet.remote;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;
import dk.magenta.bitmagasinet.configuration.RepositoryConfigurationImpl;

public class TestBitrepositoryConnectorImpl {

	@Ignore
	@Test
	public void shouldGetSaltedChecksumForFile() throws Exception {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("staging");
		repositoryConfiguration.setCollectionId("2");
		repositoryConfiguration.setPathToSettingsFiles(Paths.get("/home/andreas/bitmagasinet/bitrepository-client-1.6/conf"));
		repositoryConfiguration.setPathToCertificate(Paths.get("/home/andreas/bitmagasinet/bitrepository-client-1.6/conf/rigsark-store-client-certkey.pem"));
		repositoryConfiguration.setPillarId("rigsarkivnearline1");
		
		FileChecksum fileChecksum = new FileChecksumImpl("Something.txt", "474cc7f5020f952447044e93438d0ea6", "64");
		
		BitrepositoryConnector connector = new BitrepositoryConnectorImpl(repositoryConfiguration);
		assertEquals("474cc7f5020f952447044e93438d0ea6", connector.getRemoteChecksum(fileChecksum));
		
		connector.closeMessageBus();
	}
}
