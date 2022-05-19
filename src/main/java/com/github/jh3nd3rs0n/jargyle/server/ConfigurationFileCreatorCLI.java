package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.io.UncheckedIOException;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;

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
		} catch (UncheckedIOException e) {
			System.err.printf("%s: %s%n", this.getProgramName(), e);
			e.printStackTrace(System.err);
			throw new TerminationRequestedException(-1);
		}
		throw new TerminationRequestedException(0);
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
	
	private void newConfigurationFile(final String file) {
		File f = new File(file);
		ConfigurationRepository configurationRepository =
				ConfigurationRepository.newInstance(f);
		configurationRepository.set(this.getConfiguration());
	}
	
}
