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
		baseDir = Files.createTempDirectory("jargyle-");
		configurationFile = baseDir.resolve("configuration.xml");
	}

	@After
	public void tearDown() throws Exception {
		if (xmlFileSourceConfigurationService != null) {
			xmlFileSourceConfigurationService = null;
		}
		if (configurationFile != null) {
			Files.deleteIfExists(configurationFile);
			configurationFile = null;
		}
		if (baseDir != null) {
			Files.deleteIfExists(baseDir);
			baseDir = null;
		}
	}

	@Test
	public void testForUpdatedConfigurationFile() throws IOException {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			// WatchService.take() in FileMonitor does not receive a WatchKey
			return; 
		} 
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.EMPTY_CONFIGURATION_FILE), 
				configurationFile.toFile());
		xmlFileSourceConfigurationService = 
				XmlFileSourceConfigurationService.newInstance(
						configurationFile.toFile());
		try {
			Thread.sleep(ONE_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.CONFIGURATION_FILE), 
				configurationFile.toFile());
		try {
			Thread.sleep(ONE_SECOND);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		Configuration configuration = 
				xmlFileSourceConfigurationService.getConfiguration();
		Settings settings = configuration.getSettings();
		Port expectedPort = Port.newInstance(1234);
		Port actualPort = settings.getLastValue(SettingSpec.PORT, Port.class);
		assertEquals(expectedPort, actualPort);
	}

}
