package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.io.IOException;
import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;

public final class SocksServerCLI extends AbstractCLI {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			SocksServerCLI.class);
	
	public static void main(final String[] args) {
		CLI cli = new SocksServerCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			System.exit(e.getExitStatusCode());
		}
	}
	
	private String monitoredConfigurationFile;
	
	public SocksServerCLI(
			final String progName, 
			final String progBeginningUsage,
			final String[] args, 
			final boolean posixCorrect) {
		super(progName, progBeginningUsage, args, posixCorrect);
		this.setProgramOperandsUsage("[MONITORED_CONFIG_FILE]");
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
	
	private Configuration newConfiguration() 
			throws TerminationRequestedException {
		Configuration configuration = null;
		if (this.monitoredConfigurationFile == null) {
			configuration = ImmutableConfiguration.newInstance(
					this.getConfiguration());
		} else {
			File f = new File(this.monitoredConfigurationFile);
			ConfigurationProvider configurationProvider = null;
			try {
				configurationProvider = 
						XmlFileSourceConfigurationProvider.newInstance(f);
			} catch (IllegalArgumentException e) {
				System.err.printf("%s: %s%n", this.getProgramName(), e);
				e.printStackTrace(System.err);
				throw new TerminationRequestedException(-1);
			}
			configuration = MutableConfiguration.newInstance(
					configurationProvider);
		}
		return configuration;
	}
	
	private void startSocksServer(final Configuration configuration) 
			throws TerminationRequestedException {
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
		} catch (BindException e) {
			LOGGER.error(
					String.format(
							"Unable to listen on port %s at %s", 
							configuration.getSettings().getLastValue(
									GeneralSettingSpecConstants.PORT),
							configuration.getSettings().getLastValue(
									GeneralSettingSpecConstants.HOST)), 
					e);
			throw new TerminationRequestedException(-1);
		} catch (IOException e) {
			LOGGER.error("Error in starting SocksServer", e);
			throw new TerminationRequestedException(-1);
		}
		LOGGER.info(String.format(
				"Listening on port %s at %s",
				socksServer.getPort(),
				socksServer.getHost()));
	}

}
