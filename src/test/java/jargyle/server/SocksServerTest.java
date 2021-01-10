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
	private static Path combinedConfigurationXmlFile = null;
	private static Path configurationXmlFile = null;
	private static Path emptyConfigurationXmlFile = null;
	private static Path newConfigurationXmlFile = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		baseDir = Files.createTempDirectory("jargyle-");
		combinedConfigurationXmlFile = baseDir.resolve("combined_configuration.xml");
		configurationXmlFile = baseDir.resolve("configuration.xml");
		emptyConfigurationXmlFile = baseDir.resolve("empty_configuration.xml");
		newConfigurationXmlFile = baseDir.resolve("new_configuration.xml");		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (newConfigurationXmlFile != null) {
			Files.deleteIfExists(newConfigurationXmlFile);
			newConfigurationXmlFile = null;
		}
		if (emptyConfigurationXmlFile != null) {
			Files.deleteIfExists(emptyConfigurationXmlFile);
			emptyConfigurationXmlFile = null;
		}
		if (configurationXmlFile != null) {
			Files.deleteIfExists(configurationXmlFile);
			configurationXmlFile = null;
		}
		if (combinedConfigurationXmlFile != null) {
			Files.deleteIfExists(combinedConfigurationXmlFile);
			combinedConfigurationXmlFile = null;
		}
		if (baseDir != null) {
			Files.deleteIfExists(baseDir);
			baseDir = null;
		}
	}

	@Test
	public void testMain01() throws IOException {
		String[] args = new String[] {
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.CONFIGURATION_FILE).getAbsolutePath()),
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.NEW_CONFIGURATION_FILE).getAbsolutePath()),
				"--new-config-file=".concat(
						combinedConfigurationXmlFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedCombinedConfigurationXmlFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.COMBINED_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualCombinedConfigurationXmlFileContents =
				IoHelper.readStringFrom(combinedConfigurationXmlFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedCombinedConfigurationXmlFileContents, 
				actualCombinedConfigurationXmlFileContents);
	}

	@Test
	public void testMain02() throws IOException {
		String[] args = new String[] {
				"--setting=port=1234",
				"--setting=backlog=100",
				"--setting=socks5.authMethods=NO_AUTHENTICATION_REQUIRED",
				"--new-config-file=".concat(
						configurationXmlFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedConfigurationXmlFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualConfigurationXmlFileContents =
				IoHelper.readStringFrom(configurationXmlFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedConfigurationXmlFileContents, 
				actualConfigurationXmlFileContents);
	}

	@Test
	public void testMain03() throws IOException {
		String[] args = new String[] {
				"--new-config-file=".concat(
						emptyConfigurationXmlFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedEmptyConfigurationXmlFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.EMPTY_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualEmptyConfigurationXmlFileContents =
				IoHelper.readStringFrom(emptyConfigurationXmlFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedEmptyConfigurationXmlFileContents, 
				actualEmptyConfigurationXmlFileContents);
	}

	@Test
	public void testMain04() throws IOException {
		String[] args = new String[] {
				"--setting=clientSocketSettings=SO_TIMEOUT=500",
				"--config-file=".concat(ResourceHelper.getResourceAsFile(
						ResourceNameConstants.CONFIGURATION_FILE).getAbsolutePath()),
				"--setting=socketSettings=SO_TIMEOUT=0",
				"--new-config-file=".concat(
						newConfigurationXmlFile.toAbsolutePath().toString())
		};
		SocksServer.main(args);
		String expectedNewConfigurationXmlFileContents =
				ResourceHelper.getResourceAsString(
						ResourceNameConstants.NEW_CONFIGURATION_FILE).replace(
								"\r\n", "\n").trim();
		String actualNewConfigurationXmlFileContents =
				IoHelper.readStringFrom(newConfigurationXmlFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedNewConfigurationXmlFileContents, 
				actualNewConfigurationXmlFileContents);
	}

}
