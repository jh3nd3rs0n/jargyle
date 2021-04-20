package jargyle.net.socks.server;

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
import jargyle.net.Port;
import jargyle.util.NonnegativeInteger;

public class XmlFileSourceConfigurationServiceIT {

	private static final int THREE_SECONDS = 3000;
	
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
	public void testForUpdatedConfigurationFile01() throws IOException {
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_EMPTY_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		this.xmlFileSourceConfigurationService = 
				XmlFileSourceConfigurationService.newInstance(
						this.configurationFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		Configuration configuration = 
				this.xmlFileSourceConfigurationService.getConfiguration();
		Settings settings = configuration.getSettings();
		Port expectedPort = Port.newInstance(1234);
		Port actualPort = settings.getLastValue(SettingSpec.PORT);
		assertEquals(expectedPort, actualPort);
	}

	@Test
	public void testForUpdatedConfigurationFile02() throws IOException {
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_EMPTY_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		this.xmlFileSourceConfigurationService = 
				XmlFileSourceConfigurationService.newInstance(
						this.configurationFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		IoHelper.writeToFile(ResourceHelper.getResourceAsString(
				ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		Configuration configuration = 
				this.xmlFileSourceConfigurationService.getConfiguration();
		Settings settings = configuration.getSettings();
		NonnegativeInteger expectedBacklog = NonnegativeInteger.newInstance(
				100);
		NonnegativeInteger actualBacklog = settings.getLastValue(
				SettingSpec.BACKLOG);
		assertEquals(expectedBacklog, actualBacklog);
	}

}
