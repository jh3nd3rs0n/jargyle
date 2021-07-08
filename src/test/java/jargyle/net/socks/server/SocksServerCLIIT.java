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

public class SocksServerCLIIT {
	
	private Path baseDir = null;
	private Path combinedConfigurationFile = null;
	private Path configurationFile = null;
	private Path emptyConfigurationFile = null;
	private Path supplementedConfigurationFile = null;
	
	@Before
	public void setUpBeforeClass() throws Exception {
		this.baseDir = Files.createTempDirectory("jargyle-");
		this.combinedConfigurationFile = this.baseDir.resolve("combined_configuration.xml");
		this.configurationFile = this.baseDir.resolve("configuration.xml");
		this.emptyConfigurationFile = this.baseDir.resolve("empty_configuration.xml");
		this.supplementedConfigurationFile = this.baseDir.resolve("supplemented_configuration.xml");		
	}

	@After
	public void tearDownAfterClass() throws Exception {
		if (this.supplementedConfigurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.supplementedConfigurationFile);
			this.supplementedConfigurationFile = null;
		}
		if (this.emptyConfigurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.emptyConfigurationFile);
			this.emptyConfigurationFile = null;
		}
		if (this.configurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.configurationFile);
			this.configurationFile = null;
		}
		if (this.combinedConfigurationFile != null) {
			FilesHelper.attemptsToDeleteIfExists(this.combinedConfigurationFile);
			this.combinedConfigurationFile = null;
		}
		if (this.baseDir != null) {
			FilesHelper.attemptsToDeleteIfExists(this.baseDir);
			this.baseDir = null;
		}
	}

	@Test
	public void testMainForCombiningConfigurationFiles() throws IOException {
		String[] args = new String[] {
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_CONFIGURATION_FILE).getAbsolutePath()),
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_SUPPLEMENTED_CONFIGURATION_FILE).getAbsolutePath()),
				"--new-config-file=".concat(
						this.combinedConfigurationFile.toAbsolutePath().toString())
		};
		SocksServerCLI.main(args);
		String expectedCombinedConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_COMBINED_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualCombinedConfigurationFileContents =
				IoHelper.readStringFrom(this.combinedConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedCombinedConfigurationFileContents, 
				actualCombinedConfigurationFileContents);
	}

	@Test
	public void testMainForCreatingAConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--setting=port=1234",
				"--setting=backlog=100",
				"--setting=socks5.methods=NO_AUTHENTICATION_REQUIRED",
				"--new-config-file=".concat(
						this.configurationFile.toAbsolutePath().toString())
		};
		SocksServerCLI.main(args);
		String expectedConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualConfigurationFileContents =
				IoHelper.readStringFrom(this.configurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedConfigurationFileContents, 
				actualConfigurationFileContents);
	}

	@Test
	public void testMainForCreatingAnEmptyConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--new-config-file=".concat(
						this.emptyConfigurationFile.toAbsolutePath().toString())
		};
		SocksServerCLI.main(args);
		String expectedEmptyConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_EMPTY_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualEmptyConfigurationFileContents =
				IoHelper.readStringFrom(this.emptyConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedEmptyConfigurationFileContents, 
				actualEmptyConfigurationFileContents);
	}
	
	@Test
	public void testMainForSupplementingAConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_CONFIGURATION_FILE).getAbsolutePath()),
				"--setting=socketSettings=SO_TIMEOUT=0",
				"--new-config-file=".concat(
						this.supplementedConfigurationFile.toAbsolutePath().toString())
		};
		SocksServerCLI.main(args);
		String expectedSupplementedConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.JARGYLE_NET_SOCKS_SERVER_SUPPLEMENTED_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualSupplementedConfigurationFileContents =
				IoHelper.readStringFrom(this.supplementedConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedSupplementedConfigurationFileContents, 
				actualSupplementedConfigurationFileContents);
	}

}
