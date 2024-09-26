package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.test.help.IoHelper;
import com.github.jh3nd3rs0n.jargyle.test.help.ThreadHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class FileSourceConfigurationRepositoryTest {
	
	private Path baseDir = null;
	private Path configurationFile = null;
	private ConfigurationRepository fileSourceConfigurationRepository = null;

	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		this.configurationFile = this.baseDir.resolve("configuration.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (this.fileSourceConfigurationRepository != null) {
			this.fileSourceConfigurationRepository = null;
		}
		if (this.configurationFile != null) {
			Files.deleteIfExists(this.configurationFile);
			this.configurationFile = null;
		}
		if (this.baseDir != null) {
			Files.deleteIfExists(this.baseDir);
			this.baseDir = null;
		}
	}

	@Test
	public void testForUpdatedConfigurationFile01() throws IOException {
		File configFile = this.configurationFile.toFile();
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_EMPTY_CONFIGURATION_FILE.getContentAsString(),
				configFile);
		this.fileSourceConfigurationRepository =
				ConfigurationRepository.newFileSourceInstance(configFile);
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_CONFIGURATION_FILE.getContentAsString(),
				configFile);
		ThreadHelper.sleepForThreeSeconds();
		/* 
		 * get FileMonitor to recognize file has been modified if it hasn't already
		 * (occurs intermittently in Windows) 
		 */
		configFile.setLastModified(System.currentTimeMillis());
		ThreadHelper.sleepForThreeSeconds();
		Configuration configuration = 
				this.fileSourceConfigurationRepository.get();
		Settings settings = configuration.getSettings();
		Port expectedPort = Port.valueOf(1234);
		Port actualPort = settings.getLastValue(
				GeneralSettingSpecConstants.PORT);
		assertEquals(expectedPort, actualPort);
	}

	@Test
	public void testForUpdatedConfigurationFile02() throws IOException {
		File configFile = this.configurationFile.toFile();
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_EMPTY_CONFIGURATION_FILE.getContentAsString(),
				configFile);
		this.fileSourceConfigurationRepository =
				ConfigurationRepository.newFileSourceInstance(configFile);
		IoHelper.writeStringToFile(
				TestResourceConstants.JARGYLE_SERVER_CONFIGURATION_FILE.getContentAsString(),
				configFile);
		ThreadHelper.sleepForThreeSeconds();
		/* 
		 * get FileMonitor to recognize file has been modified if it hasn't already
		 * (occurs intermittently in Windows) 
		 */		
		configFile.setLastModified(System.currentTimeMillis());		
		ThreadHelper.sleepForThreeSeconds();
		Configuration configuration = 
				this.fileSourceConfigurationRepository.get();
		Settings settings = configuration.getSettings();
		NonNegativeInteger expectedBacklog = NonNegativeInteger.valueOf(
				100);
		NonNegativeInteger actualBacklog = settings.getLastValue(
				GeneralSettingSpecConstants.BACKLOG);
		assertEquals(expectedBacklog, actualBacklog);
	}

}
