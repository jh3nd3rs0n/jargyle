package jargyle.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import jargyle.IoHelper;
import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;

public class SocksServerTest {

	private static Path baseDir = null;
	private static Path combinedConfigurationFile = null;
	private static Path configurationFile = null;
	private static Path emptyConfigurationFile = null;
	private static Path supplementedConfigurationFile = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		baseDir = Files.createTempDirectory("jargyle-");
		combinedConfigurationFile = baseDir.resolve("combined_configuration.xml");
		configurationFile = baseDir.resolve("configuration.xml");
		emptyConfigurationFile = baseDir.resolve("empty_configuration.xml");
		supplementedConfigurationFile = baseDir.resolve("supplemented_configuration.xml");		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (supplementedConfigurationFile != null) {
			Files.deleteIfExists(supplementedConfigurationFile);
			supplementedConfigurationFile = null;
		}
		if (emptyConfigurationFile != null) {
			Files.deleteIfExists(emptyConfigurationFile);
			emptyConfigurationFile = null;
		}
		if (configurationFile != null) {
			Files.deleteIfExists(configurationFile);
			configurationFile = null;
		}
		if (combinedConfigurationFile != null) {
			Files.deleteIfExists(combinedConfigurationFile);
			combinedConfigurationFile = null;
		}
		if (baseDir != null) {
			Files.deleteIfExists(baseDir);
			baseDir = null;
		}
	}

	@Test
	public void testMainForCombiningConfigurationFiles() throws IOException {
		String[] args = new String[] {
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.CONFIGURATION_FILE).getAbsolutePath()),
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.SUPPLEMENTED_CONFIGURATION_FILE).getAbsolutePath()),
				"--new-config-file=".concat(
						combinedConfigurationFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedCombinedConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.COMBINED_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualCombinedConfigurationFileContents =
				IoHelper.readStringFrom(combinedConfigurationFile.toFile()).replace(
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
				"--setting=socks5.authMethods=NO_AUTHENTICATION_REQUIRED",
				"--new-config-file=".concat(
						configurationFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualConfigurationFileContents =
				IoHelper.readStringFrom(configurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedConfigurationFileContents, 
				actualConfigurationFileContents);
	}

	@Test
	public void testMainForCreatingAnEmptyConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--new-config-file=".concat(
						emptyConfigurationFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedEmptyConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.EMPTY_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualEmptyConfigurationFileContents =
				IoHelper.readStringFrom(emptyConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedEmptyConfigurationFileContents, 
				actualEmptyConfigurationFileContents);
	}

	@Test
	public void testMainForSupplementingAConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--setting=clientSocketSettings=SO_TIMEOUT=500",
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.CONFIGURATION_FILE).getAbsolutePath()),
				"--setting=socketSettings=SO_TIMEOUT=0",
				"--new-config-file=".concat(
						supplementedConfigurationFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedSupplementedConfigurationFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.SUPPLEMENTED_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualSupplementedConfigurationFileContents =
				IoHelper.readStringFrom(supplementedConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedSupplementedConfigurationFileContents, 
				actualSupplementedConfigurationFileContents);
	}

}
