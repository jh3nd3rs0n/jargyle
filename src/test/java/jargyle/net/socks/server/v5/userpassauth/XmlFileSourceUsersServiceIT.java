package jargyle.net.socks.server.v5.userpassauth;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jargyle.IoHelper;
import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;

public class XmlFileSourceUsersServiceIT {

	private static final int THREE_SECONDS = 3000;
	
	private Path baseDir = null;
	private Path usersFile = null;
	private XmlFileSourceUsersService xmlFileSourceUsersService = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("jargyle-");
		this.usersFile = this.baseDir.resolve("users.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (this.xmlFileSourceUsersService != null) {
			this.xmlFileSourceUsersService = null;
		}
		if (this.usersFile != null) {
			Files.deleteIfExists(this.usersFile);
			this.usersFile = null;
		}
		if (this.baseDir != null) {
			Files.deleteIfExists(this.baseDir);
			this.baseDir = null;
		}		
	}

	@Test
	public void testForUpdatedUsersFile01() throws IOException {
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_EMPTY_USERS_FILE), 
				this.usersFile.toFile());
		this.xmlFileSourceUsersService = XmlFileSourceUsersService.newInstance(
				this.usersFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_USERS_FILE), 
				this.usersFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		final int numOfUsersAdded = 3; 
		assertTrue(this.xmlFileSourceUsersService.getUsers().toList().size() == numOfUsersAdded);
	}

	@Test
	public void testForUpdatedUsersFile02() throws IOException {
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_USERS_FILE), 
				this.usersFile.toFile());
		this.xmlFileSourceUsersService = XmlFileSourceUsersService.newInstance(
				this.usersFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_V5_ADDED_USER_TO_USERS_FILE), 
				this.usersFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		final int numOfUsersAdded = 4; 
		assertTrue(this.xmlFileSourceUsersService.getUsers().toList().size() == numOfUsersAdded);
	}

}
