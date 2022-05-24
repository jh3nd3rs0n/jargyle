package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertEquals;

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
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;

public class XmlFileSourceConfigurationRepositoryIT {
	
	private Path baseDir = null;
	private Path configurationFile = null;
	private XmlFileSourceConfigurationRepository xmlFileSourceConfigurationRepository = null;

	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		this.configurationFile = this.baseDir.resolve("configuration.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (this.xmlFileSourceConfigurationRepository != null) {
			this.xmlFileSourceConfigurationRepository = null;
		}
		if (this.configurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.configurationFile);
			this.configurationFile = null;
		}
		if (this.baseDir != null) {
			FilesHelper.attemptsToDeleteIfExists(this.baseDir);
			this.baseDir = null;
		}
	}

	@Test
	public void testForUpdatedConfigurationFile01() throws IOException {
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_EMPTY_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		ThreadHelper.sleepForThreeSeconds();		
		this.xmlFileSourceConfigurationRepository = 
				XmlFileSourceConfigurationRepository.newInstance(
						this.configurationFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		Configuration configuration = 
				this.xmlFileSourceConfigurationRepository.get();
		Settings settings = configuration.getSettings();
		Port expectedPort = Port.newInstance(1234);
		Port actualPort = settings.getLastValue(
				GeneralSettingSpecConstants.PORT);
		assertEquals(expectedPort, actualPort);
	}

	@Test
	public void testForUpdatedConfigurationFile02() throws IOException {
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_EMPTY_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		ThreadHelper.sleepForThreeSeconds();		
		this.xmlFileSourceConfigurationRepository = 
				XmlFileSourceConfigurationRepository.newInstance(
						this.configurationFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		IoHelper.writeStringToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_SERVER_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		ThreadHelper.sleepForThreeSeconds();
		Configuration configuration = 
				this.xmlFileSourceConfigurationRepository.get();
		Settings settings = configuration.getSettings();
		NonnegativeInteger expectedBacklog = NonnegativeInteger.newInstance(
				100);
		NonnegativeInteger actualBacklog = settings.getLastValue(
				GeneralSettingSpecConstants.BACKLOG);
		assertEquals(expectedBacklog, actualBacklog);
	}

}
