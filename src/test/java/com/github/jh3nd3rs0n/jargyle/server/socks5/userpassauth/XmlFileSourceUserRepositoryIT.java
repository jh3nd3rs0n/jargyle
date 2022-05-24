package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

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

public class XmlFileSourceUserRepositoryIT {

	private Path baseDir = null;
	private Path usersXmlFile = null;
	private XmlFileSourceUserRepository xmlFileSourceUserRepository = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		this.usersXmlFile = this.baseDir.resolve("users.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (this.xmlFileSourceUserRepository != null) {
			this.xmlFileSourceUserRepository = null;
		}
		if (this.usersXmlFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.usersXmlFile);
			this.usersXmlFile = null;
		}
		if (this.baseDir != null) {
			FilesHelper.attemptsToDeleteIfExists(this.baseDir);
			this.baseDir = null;
		}		
	}

	@Test
	public void testForUpdatedUsersXmlFile01() throws IOException {
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_EMPTY_USERS_XML_FILE), 
				this.usersXmlFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		this.xmlFileSourceUserRepository = 
				XmlFileSourceUserRepository.newInstance(
						this.usersXmlFile.toString());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_USERS_XML_FILE), 
				this.usersXmlFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		final int numOfUsersAdded = 3; 
		assertTrue(this.xmlFileSourceUserRepository.getAll().toMap().size() == numOfUsersAdded);
	}

	@Test
	public void testForUpdatedUsersXmlFile02() throws IOException {
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_USERS_XML_FILE), 
				this.usersXmlFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		this.xmlFileSourceUserRepository = 
				XmlFileSourceUserRepository.newInstance(
						this.usersXmlFile.toString());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_ADDED_USER_TO_USERS_XML_FILE), 
				this.usersXmlFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		final int numOfUsersAdded = 4; 
		assertTrue(this.xmlFileSourceUserRepository.getAll().toMap().size() == numOfUsersAdded);
	}


}
