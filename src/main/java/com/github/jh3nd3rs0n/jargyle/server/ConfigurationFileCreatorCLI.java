package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
import com.github.jh3nd3rs0n.jargyle.server.config.xml.bind.ConfigurationXml;

import jakarta.xml.bind.JAXBException;

public final class ConfigurationFileCreatorCLI extends AbstractCLI {
	
	public static void main(final String[] args) {
		CLI cli = new ConfigurationFileCreatorCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			System.exit(e.getExitStatusCode());
		}
	}

	private String newConfigurationFile;
	
	public ConfigurationFileCreatorCLI(
			final String progName, 
			final String progBeginningUsage, 
			final String[] args, 
			final boolean posixCorrect) {
		super(progName, progBeginningUsage, args, posixCorrect);
		this.setProgramOperandsUsage("FILE");
	}
	
	@Override
	protected void afterHandleArgs() throws TerminationRequestedException {
		if (this.newConfigurationFile == null) {
			System.err.printf(
					"%s: operand FILE required%n", this.getProgramName());
			throw new TerminationRequestedException(-1);
		}
		try {
			this.newConfigurationFile(this.newConfigurationFile);
		} catch (IOException e) {
			System.err.printf("%s: %s%n", this.getProgramName(), e);
			e.printStackTrace(System.err);
			throw new TerminationRequestedException(-1);
		}
	}

	@Override
	protected void beforeHandleArgs() {
		super.beforeHandleArgs();
		this.newConfigurationFile = null;
	}

	@Override
	protected void handleNonparsedArg(
			final String nonparsedArg) throws TerminationRequestedException {
		if (this.newConfigurationFile != null) {
			throw new IllegalArgumentException(String.format(
					"extra operand `%s'", nonparsedArg));
		}
		this.newConfigurationFile = nonparsedArg;
	}
	
	private void newConfigurationFile(final String file) throws IOException {
		String tempArg = file;
		System.out.print("Writing to ");
		OutputStream out = null;
		if (file.equals("-")) {
			System.out.printf("standard output...%n");
			out = System.out;
		} else {
			File f = new File(file);
			System.out.printf("'%s'...%n", f.getAbsolutePath());
			File tempFile = null;
			do {
				tempArg = tempArg.concat(".tmp");
				tempFile = new File(tempArg);
			} while (tempFile.exists());
			tempFile.createNewFile();
			out = new FileOutputStream(tempFile);
		}
		ConfigurationXml configurationXml = new ConfigurationXml(
				this.getConfiguration());
		try {
			configurationXml.toXml(out);
			out.flush();
		} catch (JAXBException e) {
			throw new IOException(e);
		} finally {
			if (out instanceof FileOutputStream) {
				out.close();
			}
		}
		if (!file.equals("-")) {
			Files.move(
					new File(tempArg).toPath(), 
					new File(file).toPath(), 
					StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
}
