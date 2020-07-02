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

import argmatey.ArgMatey;
import argmatey.ArgMatey.Annotations.Option;
import argmatey.ArgMatey.Annotations.OptionArgSpec;
import argmatey.ArgMatey.Annotations.OptionGroup;
import argmatey.ArgMatey.ArgsHandler;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.IllegalOptionArgException;
import argmatey.ArgMatey.OptionGroups;
import argmatey.ArgMatey.OptionUsageParams;
import argmatey.ArgMatey.OptionUsageProvider;
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
import jargyle.server.socks5.UsersCli;

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
	
	private static final int ALLOWED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 0;
	private static final int ALLOWED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 1;
	private static final int ALLOWED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 2;
	private static final int BLOCKED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 3;
	private static final int BLOCKED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 4;
	private static final int BLOCKED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL = 5;
	private static final int CONFIG_FILE_OPTION_GROUP_ORDINAL = 6;
	private static final int CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL = 7;
	private static final int ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL = 8;
	private static final int EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL = 9;
	private static final int HELP_OPTION_GROUP_ORDINAL = 10;
	private static final int MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL = 11;
	private static final int NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL = 12;
	private static final int SETTINGS_HELP_OPTION_GROUP_ORDINAL = 13;
	private static final int SETTINGS_OPTION_GROUP_ORDINAL = 14;
	private static final int SOCKS5_USER_PASS_AUTHENTICATOR_OPTION_GROUP_ORDINAL = 15;
	private static final int SOCKS5_USERS_OPTION_GROUP_ORDINAL = 16;
	
	private static final Logger LOGGER = Logger.getLogger(
			SocksServerCli.class.getName());
	
	private ArgsHandler argsHandler;
	private boolean configurationFileXsdRequested;
	private final ModifiableConfiguration modifiableConfiguration;
	private String monitoredConfigurationFile;
	private boolean newConfigurationFileRequested;
	private OptionGroups optionGroups;
	private final String programBeginningUsage;
	private boolean programHelpRequested;
	private final String programName;	
	private boolean settingsHelpRequested;
	private Integer socks5UsersManagementModeStatus;
	
	SocksServerCli() {
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
		this.argsHandler = null;
		this.configurationFileXsdRequested = false;
		this.modifiableConfiguration = new ModifiableConfiguration();
		this.monitoredConfigurationFile = null;
		this.newConfigurationFileRequested = false;
		this.optionGroups = null;
		this.programBeginningUsage = progBeginningUsage;
		this.programHelpRequested = false;
		this.programName = progName;
		this.settingsHelpRequested = false;
		this.socks5UsersManagementModeStatus = null;
	}
		
	@OptionGroup(
			option = @Option(
					doc = "The space separated list of allowed client address "
							+ "criteria",
					name = "allowed-client-addr-criteria",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = ALLOWED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL 
	)
	public void addAllowedClientAddressCriteria(
			final Criteria allowedClientAddrCriteria) {
		this.modifiableConfiguration.addAllowedClientAddressCriteria(
				allowedClientAddrCriteria);
	}
		
	@OptionGroup(
			option = @Option(
					doc = "The space separated list of allowed SOCKS5 "
							+ "incoming TCP address criteria",
					name = "allowed-socks5-incoming-tcp-addr-criteria",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = ALLOWED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL 
	)
	public void addAllowedSocks5IncomingTcpAddressCriteria(
			final Criteria allowedSocks5IncomingTcpAddrCriteria) {
		this.modifiableConfiguration.addAllowedSocks5IncomingTcpAddressCriteria(
				allowedSocks5IncomingTcpAddrCriteria);
	}
		
	@OptionGroup(
			option = @Option(
					doc = "The space separated list of allowed SOCKS5 "
							+ "incoming UDP address criteria",
					name = "allowed-socks5-incoming-udp-addr-criteria",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = ALLOWED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL 
	)
	public void addAllowedSocks5IncomingUdpAddressCriteria(
			final Criteria allowedSocks5IncomingUdpAddrCriteria) {
		this.modifiableConfiguration.addAllowedSocks5IncomingUdpAddressCriteria(
				allowedSocks5IncomingUdpAddrCriteria);
	}

	@OptionGroup(
			option = @Option(
					doc = "The space separated list of blocked client address "
							+ "criteria",
					name = "blocked-client-addr-criteria",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = BLOCKED_CLIENT_ADDR_CRITERIA_OPTION_GROUP_ORDINAL 
	)
	public void addBlockedClientAddressCriteria(
			final Criteria blockedClientAddrCriteria) {
		this.modifiableConfiguration.addBlockedClientAddressCriteria(
				blockedClientAddrCriteria);
	}
	
	@OptionGroup(
			option = @Option(
					doc = "The space separated list of blocked SOCKS5 "
							+ "incoming TCP address criteria",
					name = "blocked-socks5-incoming-tcp-addr-criteria",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = BLOCKED_SOCKS5_INCOMING_TCP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL 
	)
	public void addBlockedSocks5IncomingTcpAddressCriteria(
			final Criteria blockedSocks5IncomingTcpAddrCriteria) {
		this.modifiableConfiguration.addBlockedSocks5IncomingTcpAddressCriteria(
				blockedSocks5IncomingTcpAddrCriteria);
	}
		
	@OptionGroup(
			option = @Option(
					doc = "The space separated list of blocked SOCKS5 "
							+ "incoming UDP address criteria",
					name = "blocked-socks5-incoming-udp-addr-criteria",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = CriteriaOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = BLOCKED_SOCKS5_INCOMING_UDP_ADDR_CRITERIA_OPTION_GROUP_ORDINAL 
	)
	public void addBlockedSocks5IncomingUdpAddressCriteria(
			final Criteria blockedSocks5IncomingUdpAddrCriteria) {
		this.modifiableConfiguration.addBlockedSocks5IncomingUdpAddressCriteria(
				blockedSocks5IncomingUdpAddrCriteria);
	}
		
	@OptionGroup(
			option = @Option(
					doc = "The configuration file",
					name = "config-file",
					optionArgSpec = @OptionArgSpec(
							name = "FILE"
					),
					type = GnuLongOption.class
			),
			ordinal = CONFIG_FILE_OPTION_GROUP_ORDINAL,
			otherOptions = {
					@Option(
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
		
	@OptionGroup(
			option = @Option(
					doc = "The comma separated list of settings for the SOCKS "
							+ "server",
					name = "settings",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = SettingsGnuLongOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = SETTINGS_OPTION_GROUP_ORDINAL,
			otherOptions = {
					@Option(
							name = "s",
							optionUsageProvider = SettingsPosixOptionUsageProvider.class,
							type = PosixOption.class
					)
			}
	)
	public void addSettings(final Settings sttngs) {
		this.modifiableConfiguration.addSettings(sttngs);
	}
	
	@OptionGroup(
			option = @Option(
					doc = "Mode for managing SOCKS5 users (add --help for "
							+ "more information)",
					name = "socks5-users",
					special = true,
					type = GnuLongOption.class
			),
			ordinal = SOCKS5_USERS_OPTION_GROUP_ORDINAL
	)
	public void doSocks5UsersManagementMode() {
		ArgMatey.Option socks5UsersOption = this.optionGroups.get(
				SOCKS5_USERS_OPTION_GROUP_ORDINAL).get(0);
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
		while (this.argsHandler.hasNext()) {
			String arg = this.argsHandler.next();
			remainingArgList.add(arg);
		}
		UsersCli usersCli = new UsersCli();
		int status = usersCli.process(remainingArgList.toArray(
				new String[remainingArgList.size()]));
		this.socks5UsersManagementModeStatus = Integer.valueOf(status);
	}
		
	@OptionGroup(
			option = @Option(
					doc = "Enter through an interactive prompt the username "
							+ "password to be used to access the external "
							+ "SOCKS5 server used for external connections",
					name = "enter-external-client-socks5-user-pass",
					type = GnuLongOption.class
			),
			ordinal = ENTER_EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL 
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
		
	@OptionGroup(
			option = @Option(
					doc = "Create a new configuration file based on the "
							+ "preceding options and exit",
					name = "new-config-file",
					optionArgSpec = @OptionArgSpec(
							name = "FILE"
					),
					type = GnuLongOption.class
			),
			ordinal = NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL,
			otherOptions = {
					@Option(
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
	
	@OptionGroup(
			option = @Option(
					doc = "Print the configuration file XSD and exit",
					name = "config-file-xsd",
					special = true,
					type = GnuLongOption.class
			),
			ordinal = CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL,
			otherOptions = {
					@Option(
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
		
	@OptionGroup(
			option = @Option(
					doc = "Print this help and exit",
					name = "help",
					special = true,
					type = GnuLongOption.class
			),
			ordinal = HELP_OPTION_GROUP_ORDINAL,
			otherOptions = {
					@Option(
							name = "h",
							type = PosixOption.class
					)
			}
	)
	public void printHelp() {
		ArgMatey.Option configFileXsdOption = this.optionGroups.get(
				CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option helpOption = this.optionGroups.get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option monitoredConfigFileOption = this.optionGroups.get(
				MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option newConfigFileOption = this.optionGroups.get(
				NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option settingsHelpOption = this.optionGroups.get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option socks5UsersOption = this.optionGroups.get(
				SOCKS5_USERS_OPTION_GROUP_ORDINAL).get(0);
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
		this.optionGroups.printHelpText();
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
		
	@OptionGroup(
			option = @Option(
					doc = "Print the list of available settings for the SOCKS "
							+ "server and exit",
					name = "settings-help",
					special = true,
					type = GnuLongOption.class
			),
			ordinal = SETTINGS_HELP_OPTION_GROUP_ORDINAL,
			otherOptions = {
					@Option(
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
		this.argsHandler = ArgsHandler.newInstance(args, this, false);
		this.optionGroups = this.argsHandler.getOptionGroups();
		ArgMatey.OptionGroup settingsOptionGroup = this.optionGroups.get(
				SETTINGS_OPTION_GROUP_ORDINAL); 
		ArgMatey.Option settingsHelpOption = this.optionGroups.get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		String settingsHelpSuggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		ArgMatey.Option helpOption = this.optionGroups.get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		String suggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		while (this.argsHandler.hasNext()) {
			try {
				this.argsHandler.handleNext();
			} catch (IllegalOptionArgException e) {
				String suggest = suggestion;
				if (settingsOptionGroup.toList().contains(e.getOption())) {
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
			if (this.socks5UsersManagementModeStatus != null) {
				return this.socks5UsersManagementModeStatus.intValue();
			}
		}
		Configuration configuration = this.newConfiguration();
		if (configuration == null) { return -1;	}
		return this.startSocksServer(configuration);
	}
	
	@OptionGroup(
			option = @Option(
					doc = "The username password to be used to access the "
							+ "external SOCKS5 server used for external "
							+ "connections",
					name = "external-client-socks5-user-pass",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = UsernamePasswordOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = EXTERNAL_CLIENT_SOCKS5_USER_PASS_OPTION_GROUP_ORDINAL
	)
	public void setExternalClientSocks5UsernamePassword(
			final UsernamePassword usernamePassword) {
		this.modifiableConfiguration.setExternalClientSocks5UsernamePassword(
				usernamePassword);
	}
		
	@OptionGroup(
			option = @Option(
					doc = "The configuration file to be monitored for any "
							+ "changes to be applied to the running "
							+ "configuration",
					name = "monitored-config-file",
					optionArgSpec = @OptionArgSpec(
							name = "FILE"
					),
					special = true,
					type = GnuLongOption.class
			),
			ordinal = MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL,
			otherOptions = {
					@Option(
							name = "m",
							type = PosixOption.class
					)					
			}
	)
	public void setMonitoredConfigurationFile(final String file) {
		this.monitoredConfigurationFile = file;
	}
		
	@OptionGroup(
			option = @Option(
					doc = "The SOCKS5 username password authenticator for the "
							+ "SOCKS server",
					name = "socks5-user-pass-authenticator",
					optionArgSpec = @OptionArgSpec(),
					optionUsageProvider = UsernamePasswordAuthenticatorOptionUsageProvider.class,
					type = GnuLongOption.class
			),
			ordinal = SOCKS5_USER_PASS_AUTHENTICATOR_OPTION_GROUP_ORDINAL 
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