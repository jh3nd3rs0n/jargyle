package jargyle.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jargyle.IoHelper;
import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
import jargyle.common.net.Port;

public class XmlFileSourceConfigurationServiceTest {

	private static final int ONE_SECOND = 1000;
	
	private Path baseDir = null;
	private Path configurationFile = null;
	private XmlFileSourceConfigurationService xmlFileSourceConfigurationService = null;

	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("jargyle-");
		this.configurationFile = this.baseDir.resolve("configuration.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (this.xmlFileSourceConfigurationService != null) {
			this.xmlFileSourceConfigurationService = null;
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
	public void testForUpdatedConfigurationFile() throws IOException {
		/*
		if (System.getProperty("os.name").equals("Mac OS X")) {
			// WatchService.take() in FileMonitor does not receive a WatchKey
			return; 
		}
		*/
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.EMPTY_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		this.xmlFileSourceConfigurationService = 
				XmlFileSourceConfigurationService.newInstance(
						this.configurationFile.toFile());
		try {
			Thread.sleep(ONE_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		try {
			Thread.sleep(ONE_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		Configuration configuration = 
				this.xmlFileSourceConfigurationService.getConfiguration();
		Settings settings = configuration.getSettings();
		Port expectedPort = Port.newInstance(1234);
		Port actualPort = settings.getLastValue(SettingSpec.PORT, Port.class);
		assertEquals(expectedPort, actualPort);
	}

}
