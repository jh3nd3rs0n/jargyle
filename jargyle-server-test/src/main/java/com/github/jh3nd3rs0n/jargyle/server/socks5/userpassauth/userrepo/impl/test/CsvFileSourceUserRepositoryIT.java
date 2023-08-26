package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository;
import com.github.jh3nd3rs0n.test.help.IoHelper;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;
import com.github.jh3nd3rs0n.test.help.constants.TestResourceConstants;

public class CsvFileSourceUserRepositoryIT {

	private Path baseDir = null;
	private Path usersCsvFile = null;
	private CsvFileSourceUserRepository csvFileSourceUserRepository = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		this.usersCsvFile = this.baseDir.resolve("users.csv");
	}

	@After
	public void tearDown() throws Exception {
		if (this.csvFileSourceUserRepository != null) {
			this.csvFileSourceUserRepository = null;
		}
		if (this.usersCsvFile != null) {
			Files.deleteIfExists(this.usersCsvFile);
			this.usersCsvFile = null;
		}
		if (this.baseDir != null) {
			Files.deleteIfExists(this.baseDir);
			this.baseDir = null;
		}		
	}

	@Test
	public void testForUpdatedUsersCsvFile01() throws IOException {
		File usrsCsvFile = this.usersCsvFile.toFile();
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_USERREPO_IMPL_EMPTY_USERS_CSV_FILE.getContentAsString(), 
				usrsCsvFile);
		this.csvFileSourceUserRepository = 
				CsvFileSourceUserRepository.newInstance(usrsCsvFile.toString());
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_USERREPO_IMPL_USERS_CSV_FILE.getContentAsString(), 
				usrsCsvFile);
		ThreadHelper.sleepForThreeSeconds();
		/* 
		 * get FileMonitor to recognize file has been modified if it hasn't already
		 * (occurs intermittently in Windows) 
		 */
		usrsCsvFile.setLastModified(System.currentTimeMillis());
		ThreadHelper.sleepForThreeSeconds();
		final int userCount = 3; 
		assertTrue(this.csvFileSourceUserRepository.getAll().toMap().size() == userCount);
	}

	@Test
	public void testForUpdatedUsersCsvFile02() throws IOException {
		File usrsCsvFile = this.usersCsvFile.toFile();
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_USERREPO_IMPL_USERS_CSV_FILE.getContentAsString(), 
				usrsCsvFile);
		this.csvFileSourceUserRepository = 
				CsvFileSourceUserRepository.newInstance(usrsCsvFile.toString());
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_SOCKS5_USERPASSAUTH_USERREPO_IMPL_ADDED_USER_TO_USERS_CSV_FILE.getContentAsString(), 
				usrsCsvFile);
		ThreadHelper.sleepForThreeSeconds();
		/* 
		 * get FileMonitor to recognize file has been modified if it hasn't already
		 * (occurs intermittently in Windows) 
		 */		
		usrsCsvFile.setLastModified(System.currentTimeMillis());
		ThreadHelper.sleepForThreeSeconds();		
		final int userCount = 4; 
		assertTrue(this.csvFileSourceUserRepository.getAll().toMap().size() == userCount);
	}

}
