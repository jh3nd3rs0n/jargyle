package jargyle.net.socks.server.v5.userpassauth;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jargyle.FilesHelper;
import jargyle.IoHelper;
import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
import jargyle.ThreadHelper;

public class XmlFileSourceUsersProviderIT {
	
	private Path baseDir = null;
	private Path usersFile = null;
	private XmlFileSourceUsersProvider xmlFileSourceUsersProvider = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("jargyle-");
		this.usersFile = this.baseDir.resolve("users.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (this.xmlFileSourceUsersProvider != null) {
			this.xmlFileSourceUsersProvider = null;
		}
		if (this.usersFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.usersFile);
			this.usersFile = null;
		}
		if (this.baseDir != null) {
			FilesHelper.attemptsToDeleteIfExists(this.baseDir);
			this.baseDir = null;
		}		
	}

	@Test
	public void testForUpdatedUsersFile01() throws IOException {
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_USERPASSAUTH_EMPTY_USERS_FILE), 
				this.usersFile.toFile());
		this.xmlFileSourceUsersProvider = 
				XmlFileSourceUsersProvider.newInstance(
						this.usersFile.toString());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_USERPASSAUTH_USERS_FILE), 
				this.usersFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		final int numOfUsersAdded = 3; 
		assertTrue(this.xmlFileSourceUsersProvider.getUsers().toMap().size() == numOfUsersAdded);
	}

	@Test
	public void testForUpdatedUsersFile02() throws IOException {
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_USERPASSAUTH_USERS_FILE), 
				this.usersFile.toFile());
		this.xmlFileSourceUsersProvider = 
				XmlFileSourceUsersProvider.newInstance(
						this.usersFile.toString());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_USERPASSAUTH_ADDED_USER_TO_USERS_FILE), 
				this.usersFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		final int numOfUsersAdded = 4; 
		assertTrue(this.xmlFileSourceUsersProvider.getUsers().toMap().size() == numOfUsersAdded);
	}

}
