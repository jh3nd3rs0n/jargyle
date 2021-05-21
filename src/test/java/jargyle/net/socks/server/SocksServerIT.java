package jargyle.net.socks.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jargyle.FilesHelper;
import jargyle.IoHelper;
import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
import jargyle.ThreadHelper;
import jargyle.net.Port;

public class SocksServerIT {
	
	private Path baseDir = null;
	private Path configurationFile = null;
	
	@Before
	public void setUpBeforeClass() throws Exception {
		this.baseDir = Files.createTempDirectory("jargyle-");
		this.configurationFile = this.baseDir.resolve("configuration.xml");
	}

	@After
	public void tearDownAfterClass() throws Exception {
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
	public void testGetPortForChangingConfiguration() throws IOException {
		IoHelper.writeStringToFile(
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_EMPTY_CONFIGURATION_FILE), 
				this.configurationFile.toFile());
		ConfigurationProvider configurationProvider = 
				XmlFileSourceConfigurationProvider.newInstance(
						this.configurationFile.toFile());
		Configuration configuration = MutableConfiguration.newInstance(
				configurationProvider);
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
			ThreadHelper.sleepForThreeSeconds();		
			IoHelper.writeStringToFile(
					ResourceHelper.getResourceAsString(
							ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_CONFIGURATION_FILE), 
					this.configurationFile.toFile());
			ThreadHelper.sleepForThreeSeconds();
			Port expectedPort = Port.newInstance(1080);
			Port actualPort = socksServer.getPort();
			assertEquals(expectedPort, actualPort);
		} finally {
			if (socksServer.isStarted()) {
				socksServer.stop();
			}
		}
	}

}
