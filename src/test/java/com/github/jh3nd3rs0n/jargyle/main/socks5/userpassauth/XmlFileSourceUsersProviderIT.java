package com.github.jh3nd3rs0n.jargyle.main.socks5.userpassauth;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.FilesHelper;
import com.github.jh3nd3rs0n.jargyle.IoHelper;
import com.github.jh3nd3rs0n.jargyle.ResourceHelper;
import com.github.jh3nd3rs0n.jargyle.ResourceNameConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;

public class XmlFileSourceUsersProviderIT {
	
	private Path baseDir = null;
	private Path usersFile = null;
	private XmlFileSourceUsersProvider xmlFileSourceUsersProvider = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
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
				ResourceNameConstants.JARGYLE_MAIN_SOCKS5_USERPASSAUTH_EMPTY_USERS_FILE), 
				this.usersFile.toFile());
		this.xmlFileSourceUsersProvider = 
				XmlFileSourceUsersProvider.newInstance(
						this.usersFile.toString());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_MAIN_SOCKS5_USERPASSAUTH_USERS_FILE), 
				this.usersFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		final int numOfUsersAdded = 3; 
		assertTrue(this.xmlFileSourceUsersProvider.getUsers().toMap().size() == numOfUsersAdded);
	}

	@Test
	public void testForUpdatedUsersFile02() throws IOException {
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_MAIN_SOCKS5_USERPASSAUTH_USERS_FILE), 
				this.usersFile.toFile());
		this.xmlFileSourceUsersProvider = 
				XmlFileSourceUsersProvider.newInstance(
						this.usersFile.toString());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_MAIN_SOCKS5_USERPASSAUTH_ADDED_USER_TO_USERS_FILE), 
				this.usersFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		final int numOfUsersAdded = 4; 
		assertTrue(this.xmlFileSourceUsersProvider.getUsers().toMap().size() == numOfUsersAdded);
	}

}
