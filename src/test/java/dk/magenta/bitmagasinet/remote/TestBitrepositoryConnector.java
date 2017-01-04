package dk.magenta.bitmagasinet.remote;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;
import dk.magenta.bitmagasinet.configuration.InvalidArgumentException;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;
import dk.magenta.bitmagasinet.configuration.RepositoryConfigurationImpl;

public class TestBitrepositoryConnector implements ThreadStatusObserver {

	private BitrepositoryConnectionResult bitrepositoryConnectionResult;
	
	@Ignore
	@Before
	public void setUp() throws InvalidArgumentException {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("staging");
		repositoryConfiguration.setCollectionId("2");
		repositoryConfiguration.setPathToSettingsFiles(Paths.get("/home/andreas/bitmagasinet/bitrepository-client-1.6/conf"));
		repositoryConfiguration.setPathToCertificate(Paths.get("/home/andreas/bitmagasinet/bitrepository-client-1.6/conf/rigsark-store-client-certkey.pem"));
		repositoryConfiguration.setPillarId("rigsarkivnearline1");
		
		FileChecksum fileChecksum = new FileChecksumImpl("Something.txt", "474cc7f5020f952447044e93438d0ea6", "64");
		
		BitrepositoryConnectorImpl connector = new BitrepositoryConnectorImpl(repositoryConfiguration, fileChecksum);
		connector.addObserver(this);
		
		Thread t = new Thread(connector);
		t.start();
	}
	
	@Ignore
	@Test
	public void shouldGetSaltedChecksumForFile() throws Exception {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		
		assertEquals(ThreadStatus.SUCCESS, bitrepositoryConnectionResult.getStatus());
		assertEquals("474cc7f5020f952447044e93438d0ea6", bitrepositoryConnectionResult.getFileChecksum().getRemoteChecksum());
	}
	
	@Override
	public void update(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		this.bitrepositoryConnectionResult = bitrepositoryConnectionResult;
	}
	
	@Override
	public void messageBusErrorCallback() {
	}
}
