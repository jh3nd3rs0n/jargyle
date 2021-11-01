package com.github.jh3nd3rs0n.jargyle.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.BindException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.argmatey.ArgMatey;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Option;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.OptionArgSpec;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Ordinal;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.IllegalOptionArgException;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionType;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionUsageParams;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionUsageProvider;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.socks5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.internal.io.ConsoleWrapper;
import com.github.jh3nd3rs0n.jargyle.main.socks5.userpassauth.UsersCLI;
import com.github.jh3nd3rs0n.jargyle.server.ChainingDtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingGeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSocks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationProvider;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.ModifiableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.MutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.XmlFileSourceConfigurationProvider;
import com.github.jh3nd3rs0n.jargyle.server.config.xml.bind.ConfigurationXml;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.SchemaOutputResolver;

public final class JargyleCLI extends CLI {
	
	private static final class CustomSchemaOutputResolver 
		extends SchemaOutputResolver {
	
		private final Result result;
		
		public CustomSchemaOutputResolver(final Result res) {
			this.result = res;
		}
		
		@Override
		public Result createOutput(
				final String namespaceUri, 
				final String suggestedFileName) throws IOException {
			return this.result;
		}
		
	}
	
	private static final class SettingGnuLongOptionUsageProvider 
		extends OptionUsageProvider {

		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			return String.format("%s=NAME=VALUE", params.getOption());
		}
		
	}
	
	private static final class SettingPosixOptionUsageProvider 
		extends OptionUsageProvider {

		@Override
		public String getOptionUsage(final OptionUsageParams params) {
			return String.format("%s NAME=VALUE", params.getOption());
		}
		
	}

	private static final int CONFIG_FILE_OPTION_GROUP_ORDINAL = 0;
	private static final int CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL = 1;	
	private static final int ENTER_CHAINING_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 2;
	private static final int ENTER_CHAINING_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 3;
	private static final int ENTER_CHAINING_SOCKS5_USERPASSAUTH_USER_PASS_OPTION_GROUP_ORDINAL = 4;
	private static final int ENTER_CHAINING_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 5;
	private static final int ENTER_CHAINING_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 6;	
	private static final int ENTER_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 7;
	private static final int ENTER_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 8;
	private static final int ENTER_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 9;
	private static final int ENTER_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 10;
	private static final int HELP_OPTION_GROUP_ORDINAL = 11;
	private static final int MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL = 12;
	private static final int NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL = 13;
	private static final int SETTING_OPTION_GROUP_ORDINAL = 14;
	private static final int SETTINGS_HELP_OPTION_GROUP_ORDINAL = 15;
	private static final int SOCKS5_USERPASSAUTH_USERS_OPTION_GROUP_ORDINAL = 16;
	private static final int VERSION_OPTION_GROUP_ORDINAL = 17;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			JargyleCLI.class);
	
	public static void main(final String[] args) {
		CLI cli = new JargyleCLI(args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			System.exit(e.getExitStatusCode());
		}
	}
	
	private ModifiableConfiguration modifiableConfiguration;
	private String monitoredConfigurationFile;
	private final boolean posixlyCorrect;
	private final String programBeginningUsage;
	
	public JargyleCLI(final String[] args, final boolean posixCorrect) {
		super(args, posixCorrect);
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
		this.posixlyCorrect = posixCorrect;
		this.programBeginningUsage = progBeginningUsage;
		this.setProgramName(progName);
	}
	
	@Option(
			doc = "The configuration file",
			name = "config-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "f",
			type = OptionType.POSIX
	)
	@Ordinal(CONFIG_FILE_OPTION_GROUP_ORDINAL)
	private void addConfigurationFile(final String file) throws IOException {
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
			configuration = ConfigurationXml.newInstanceFromXml(
					in).toConfiguration();
		} catch (JAXBException e) { 
			throw new IllegalArgumentException(String.format(
					"possible invalid XML file '%s'", file), 
					e);
		}  finally {
			if (in instanceof FileInputStream) {
				in.close();
			}
		}
		this.modifiableConfiguration.addSettings(configuration.getSettings());
	}
	
	@Option(
			doc = "A setting for the SOCKS server",
			name = "setting",
			type = OptionType.GNU_LONG,
			optionUsageProvider = SettingGnuLongOptionUsageProvider.class
	)
	@Option(
			name = "s",
			type = OptionType.POSIX,
			optionUsageProvider = SettingPosixOptionUsageProvider.class
	)
	@Ordinal(SETTING_OPTION_GROUP_ORDINAL)
	private void addSetting(final Setting<? extends Object> sttng) {
		this.modifiableConfiguration.addSetting(sttng);
	}
	
	@Override
	protected void afterHandleArgs() throws TerminationRequestedException {
		Configuration configuration = this.newConfiguration();
		this.startSocksServer(configuration);
	}

	@Override
	protected void beforeHandleArgs() {
		this.modifiableConfiguration = ModifiableConfiguration.newInstance();
		this.monitoredConfigurationFile = null;
	}

	@Option(
			doc = "Print this help and exit",
			name = "help",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "h",
			type = OptionType.POSIX
	)
	@Ordinal(HELP_OPTION_GROUP_ORDINAL)
	@Override
	protected void displayProgramHelp() throws TerminationRequestedException {
		ArgMatey.Option configFileXsdOption = this.getOptionGroups().get(
				CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option monitoredConfigFileOption = this.getOptionGroups().get(
				MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option newConfigFileOption = this.getOptionGroups().get(
				NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option settingsHelpOption = this.getOptionGroups().get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option socks5UserpassauthUsersOption = 
				this.getOptionGroups().get(
						SOCKS5_USERPASSAUTH_USERS_OPTION_GROUP_ORDINAL).get(0);
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
				socks5UserpassauthUsersOption.getUsage());
		System.out.println();
		System.out.println();
		System.out.println("OPTIONS:");
		this.getOptionGroups().printHelpText();
		System.out.println();
		throw new TerminationRequestedException(0);
	}
	
	@Option(
			doc = "Print version information and exit",
			name = "version",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "v",
			type = OptionType.POSIX
	)
	@Ordinal(VERSION_OPTION_GROUP_ORDINAL)	
	@Override
	protected void displayProgramVersion() 
			throws TerminationRequestedException { 
		Package pkg = this.getClass().getPackage();
		System.out.printf(
				"%s %s%n", 
				pkg.getSpecificationTitle(), 
				pkg.getSpecificationVersion());
		throw new TerminationRequestedException(0);
	}
		
	@Option(
			doc = "Mode for managing SOCKS5 users (add --help for "
					+ "more information)",
			name = "socks5-userpassauth-users",
			type = OptionType.GNU_LONG
	)
	@Ordinal(SOCKS5_USERPASSAUTH_USERS_OPTION_GROUP_ORDINAL)
	private void doSocks5UserpassauthUsersManagementMode() 
			throws TerminationRequestedException {
		ArgMatey.Option socks5UserpassauthUsersOption = 
				this.getOptionGroups().get(
						SOCKS5_USERPASSAUTH_USERS_OPTION_GROUP_ORDINAL).get(0);
		String newProgramBeginningUsage = String.format("%s %s", 
				this.programBeginningUsage, 
				socks5UserpassauthUsersOption.getUsage());
		List<String> remainingArgList = new ArrayList<String>();
		while (this.hasNext()) {
			String arg = this.next();
			remainingArgList.add(arg);
		}
		String[] remainingArgs = remainingArgList.toArray(
				new String[remainingArgList.size()]);
		CLI cli = new UsersCLI(
				this.getProgramName(), 
				newProgramBeginningUsage, 
				remainingArgs, 
				this.posixlyCorrect);
		cli.handleArgs();
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the DTLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-dtls-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterChainingDtlsKeyStorePassword() {
		String prompt = "Please enter the password for the key store for the "
				+ "DTLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_KEY_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "trust store for the DTLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-dtls-trust-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterChainingDtlsTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "DTLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);
	}
	
	@Option(
			doc = "Enter through an interactive prompt the username "
					+ "password to be used to access the other SOCKS5 server",
			name = "enter-chaining-socks5-userpassauth-user-pass",
			type = OptionType.GNU_LONG
	)
	@Ordinal(ENTER_CHAINING_SOCKS5_USERPASSAUTH_USER_PASS_OPTION_GROUP_ORDINAL)
	private void enterChainingSocks5UserpassauthUsernamePassword() {
		String prompt = "Please enter the username and password to be used to "
				+ "access the other SOCKS5 server";
		ConsoleWrapper consoleWrapper = new ConsoleWrapper(System.console());
		consoleWrapper.printf(prompt);
		consoleWrapper.printf("%n");
		String username;
		while (true) {
			username = consoleWrapper.readLine("Username: ");
			try {
				UsernamePassword.validateUsername(username);
				break;
			} catch (IllegalArgumentException e) {
				consoleWrapper.printf(
						"Username must be no more than %s byte(s).%n", 
						UsernamePassword.MAX_USERNAME_LENGTH);
			}
		}
		char[] password;
		while (true) {
			password = consoleWrapper.readPassword("Password: ");
			try {
				UsernamePassword.validatePassword(password);
				break;
			} catch (IllegalArgumentException e) {
				consoleWrapper.printf(
						"Password must be no more than %s byte(s).%n", 
						UsernamePassword.MAX_PASSWORD_LENGTH);
			}
		}		
		UsernamePassword usernamePassword = UsernamePassword.newInstance(
				username, password);
		Setting<UsernamePassword> setting = 
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_USERNAME_PASSWORD.newSetting(
						usernamePassword);
		this.modifiableConfiguration.addSetting(setting);
	}
		
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the SSL/TLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-ssl-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterChainingSslKeyStorePassword() {
		String prompt = "Please enter the password for the key store for the "
				+ "SSL/TLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "trust store for the SSL/TLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-ssl-trust-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterChainingSslTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "SSL/TLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);
	}
		
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the DTLS connections to the SOCKS "
					+ "server",
			name = "enter-dtls-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterDtlsKeyStorePassword() {
		String prompt = "Please enter the password for the key store for the "
				+ "DTLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);		
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "trust store for the DTLS connections to the SOCKS "
					+ "server",
			name = "enter-dtls-trust-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterDtlsTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "DTLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);		
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the SSL/TLS connections to the SOCKS "
					+ "server",
			name = "enter-ssl-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterSslKeyStorePassword() {
		String prompt = "Please enter the password for the key store for the "
				+ "SSL/TLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);		
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "trust store for the SSL/TLS connections to the SOCKS "
					+ "server",
			name = "enter-ssl-trust-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL)
	private void enterSslTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "SSL/TLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);		
	}
	
	@Override
	protected void handleThrowable(final Throwable t) 
			throws TerminationRequestedException {
		ArgMatey.OptionGroup settingsOptionGroup = this.getOptionGroups().get(
				SETTING_OPTION_GROUP_ORDINAL); 
		ArgMatey.Option settingsHelpOption = this.getOptionGroups().get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		String settingsHelpSuggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		String suggestion = String.format(
				"Try `%s %s' for more information.", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		if (t instanceof IllegalOptionArgException) {
			IllegalOptionArgException e = (IllegalOptionArgException) t;
			String suggest = suggestion;
			if (settingsOptionGroup.toList().contains(e.getOption())) {
				suggest = settingsHelpSuggestion;
			}
			System.err.printf("%s: %s%n", this.getProgramName(), e);
			System.err.println(suggest);
			e.printStackTrace(System.err);
			throw new TerminationRequestedException(-1);
		}
		System.err.printf("%s: %s%n", this.getProgramName(), t);
		System.err.println(suggestion);
		t.printStackTrace(System.err);
		throw new TerminationRequestedException(-1);
	}
	
	private Configuration newConfiguration() 
			throws TerminationRequestedException {
		Configuration configuration = null;
		if (this.monitoredConfigurationFile == null) {
			configuration = ImmutableConfiguration.newInstance(
					this.modifiableConfiguration);
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
	
	@Option(
			doc = "Create a new configuration file based on the "
					+ "preceding options and exit",
			name = "new-config-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "n",
			type = OptionType.POSIX
	)
	@Ordinal(NEW_CONFIG_FILE_OPTION_GROUP_ORDINAL)
	private void newConfigurationFile(final String file) 
			throws IOException, TerminationRequestedException {
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
		ConfigurationXml configurationXml = new ConfigurationXml(
				immutableConfiguration);
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
		throw new TerminationRequestedException(0);
	}
	
	@Option(
			doc = "Print the configuration file XSD and exit",
			name = "config-file-xsd",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "x",
			type = OptionType.POSIX
	)
	@Ordinal(CONFIG_FILE_XSD_OPTION_GROUP_ORDINAL)
	private void printConfigurationFileXsd() 
			throws IOException, TerminationRequestedException {
		StreamResult result = new StreamResult(System.out);
		result.setSystemId("");
		try {
			ConfigurationXml.generateXsd(
					System.out, new CustomSchemaOutputResolver(result));
		} catch (JAXBException e) {
			throw new IOException(e);
		}
		System.out.flush();
		throw new TerminationRequestedException(0);
	}
	
	private void printHelpText(final Class<?> cls) {
		System.out.println();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			HelpText helpText = field.getAnnotation(HelpText.class);
			if (helpText != null) {
				System.out.print("  ");
				System.out.println(helpText.usage());
				String doc = helpText.doc();
				if (!doc.isEmpty()) {
					System.out.print("      ");
					System.out.println(doc);
				}
				System.out.println();
			}
		}
	}
	
	@Option(
			doc = "Print the list of available settings for the SOCKS "
					+ "server and exit",
			name = "settings-help",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "H",
			type = OptionType.POSIX
	)
	@Ordinal(SETTINGS_HELP_OPTION_GROUP_ORDINAL)
	private void printSettingsHelp() throws TerminationRequestedException {
		System.out.println("GENERAL SETTINGS:");
		this.printHelpText(GeneralSettingSpecConstants.class);
		System.out.println("CHAINING GENERAL SETTINGS:");
		this.printHelpText(ChainingGeneralSettingSpecConstants.class);
		System.out.println("CHAINING DTLS SETTINGS:");
		this.printHelpText(ChainingDtlsSettingSpecConstants.class);
		System.out.println("CHAINING SOCKS5 SETTINGS:");
		this.printHelpText(ChainingSocks5SettingSpecConstants.class);
		System.out.println("CHAINING SSL SETTINGS:");
		this.printHelpText(ChainingSslSettingSpecConstants.class);		
		System.out.println("DTLS SETTINGS:");
		this.printHelpText(DtlsSettingSpecConstants.class);
		System.out.println("SOCKS5 SETTINGS:");
		this.printHelpText(Socks5SettingSpecConstants.class);
		System.out.println("SSL SETTINGS:");
		this.printHelpText(SslSettingSpecConstants.class);
		System.out.println("LOG_ACTIONS:");
		this.printHelpText(LogAction.class);
		System.out.println("RULE_ACTIONS:");
		this.printHelpText(RuleAction.class);
		System.out.println("RULE_FIELDS:");
		this.printHelpText(Rule.class);
		System.out.println("SCHEMES:");
		this.printHelpText(Scheme.class);
		System.out.println("SOCKET_SETTINGS:");
		this.printHelpText(StandardSocketSettingSpecConstants.class);
		System.out.println("SOCKS5_COMMANDS:");
		this.printHelpText(Command.class);		
		System.out.println("SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS:");
		this.printHelpText(ProtectionLevel.class);
		System.out.println("SOCKS5_METHODS:");
		this.printHelpText(Method.class);
		System.out.println("SOCKS5_REQUEST_RULE_FIELDS:");
		this.printHelpText(Socks5RequestRule.class);		
		throw new TerminationRequestedException(0);
	}
		
	private EncryptedPassword readEncryptedPassword(final String prompt) {
		ConsoleWrapper consoleWrapper = new ConsoleWrapper(System.console());
		return EncryptedPassword.newInstance(consoleWrapper.readPassword(
				prompt));
	}

	@Option(
			doc = "The configuration file to be monitored for any "
					+ "changes to be applied to the running "
					+ "configuration",
			name = "monitored-config-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "m",
			type = OptionType.POSIX
	)
	@Ordinal(MONITORED_CONFIG_FILE_OPTION_GROUP_ORDINAL)
	private void setMonitoredConfigurationFile(final String file) {
		this.monitoredConfigurationFile = file;
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
