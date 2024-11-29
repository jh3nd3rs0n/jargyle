package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

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
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.security.SystemPropertyNameConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingDtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSocks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;

public abstract class ServerConfigurationCLI extends CLI {
	
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
	private static final int ENTER_CHAINING_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 1;
	private static final int ENTER_CHAINING_SOCKS5_USERPASSMETHOD_PASS_OPTION_GROUP_ORDINAL = 2;
	private static final int ENTER_CHAINING_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 3;
	private static final int ENTER_CHAINING_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 4;
	private static final int ENTER_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 5;
	private static final int ENTER_PARTIAL_ENCRYPTION_PASS_OPTION_GROUP_ORDINAL = 6;
	private static final int ENTER_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 7;
	private static final int ENTER_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 8;
	private static final int HELP_OPTION_GROUP_ORDINAL = 9;
	private static final int PARTIAL_ENCRYPTION_PASS_OPTION_GROUP_ORDINAL = 10;
	private static final int PARTIAL_ENCRYPTION_PASS_FILE_OPTION_GROUP_ORDINAL = 11;
	private static final int SETTING_OPTION_GROUP_ORDINAL = 12;
	private static final int SETTINGS_HELP_OPTION_GROUP_ORDINAL = 13;
	
	private Configuration configuration;
	private final String programBeginningUsage;
	
	ServerConfigurationCLI(
			final String progName, 
			final String progBeginningUsage,
			final String[] args, 
			final boolean posixCorrect) {
		super(args, posixCorrect);
		String prgName = progName;
		if (prgName == null) {
			prgName = this.getClass().getName();
		}
		String prgBeginningUsage = progBeginningUsage;
		if (prgBeginningUsage == null) {
			prgBeginningUsage = prgName;
		}
		this.programBeginningUsage = prgBeginningUsage;
		this.setProgramName(prgName);
	}
	
	@Option(
			doc = "A configuration file",
			name = "config-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "f",
			type = OptionType.POSIX
	)
	@Ordinal(CONFIG_FILE_OPTION_GROUP_ORDINAL)
	protected void addConfigurationFile(final String file) {
		File f = new File(file);
		if (!f.exists()) {
			throw new IllegalArgumentException(String.format(
					"`%s' not found", 
					file));
		}
		if (!f.isFile()) {
			throw new IllegalArgumentException(String.format(
					"`%s' not a file", 
					file));			
		}
		ConfigurationRepository configurationRepository =
				ConfigurationRepository.newFileSourceInstance(f);
		Configuration configuration = configurationRepository.get();
		this.configuration.addSettings(configuration.getSettings());
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
	protected void addSetting(final Setting<? extends Object> sttng) {
		this.configuration.addSetting(sttng);
	}

	@Override
	protected void beforeHandleArgs() {
		this.configuration = Configuration.newModifiableInstance();
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
		this.printProgramHelp(new PrintWriter(System.out, true));
		throw new TerminationRequestedException(0);
	}
	
	@Override
	protected void displayProgramVersion() 
			throws TerminationRequestedException { 
		throw new UnsupportedOperationException("not implemented");
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
	protected void displaySettingsHelp() throws TerminationRequestedException {
		this.printSettingsHelp(new PrintWriter(System.out, true));
		throw new TerminationRequestedException(0);
	}

	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "trust store for the DTLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-dtls-trust-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL)
	protected void enterChainingDtlsTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "DTLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.configuration.addSetting(setting);
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password to be "
					+ "used to access the other SOCKS5 server",
			name = "enter-chaining-socks5-userpassmethod-pass",
			type = OptionType.GNU_LONG
	)
	@Ordinal(ENTER_CHAINING_SOCKS5_USERPASSMETHOD_PASS_OPTION_GROUP_ORDINAL)
	protected void enterChainingSocks5UserpassMethodPassword() {
		String prompt = "Please enter the password to be used to access the "
				+ "other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD.newSetting(
						encryptedPassword);
		this.configuration.addSetting(setting);		
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the SSL/TLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-ssl-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	protected void enterChainingSslKeyStorePassword() {
		String prompt = "Please enter the password for the key store for the "
				+ "SSL/TLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.configuration.addSetting(setting);
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "trust store for the SSL/TLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-ssl-trust-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL)
	protected void enterChainingSslTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "SSL/TLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.configuration.addSetting(setting);
	}

	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the DTLS connections to the SOCKS "
					+ "server",
			name = "enter-dtls-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	protected void enterDtlsKeyStorePassword() {
		String prompt = "Please enter the password for the key store for the "
				+ "DTLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.configuration.addSetting(setting);		
	}

