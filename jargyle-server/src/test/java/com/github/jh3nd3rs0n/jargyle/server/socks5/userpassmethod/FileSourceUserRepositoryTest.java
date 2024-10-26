package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;

import com.github.jh3nd3rs0n.jargyle.test.help.io.IoHelper;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.server.TestResourceConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class FileSourceUserRepositoryTest {

	private Path baseDir = null;
	private Path usersFile = null;
	private UserRepository fileSourceUserRepository = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		this.usersFile = this.baseDir.resolve("users");
	}

	@After
	public void tearDown() throws Exception {
		if (this.fileSourceUserRepository != null) {
			this.fileSourceUserRepository = null;
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
		File usrsFile = this.usersFile.toFile();
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSMETHOD_EMPTY_USERS_FILE.getContentAsString(),
				usrsFile);
		this.fileSourceUserRepository = 
				UserRepositorySpecConstants.FILE_SOURCE_USER_REPOSITORY.newUserRepository(
						usrsFile.toString()); 
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSMETHOD_USERS_FILE.getContentAsString(),
				usrsFile);
		ThreadHelper.interruptibleSleepForThreeSeconds();
		/* 
		 * get FileMonitor to recognize file has been modified if it hasn't already
		 * (occurs intermittently in Windows) 
		 */
		usrsFile.setLastModified(System.currentTimeMillis());
		ThreadHelper.interruptibleSleepForThreeSeconds();
		final int userCount = 3; 
		assertTrue(this.fileSourceUserRepository.getAll().toMap().size() == userCount);
	}

	@Test
	public void testForUpdatedUsersFile02() throws IOException {
		File usrsFile = this.usersFile.toFile();
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSMETHOD_USERS_FILE.getContentAsString(),
				usrsFile);
		this.fileSourceUserRepository = 
				UserRepositorySpecConstants.FILE_SOURCE_USER_REPOSITORY.newUserRepository(
						usrsFile.toString());
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSMETHOD_ADDED_USER_TO_USERS_FILE.getContentAsString(),
				usrsFile);
		ThreadHelper.interruptibleSleepForThreeSeconds();
		/* 
		 * get FileMonitor to recognize file has been modified if it hasn't already
		 * (occurs intermittently in Windows) 
		 */		
		usrsFile.setLastModified(System.currentTimeMillis());
		ThreadHelper.interruptibleSleepForThreeSeconds();
		final int userCount = 4; 
		assertTrue(this.fileSourceUserRepository.getAll().toMap().size() == userCount);
	}

}
