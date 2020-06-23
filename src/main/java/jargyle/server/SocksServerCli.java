package jargyle.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import argmatey.ArgMatey.ArgsParser;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.IllegalOptionArgException;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.OptionArgSpecBuilder;
import argmatey.ArgMatey.OptionBuilder;
import argmatey.ArgMatey.OptionSink;
import argmatey.ArgMatey.OptionUsageParams;
import argmatey.ArgMatey.OptionUsageProvider;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.PosixOption;
import jargyle.client.Scheme;
import jargyle.client.socks5.DefaultUsernamePasswordRequestor;
import jargyle.client.socks5.UsernamePassword;
import jargyle.client.socks5.UsernamePasswordRequestor;
import jargyle.common.cli.HelpTextParams;
import jargyle.common.net.SocketSettingSpec;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevel;
import jargyle.server.socks5.UsernamePasswordAuthenticator;
import jargyle.server.socks5.Users;

public final class SocksServerCli {

	public static final class CriteriaOptionUsageProvider 
		extends OptionUsageProvider {
		
		public CriteriaOptionUsageProvider() { }

		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			return String.format(
					"%1$s=[%2$s:%3$s1[ %2$s:%3$s2[...]]]", 
					params.getOption(),
					"equals|matches",
					"VALUE");
		}
		
	}
	
	public static final class SettingsGnuLongOptionUsageProvider 
		extends OptionUsageProvider {
		
		public SettingsGnuLongOptionUsageProvider() { }

		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			return String.format(
					"%1$s=[%2$s1=%3$s1[,%2$s2=%3$s2[...]]]", 
					params.getOption(),
					"NAME",
					"VALUE");
		}
		
	}
	
	public static final class SettingsPosixOptionUsageProvider 
		extends OptionUsageProvider {
		
		public SettingsPosixOptionUsageProvider() { }

		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			return String.format(
					"%1$s [%2$s1=%3$s1[,%2$s2=%3$s2[...]]]",
					params.getOption(),
					"NAME",
					"VALUE");
		}
		
		
	}
	
	public static final class UsernamePasswordAuthenticatorOptionUsageProvider 
		extends OptionUsageProvider {
		
		public UsernamePasswordAuthenticatorOptionUsageProvider() { }

		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			return String.format("%s=CLASSNAME[:VALUE]", params.getOption());
		}
		
	}
	
	public static final class UsernamePasswordOptionUsageProvider
		extends OptionUsageProvider {
				
		public UsernamePasswordOptionUsageProvider() { }

		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			return String.format("%s=USERNAME:PASSWORD", params.getOption());
		}
		
	}
	
	private static final int ALLOWED_CLIENT_ADDR_CRITERIA_OPTION_ORDINAL = 0;
	private static final int ALLOWED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_ORDINAL = 1;
	private static final int ALLOWED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_ORDINAL = 2;
	private static final int BLOCKED_CLIENT_ADDR_CRITERIA_OPTION_ORDINAL = 3;
	private static final int BLOCKED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_ORDINAL = 4;
	private static final int BLOCKED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_ORDINAL = 5;
	private static final int CONFIG_FILE_OPTION_ORDINAL = 6;
	private static final int CONFIG_FILE_XSD_OPTION_ORDINAL = 7;
	private static final int ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_ORDINAL = 8;
	private static final int EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_ORDINAL = 9;
	private static final int HELP_OPTION_ORDINAL = 10;
	private static final int MONITORED_CONFIG_FILE_OPTION_ORDINAL = 11;
	private static final int NEW_CONFIG_FILE_OPTION_ORDINAL = 12;
	private static final int SETTINGS_HELP_OPTION_ORDINAL = 13;
	private static final int SETTINGS_OPTION_ORDINAL = 14;
	private static final int SOCKS5_USER_PASS_AUTHENTICATOR_OPTION_ORDINAL = 15;
	private static final int SOCKS5_USERS_OPTION_ORDINAL = 16;
	
	private static final Logger LOGGER = Logger.getLogger(
			SocksServerCli.class.getName());
	
	private ArgsParser argsParser;
	private boolean configurationFileXsdRequested;
	private final ModifiableConfiguration modifiableConfiguration;
	private String monitoredConfigurationFile;
	private boolean newConfigurationFileRequested;
	private final Options options;
	private final String programBeginningUsage;
	private boolean programHelpRequested;
	private final String programName;	
	private boolean settingsHelpRequested;
	
	SocksServerCli() {
		Options opts = Options.newInstanceFrom(this.getClass());
		String progName = System.getProperty(
				SystemPropertyNameConstants.PROGRAM_NAME);
		if (progName == null) {
			progName = SocksServer.class.getName();
		}
		String progBeginningUsage = System.getProperty(
				SystemPropertyNameConstants.PROGRAM_BEGINNING_USAGE);
		if (progBeginningUsage == null) {
			progBeginningUsage = progName;
		}
		this.argsParser = null;
		this.configurationFileXsdRequested = false;
		this.modifiableConfiguration = new ModifiableConfiguration();
		this.monitoredConfigurationFile = null;
		this.newConfigurationFileRequested = false;
		this.options = opts;
		this.programBeginningUsage = progBeginningUsage;
		this.programHelpRequested = false;
		this.programName = progName;
		this.settingsHelpRequested = false;
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The space separated list of allowed client address "
							+ "criteria",
					name = "allowed-client-addr-criteria",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					ordinal = ALLOWED_CLIENT_ADDR_CRITERIA_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void addAllowedClientAddressCriteria(
			final Criteria allowedClientAddrCriteria) {
		this.modifiableConfiguration.addAllowedClientAddressCriteria(
				allowedClientAddrCriteria);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The space separated list of allowed SOCKS5 "
							+ "incoming TCP address criteria",
					name = "allowed-socks5-incoming-tcp-addr-criteria",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					ordinal = ALLOWED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void addAllowedSocks5IncomingTcpAddressCriteria(
			final Criteria allowedSocks5IncomingTcpAddrCriteria) {
		this.modifiableConfiguration.addAllowedSocks5IncomingTcpAddressCriteria(
				allowedSocks5IncomingTcpAddrCriteria);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The space separated list of allowed SOCKS5 "
							+ "incoming UDP address criteria",
					name = "allowed-socks5-incoming-udp-addr-criteria",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					ordinal = ALLOWED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void addAllowedSocks5IncomingUdpAddressCriteria(
			final Criteria allowedSocks5IncomingUdpAddrCriteria) {
		this.modifiableConfiguration.addAllowedSocks5IncomingUdpAddressCriteria(
				allowedSocks5IncomingUdpAddrCriteria);
	}

	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The space separated list of blocked client address "
							+ "criteria",
					name = "blocked-client-addr-criteria",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					ordinal = BLOCKED_CLIENT_ADDR_CRITERIA_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void addBlockedClientAddressCriteria(
			final Criteria blockedClientAddrCriteria) {
		this.modifiableConfiguration.addBlockedClientAddressCriteria(
				blockedClientAddrCriteria);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The space separated list of blocked SOCKS5 "
							+ "incoming TCP address criteria",
					name = "blocked-socks5-incoming-tcp-addr-criteria",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					ordinal = BLOCKED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void addBlockedSocks5IncomingTcpAddressCriteria(
			final Criteria blockedSocks5IncomingTcpAddrCriteria) {
		this.modifiableConfiguration.addBlockedSocks5IncomingTcpAddressCriteria(
				blockedSocks5IncomingTcpAddrCriteria);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The space separated list of blocked SOCKS5 "
							+ "incoming UDP address criteria",
					name = "blocked-socks5-incoming-udp-addr-criteria",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					ordinal = BLOCKED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void addBlockedSocks5IncomingUdpAddressCriteria(
			final Criteria blockedSocks5IncomingUdpAddrCriteria) {
		this.modifiableConfiguration.addBlockedSocks5IncomingUdpAddressCriteria(
				blockedSocks5IncomingUdpAddrCriteria);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The configuration file",
					name = "config-file",
					optionArgSpecBuilder = @OptionArgSpecBuilder(
							name = "FILE"
					),
					ordinal = CONFIG_FILE_OPTION_ORDINAL,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "f",
							type = PosixOption.class
					)
			}
	)
	public void addConfigurationFile(final String file)	throws IOException {
		InputStream in = null;
		if (file.equals("-")) {
			in = System.in;
		} else {
			File f = new File(file);
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}
		Configuration configuration = null;
		try {
			configuration = ImmutableConfiguration.newInstanceFrom(in);
		} catch (JAXBException e) { 
			throw new IllegalArgumentException(String.format(
					"possible invalid XML file '%s'", file), 
					e);
		}  finally {
			if (in instanceof FileInputStream) {
				in.close();
			}
		}
		this.modifiableConfiguration.add(configuration);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The comma separated list of settings for the SOCKS "
							+ "server",
					name = "settings",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = SettingsGnuLongOptionUsageProvider.class,
					ordinal = SETTINGS_OPTION_ORDINAL,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "s",
							optionUsageProvider = SettingsPosixOptionUsageProvider.class,
							type = PosixOption.class
					)
			}
	)
	public void addSettings(final Settings sttngs) {
		this.modifiableConfiguration.addSettings(sttngs);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Mode for managing SOCKS5 users (add --help for "
							+ "more information)",
					name = "socks5-users",
					ordinal = SOCKS5_USERS_OPTION_ORDINAL,
					special = true,
					type = GnuLongOption.class
			)
	)
	public void doSocks5UsersManagementMode() {
		if (this.argsParser == null) {
			throw new IllegalStateException(
					"process(java.lang.String[]) must be run first");
		}
		Option socks5UsersOption = this.options.toList().get(
				SOCKS5_USERS_OPTION_ORDINAL);
		String newProgramBeginningUsage = String.format("%s %s", 
				this.programBeginningUsage, 
				socks5UsersOption.getUsage());
		System.setProperty(
				SystemPropertyNameConstants.PROGRAM_NAME, 
				this.programName);
		System.setProperty(
				SystemPropertyNameConstants.PROGRAM_BEGINNING_USAGE, 
				newProgramBeginningUsage);
		List<String> remainingArgList = new ArrayList<String>();
		while (this.argsParser.hasNext()) {
			String arg = this.argsParser.next();
			remainingArgList.add(arg);
		}
		Users.main(remainingArgList.toArray(
				new String[remainingArgList.size()]));
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Enter through an interactive prompt the username "
							+ "password for the external SOCKS5 server for "
							+ "external connections",
					name = "enter-external-client-socks5-user-pass",
					ordinal = ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void enterExternalClientSocks5UsernamePassword() {
		String prompt = "Please enter username and password for the external "
				+ "SOCKS5 server for external connections";
		UsernamePasswordRequestor usernamePasswordRequestor = 
				new DefaultUsernamePasswordRequestor();
		UsernamePassword usernamePassword = 
				usernamePasswordRequestor.requestUsernamePassword(null, prompt);
		this.modifiableConfiguration.setExternalClientSocks5UsernamePassword(
				usernamePassword);
	}
	
	private Configuration newConfiguration() {
		Configuration configuration = null;
		if (this.monitoredConfigurationFile == null) {
			configuration = ImmutableConfiguration.newInstance(
					this.modifiableConfiguration);
		} else {
			File f = new File(this.monitoredConfigurationFile);
			ConfigurationService configurationService = null;
			try {
				configurationService = 
						XmlFileSourceConfigurationService.newInstance(f);
			} catch (IllegalArgumentException e) {
				System.err.printf("%s: %s%n", this.programName, e);
				e.printStackTrace(System.err);
				return null;
			}
			configuration = new MutableConfiguration(configurationService);
		}
		return configuration;
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Create a new configuration file based on the "
							+ "preceding options and exit",
					name = "new-config-file",
					optionArgSpecBuilder = @OptionArgSpecBuilder(
							name = "FILE"
					),
					ordinal = NEW_CONFIG_FILE_OPTION_ORDINAL,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "n",
							type = PosixOption.class
					)
			}
	)
	public void newConfigurationFile(final String file) 
			throws JAXBException, IOException {
		ImmutableConfiguration immutableConfiguration = 
				ImmutableConfiguration.newInstance(
						this.modifiableConfiguration);
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
		try {
			byte[] xml = immutableConfiguration.toXml();
			out.write(xml);
			out.flush();
		} finally {
			if (out instanceof FileOutputStream) {
				out.close();
			}
		}
		if (!file.equals("-")) {
			File f = new File(file);
			File tempFile = new File(tempArg);
			if (!tempFile.renameTo(f)) {
				throw new IOException(String.format(
						"unable to rename '%s' to '%s'", tempFile, f));
			}
		}
		this.newConfigurationFileRequested = true;
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Print the configuration file XSD and exit",
					name = "config-file-xsd",
					ordinal = CONFIG_FILE_XSD_OPTION_ORDINAL,
					special = true,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "x",
							type = PosixOption.class
					)
			}
	)
	public void printConfigurationFileXsd() throws JAXBException, IOException {
		byte[] xsd = ImmutableConfiguration.getXsd();
		System.out.write(xsd);
		System.out.flush();
		this.configurationFileXsdRequested = true;
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Print this help and exit",
					name = "help",
					ordinal = HELP_OPTION_ORDINAL,
					special = true,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "h",
							type = PosixOption.class
					)
			}
	)
	public void printHelp() {
		Option configFileXsdOption = this.options.toList().get(
				CONFIG_FILE_XSD_OPTION_ORDINAL);
		Option helpOption = this.options.toList().get(HELP_OPTION_ORDINAL);
		Option monitoredConfigFileOption = this.options.toList().get(
				MONITORED_CONFIG_FILE_OPTION_ORDINAL);
		Option newConfigFileOption = this.options.toList().get(
				NEW_CONFIG_FILE_OPTION_ORDINAL);
		Option settingsHelpOption = this.options.toList().get(
				SETTINGS_HELP_OPTION_ORDINAL);
		Option socks5UsersOption = this.options.toList().get(
				SOCKS5_USERS_OPTION_ORDINAL);
		System.out.printf("Usage: %s [OPTIONS]%n", this.programBeginningUsage);
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				configFileXsdOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				monitoredConfigFileOption.getUsage());		
		System.out.printf("       %s [OPTIONS] %s%n", 
				this.programBeginningUsage, 
				newConfigFileOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		System.out.printf("       %s %s ARGS", 
				this.programBeginningUsage, 
				socks5UsersOption.getUsage());
		System.out.println();
		System.out.println();
		System.out.println("OPTIONS:");
		this.options.printHelpText();
		System.out.println();
		System.out.println();
		this.programHelpRequested = true;
	}
	
	private void printHelpText(final List<HelpTextParams> list) {
		System.out.println();
		for (HelpTextParams helpTextParams : list) {
			System.out.print("  ");
			System.out.println(helpTextParams.getUsage());
			System.out.print("      ");
			System.out.println(helpTextParams.getDoc());
			System.out.println();
		}
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Print the list of available settings for the SOCKS "
							+ "server and exit",
					name = "settings-help",
					ordinal = SETTINGS_HELP_OPTION_ORDINAL,
					special = true,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "H",
							type = PosixOption.class
					)
			}
	)
	public void printSettingsHelp() {
		System.out.println("SETTINGS:");
		this.printHelpText(Arrays.asList(SettingSpec.values()));
		System.out.println("SCHEMES:");
		this.printHelpText(Arrays.asList(Scheme.values()));
		System.out.println("SOCKET_SETTINGS:");
		this.printHelpText(Arrays.asList(SocketSettingSpec.values()));
		System.out.println("SOCKS5_AUTH_METHODS:");
		this.printHelpText(Arrays.asList(AuthMethod.values()));
		System.out.println("SOCKS5_GSSAPI_PROTECTION_LEVELS:");
		this.printHelpText(Arrays.asList(GssapiProtectionLevel.values()));
		this.settingsHelpRequested = true;
	}
	
	public int process(final String[] args) {
		Option settingsOption = this.options.toList().get(
				SETTINGS_OPTION_ORDINAL);
		Option settingsHelpOption = this.options.toList().get(
				SETTINGS_HELP_OPTION_ORDINAL);
		String settingsHelpSuggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		Option helpOption = this.options.toList().get(HELP_OPTION_ORDINAL);
		String suggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		this.argsParser = ArgsParser.newInstance(args, this.options, false);
		while (this.argsParser.hasNext()) {
			try {
				this.argsParser.parseNextTo(this);
			} catch (IllegalOptionArgException e) {
				String suggest = suggestion;
				if (settingsOption.getAllOptions().contains(e.getOption())) {
					suggest = settingsHelpSuggestion;
				}
				System.err.printf("%s: %s%n", this.programName, e);
				System.err.println(suggest);
				e.printStackTrace(System.err);
				return -1;
			} catch (Throwable t) {
				System.err.printf("%s: %s%n", this.programName, t);
				System.err.println(suggestion);
				t.printStackTrace(System.err);
				return -1;
			}
			if (this.configurationFileXsdRequested
					|| this.newConfigurationFileRequested
					|| this.programHelpRequested
					|| this.settingsHelpRequested) {
				return 0;
			}
		}
		Configuration configuration = this.newConfiguration();
		if (configuration == null) { return -1;	}
		return (this.startSocksServer(configuration) != 0) ? -1 : 0;
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The username password for the external SOCKS5 "
							+ "server for external connections",
					name = "external-client-socks5-user-pass",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = UsernamePasswordOptionUsageProvider.class,
					ordinal = EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void setExternalClientSocks5UsernamePassword(
			final UsernamePassword usernamePassword) {
		this.modifiableConfiguration.setExternalClientSocks5UsernamePassword(
				usernamePassword);
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The configuration file to be monitored for any "
							+ "changes to be applied to the running "
							+ "configuration",
					name = "monitored-config-file",
					optionArgSpecBuilder = @OptionArgSpecBuilder(
							name = "FILE"
					),
					ordinal = MONITORED_CONFIG_FILE_OPTION_ORDINAL,
					special = true,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "m",
							type = PosixOption.class
					)
			}
	)
	public void setMonitoredConfigurationFile(final String file) {
		this.monitoredConfigurationFile = file;
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "The SOCKS5 username password authenticator for the "
							+ "SOCKS server",
					name = "socks5-user-pass-authenticator",
					optionArgSpecBuilder = @OptionArgSpecBuilder(),
					optionUsageProvider = UsernamePasswordAuthenticatorOptionUsageProvider.class,
					ordinal = SOCKS5_USER_PASS_AUTHENTICATOR_OPTION_ORDINAL,
					type = GnuLongOption.class
			)
	)
	public void setSocks5UsernamePasswordAuthenticator(
			final UsernamePasswordAuthenticator usernamePasswordAuthenticator) {
		this.modifiableConfiguration.setSocks5UsernamePasswordAuthenticator(
				usernamePasswordAuthenticator);
	}
	
	private int startSocksServer(final Configuration configuration) {
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
		} catch (BindException e) {
			LOGGER.log(
					Level.SEVERE, 
					String.format(
							"Unable to listen on port %s at %s", 
							configuration.getSettings().getLastValue(
									SettingSpec.PORT, Port.class),
							configuration.getSettings().getLastValue(
									SettingSpec.HOST, Host.class)), 
					e);
			return -1;
		} catch (IOException e) {
			LOGGER.log(
					Level.SEVERE, 
					"Error in starting SocksServer", 
					e);
			return -1;
		}
		LOGGER.log(
				Level.INFO,
				String.format(
						"Listening on port %s at %s",
						socksServer.getPort(),
						socksServer.getHost()));
		return 0;
	}

}