	@Option(
			doc = "Enter through an interactive prompt the partial password "
					+ "to be used for encryption/decryption",
			name = "enter-partial-encryption-pass",
			type = OptionType.GNU_LONG
	)
	@Ordinal(ENTER_PARTIAL_ENCRYPTION_PASS_OPTION_GROUP_ORDINAL)
	protected void enterPartialEncryptionPassword() {
		String prompt = "Please enter the partial password to be used for "
				+ "encryption/decryption: ";
		char[] password = this.readPassword(prompt);
		System.setProperty(
				SystemPropertyNameConstants.PARTIAL_ENCRYPTION_PASSWORD_SYSTEM_PROPERTY_NAME,
				new String(password));
	}

	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the SSL/TLS connections to the SOCKS "
					+ "server",
			name = "enter-ssl-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	protected void enterSslKeyStorePassword() {
		String prompt = "Please enter the password for the key store for the "
				+ "SSL/TLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.configuration.addSetting(setting);		
	}
		
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "trust store for the SSL/TLS connections to the SOCKS "
					+ "server",
			name = "enter-ssl-trust-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL)
	protected void enterSslTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "SSL/TLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.configuration.addSetting(setting);		
	}
	
	protected final Configuration getConfiguration() {
		return Configuration.newUnmodifiableInstance(this.configuration);
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
	
	final void printProgramHelp(final PrintWriter pw) {
		String progOperandsUsage = this.getProgramOperandsUsage();
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option settingsHelpOption = this.getOptionGroups().get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		pw.printf("Usage: %s [OPTIONS]", 
				this.programBeginningUsage);
		if (progOperandsUsage != null && !progOperandsUsage.isEmpty()) {
			pw.printf(" %s", progOperandsUsage);
		}
		pw.println();
		pw.printf("       %s %s%n", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		pw.printf("       %s %s%n", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		pw.println();
		pw.println("OPTIONS:");
		this.getOptionGroups().printHelpText(pw);
		pw.println();
	}
	
	void printSettingsHelp(final PrintWriter pw) {
		new SettingsHelpPrinter().printSettingsHelp(pw);
	}
	
	private EncryptedPassword readEncryptedPassword(final String prompt) {
		return EncryptedPassword.newInstance(this.readPassword(prompt));
	}

	private char[] readPassword(final String prompt) {
		ConsoleWrapper consoleWrapper = new ConsoleWrapper(System.console());
		return consoleWrapper.readPassword(prompt);
	}

	@Option(
			doc = "The partial password to be used for encryption/decryption",
			name = "partial-encryption-pass",
			optionArgSpec = @OptionArgSpec(name = "PASSWORD"),
			type = OptionType.GNU_LONG
	)
	@Ordinal(PARTIAL_ENCRYPTION_PASS_OPTION_GROUP_ORDINAL)
	protected void setPartialEncryptionPassword(final String password) {
		System.setProperty(
				SystemPropertyNameConstants.PARTIAL_ENCRYPTION_PASSWORD_SYSTEM_PROPERTY_NAME,
				password);
	}

	@Option(
			doc = "The file of the partial password to be used for "
					+ "encryption/decryption",
			name = "partial-encryption-pass-file",
			optionArgSpec = @OptionArgSpec(name = "FILE"),
			type = OptionType.GNU_LONG
	)
	@Ordinal(PARTIAL_ENCRYPTION_PASS_FILE_OPTION_GROUP_ORDINAL)
	protected void setPartialEncryptionPasswordFile(final String file) {
		System.setProperty(
				SystemPropertyNameConstants.PARTIAL_ENCRYPTION_PASSWORD_FILE_SYSTEM_PROPERTY_NAME,
				file);
	}

}
