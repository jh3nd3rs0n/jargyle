package com.github.jh3nd3rs0n.jargyle.cli;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
import com.github.jh3nd3rs0n.jargyle.test.help.IoHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class ServerConfigurationFileCreatorCLITest {
	
	private Path baseDir = null;
	private Path combinedConfigurationFile = null;
	private Path documentedCombinedConfigurationFile = null;
	private Path emptyConfigurationFile = null;	
	private Path generalConfigurationFile = null;
	private Path supplementedGeneralConfigurationFile = null;
	
	@Before
	public void setUp() throws Exception {
		this.baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		this.combinedConfigurationFile = this.baseDir.resolve("combined_configuration.xml");
		this.documentedCombinedConfigurationFile = this.baseDir.resolve("documented_combined_configuration.xml");
		this.emptyConfigurationFile = this.baseDir.resolve("empty_configuration.xml");
		this.generalConfigurationFile = this.baseDir.resolve("general_configuration.xml");
		this.supplementedGeneralConfigurationFile = this.baseDir.resolve("supplemented_general_configuration.xml");		
	}

	@After
	public void tearDown() throws Exception {
		if (this.supplementedGeneralConfigurationFile != null) {
			Files.deleteIfExists(this.supplementedGeneralConfigurationFile);
			this.supplementedGeneralConfigurationFile = null;
		}
		if (this.generalConfigurationFile != null) {
			Files.deleteIfExists(this.generalConfigurationFile);
			this.generalConfigurationFile = null;
		}
		if (this.emptyConfigurationFile != null) {
			Files.deleteIfExists(this.emptyConfigurationFile);
			this.emptyConfigurationFile = null;
		}
		if (this.documentedCombinedConfigurationFile != null) {
			Files.deleteIfExists(this.documentedCombinedConfigurationFile);
			this.documentedCombinedConfigurationFile = null;
		}
		if (this.combinedConfigurationFile != null) {
			Files.deleteIfExists(this.combinedConfigurationFile);
			this.combinedConfigurationFile = null;
		}
		if (this.baseDir != null) {
			Files.deleteIfExists(this.baseDir);
			this.baseDir = null;
		}
	}

	@Test
	public void testMainForCombiningConfigurationFiles() throws IOException {
		String[] args = new String[] {
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_CLI_SUPPLEMENTED_GENERAL_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_CLI_SOCKS5_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				this.combinedConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		String expectedCombinedConfigurationFileContents =
				TestResourceConstants.JARGYLE_CLI_COMBINED_CONFIGURATION_FILE.getContentAsString().replace(
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
				this.generalConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		String expectedConfigurationFileContents =
				TestResourceConstants.JARGYLE_CLI_GENERAL_CONFIGURATION_FILE.getContentAsString().replace(
						"\r\n", "\n").trim();
		String actualConfigurationFileContents =
				IoHelper.readStringFrom(this.generalConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedConfigurationFileContents, 
				actualConfigurationFileContents);
	}

	@Test
	public void testMainForCreatingADocumentedConfigurationFile() throws IOException {
		String[] args = new String[] {
				"--setting=doc=Start of general settings",
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_CLI_SUPPLEMENTED_GENERAL_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				"--setting=doc=End of general settings",
				"--setting=doc=Start of SOCKS5 settings",
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_CLI_SOCKS5_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				"--setting=doc=End of SOCKS5 settings",
				this.documentedCombinedConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		String expectedDocumentedConfigurationFileContents =
				TestResourceConstants.JARGYLE_CLI_DOCUMENTED_COMBINED_CONFIGURATION_FILE.getContentAsString().replace(
						"\r\n", "\n").trim();
		String actualDocumentedConfigurationFileContents =
				IoHelper.readStringFrom(this.documentedCombinedConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedDocumentedConfigurationFileContents, 
				actualDocumentedConfigurationFileContents);		
	}

	@Test
	public void testMainForCreatingAnEmptyConfigurationFile() throws IOException {
		String[] args = new String[] {
				this.emptyConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		String expectedEmptyConfigurationFileContents =
				TestResourceConstants.JARGYLE_CLI_EMPTY_CONFIGURATION_FILE.getContentAsString().replace(
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
				"--config-file=".concat(
						TestResourceConstants.JARGYLE_CLI_GENERAL_CONFIGURATION_FILE.getFile().getAbsolutePath()),
				"--setting=socksServerSocketSettings=SO_TIMEOUT=0",
				this.supplementedGeneralConfigurationFile.toAbsolutePath().toString()
		};
		CLI cli = new ServerConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
		}
		String expectedSupplementedConfigurationFileContents =
				TestResourceConstants.JARGYLE_CLI_SUPPLEMENTED_GENERAL_CONFIGURATION_FILE.getContentAsString().replace(
						"\r\n", "\n").trim();
		String actualSupplementedConfigurationFileContents =
				IoHelper.readStringFrom(this.supplementedGeneralConfigurationFile.toFile()).replace(
						"\r\n", "\n").trim();
		assertEquals(
				expectedSupplementedConfigurationFileContents, 
				actualSupplementedConfigurationFileContents);
	}

}
