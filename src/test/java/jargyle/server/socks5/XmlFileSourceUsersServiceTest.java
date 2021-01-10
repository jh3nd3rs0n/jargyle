package jargyle.server.socks5;

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

public class XmlFileSourceUsersServiceTest {

	private static final int HALF_SECOND = 500;
	
	private Path baseDir = null;
	private Path usersFile = null;
	private XmlFileSourceUsersService xmlFileSourceUsersService = null;
	
	@Before
	public void setUp() throws Exception {
		baseDir = Files.createTempDirectory("jargyle-");
		usersFile = baseDir.resolve("users.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (xmlFileSourceUsersService != null) {
			xmlFileSourceUsersService = null;
		}
		if (usersFile != null) {
			Files.deleteIfExists(usersFile);
			usersFile = null;
		}
		if (baseDir != null) {
			Files.deleteIfExists(baseDir);
			baseDir = null;
		}		
	}

	@Test
	public void testForUpdatedUsersFile01() throws IOException {
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.EMPTY_USERS_FILE), 
				usersFile.toFile());
		xmlFileSourceUsersService = XmlFileSourceUsersService.newInstance(
				usersFile.toFile());
		try {
			Thread.sleep(HALF_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.USERS_FILE), 
				usersFile.toFile());
		try {
			Thread.sleep(HALF_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		final int numOfUsersAdded = 3; 
		assertTrue(xmlFileSourceUsersService.getUsers().toList().size() == numOfUsersAdded);
	}

	@Test
	public void testForUpdatedUsersFile02() throws IOException {
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.USERS_FILE), 
				usersFile.toFile());
		xmlFileSourceUsersService = XmlFileSourceUsersService.newInstance(
				usersFile.toFile());
		try {
			Thread.sleep(HALF_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.ADDED_USER_TO_USERS_FILE), 
				usersFile.toFile());
		try {
			Thread.sleep(HALF_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		final int numOfUsersAdded = 4; 
		assertTrue(xmlFileSourceUsersService.getUsers().toList().size() == numOfUsersAdded);
	}

}
