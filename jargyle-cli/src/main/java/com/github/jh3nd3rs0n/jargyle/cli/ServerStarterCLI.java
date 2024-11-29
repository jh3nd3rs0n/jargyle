package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

public final class ServerStarterCLI extends ServerConfigurationCLI {
	
	public static void main(final String[] args) {
		CLI cli = new ServerStarterCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			System.exit(e.getExitStatusCode());
		}
	}
	
	private final Logger logger;
	private String monitoredConfigurationFile;
	
	public ServerStarterCLI(
			final String progName, 
			final String progBeginningUsage,
			final String[] args, 
			final boolean posixCorrect) {
		super(progName, progBeginningUsage, args, posixCorrect);
		this.setProgramOperandsUsage("[MONITORED_CONFIG_FILE]");
		this.logger = LoggerFactory.getLogger(ServerStarterCLI.class);
	}

	@Override
	protected void afterHandleArgs() throws TerminationRequestedException {
		Configuration configuration = this.newConfiguration();
		this.startSocksServer(configuration);
	}
	
	@Override
	protected void beforeHandleArgs() {
		super.beforeHandleArgs();
		this.monitoredConfigurationFile = null;
	}

	@Override
	protected void handleNonparsedArg(
			final String nonparsedArg) throws TerminationRequestedException {
		if (this.monitoredConfigurationFile != null) {
			throw new IllegalArgumentException(String.format(
					"extra operand `%s'", nonparsedArg));
		}
		this.monitoredConfigurationFile = nonparsedArg;
	}
	
	private Configuration newConfiguration() 
			throws TerminationRequestedException {
		Configuration configuration = null;
		if (this.monitoredConfigurationFile == null) {
			configuration = Configuration.newUnmodifiableInstance(
					this.getConfiguration());
		} else {
			File f = new File(this.monitoredConfigurationFile);
			if (!f.exists()) {
				System.err.printf("%s: `%s' not found%n", 
						this.getProgramName(), 
						this.monitoredConfigurationFile);
				throw new TerminationRequestedException(-1);
			}
			if (!f.isFile()) {
				System.err.printf("%s: `%s' not a file%n", 
						this.getProgramName(), 
						this.monitoredConfigurationFile);
				throw new TerminationRequestedException(-1);			
			}			
			ConfigurationRepository configurationRepository = null;
			try {
				configurationRepository = 
						ConfigurationRepository.newFileSourceInstance(f);
			} catch (Throwable t) {
				System.err.printf("%s: %s%n", this.getProgramName(), t);
				t.printStackTrace(System.err);
				throw new TerminationRequestedException(-1);
			}
			configuration = Configuration.newUpdatedInstance(
					configurationRepository);
		}
		return configuration;
	}
	
	private void startSocksServer(final Configuration configuration) 
			throws TerminationRequestedException {
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
		} catch (IOException e) {
			this.logger.error("Error in starting SocksServer", e);
			throw new TerminationRequestedException(-1);
		}
		this.logger.info(String.format(
				"Listening on port %s at %s",
				socksServer.getPort(),
				socksServer.getHost()));
	}

}
