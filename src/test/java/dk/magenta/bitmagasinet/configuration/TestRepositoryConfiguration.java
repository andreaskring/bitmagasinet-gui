package dk.magenta.bitmagasinet.configuration;

import static org.junit.Assert.assertEquals;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

public class TestRepositoryConfiguration {

	private RepositoryConfiguration repositoryConfiguration;
	private final Path path1 = FileSystems.getDefault().getPath("/tmp");
	private final Path path2 = FileSystems.getDefault().getPath("/tmp/folder1");
	
	@Before
	public void setUp() {
		repositoryConfiguration = new RepositoryConfigurationImpl("name1");
	}
	
	@Test
	public void shouldStoreItsNameAsName1() {
		assertEquals("name1", repositoryConfiguration.getName());
	}
	
	@Test
	public void shouldStoreItsNameAsName2() {
		RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl("name2");
		assertEquals("name2", repositoryConfiguration.getName());
	}

	@Test
	public void shouldSetNameToName3() {
		repositoryConfiguration.setName("name3");
		assertEquals("name3", repositoryConfiguration.getName());
	}
	
	@Test
	public void shouldHaveCollectionIdC1() {
		repositoryConfiguration.setCollectionId("c1");
		assertEquals("c1", repositoryConfiguration.getCollectionId());
	}
	
	@Test
	public void shouldHaveCollectionIdC2() {
		repositoryConfiguration.setCollectionId("c2");
		assertEquals("c2", repositoryConfiguration.getCollectionId());
	}

	@Test
	public void shouldHavePathToCertificate_tmp() {
		repositoryConfiguration.setPathToCertificate(path1);
		assertEquals("/tmp", repositoryConfiguration.getPathToCertificate().toString());
	}
	
	@Test
	public void shouldHavePathToCertificate_tmp_folder1() {
		repositoryConfiguration.setPathToCertificate(path2);
		assertEquals("/tmp/folder1", repositoryConfiguration.getPathToCertificate().toString());
	}

	@Test
	public void shouldHavePathToChecksumList_tmp() {
		repositoryConfiguration.setPathToChecksumList(path1);
		assertEquals("/tmp", repositoryConfiguration.getPathToChecksumList().toString());
	}

	@Test
	public void shouldHavePathToChecksumList_tmp_folder1() {
		repositoryConfiguration.setPathToChecksumList(path2);
		assertEquals("/tmp/folder1", repositoryConfiguration.getPathToChecksumList().toString());
	}

	@Test
	public void shouldHavePathToSettingsFiles_tmp() {
		repositoryConfiguration.setPathToSettingsFiles(path1);
		assertEquals("/tmp", repositoryConfiguration.getPathToSettingsFiles().toString());
	}

	@Test
	public void shouldHavePathToSettingsFiles_tmp_folder1() {
		repositoryConfiguration.setPathToSettingsFiles(path2);
		assertEquals("/tmp/folder1", repositoryConfiguration.getPathToSettingsFiles().toString());
	}
	
	@Test
	public void shouldHavePillarIdP1() {
		repositoryConfiguration.setPillarId("p1");
		assertEquals("p1", repositoryConfiguration.getPillarId());
	}

	@Test
	public void shouldHavePillarIdP2() {
		repositoryConfiguration.setPillarId("p2");
		assertEquals("p2", repositoryConfiguration.getPillarId());
	}

	
}

