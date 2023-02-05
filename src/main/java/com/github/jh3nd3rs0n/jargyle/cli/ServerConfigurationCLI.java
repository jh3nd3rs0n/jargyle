package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.File;
import java.lang.reflect.Field;

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
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.ChainingDtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingGeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSocks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.ModifiableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;

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
	private static final int ENTER_CHAINING_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 1;
	private static final int ENTER_CHAINING_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 2;
	private static final int ENTER_CHAINING_SOCKS5_USERPASSAUTH_PASS_OPTION_GROUP_ORDINAL = 3;
	private static final int ENTER_CHAINING_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 4;
	private static final int ENTER_CHAINING_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 5;	
	private static final int ENTER_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 6;
	private static final int ENTER_DTLS_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 7;
	private static final int ENTER_SSL_KEY_STORE_PASS_OPTION_GROUP_ORDINAL = 8;
	private static final int ENTER_SSL_TRUST_STORE_PASS_OPTION_GROUP_ORDINAL = 9;
	private static final int HELP_OPTION_GROUP_ORDINAL = 10;
	private static final int SETTING_OPTION_GROUP_ORDINAL = 11;
	private static final int SETTINGS_HELP_OPTION_GROUP_ORDINAL = 12;
	
	private ModifiableConfiguration modifiableConfiguration;
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
				ConfigurationRepository.newInstance(f);
		Configuration configuration = configurationRepository.get();
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
	protected void addSetting(final Setting<? extends Object> sttng) {
		this.modifiableConfiguration.addSetting(sttng);
	}

	@Override
	protected void beforeHandleArgs() {
		this.modifiableConfiguration = ModifiableConfiguration.newInstance();
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
		String progOperandsUsage = this.getProgramOperandsUsage();
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option settingsHelpOption = this.getOptionGroups().get(
				SETTINGS_HELP_OPTION_GROUP_ORDINAL).get(0);
		System.out.printf("Usage: %s [OPTIONS]", 
				this.programBeginningUsage);
		if (progOperandsUsage != null && !progOperandsUsage.isEmpty()) {
			System.out.printf(" %s", progOperandsUsage);
		}
		System.out.println();
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				settingsHelpOption.getUsage());
		System.out.println();
		System.out.println("OPTIONS:");
		this.getOptionGroups().printHelpText();
		System.out.println();
		throw new TerminationRequestedException(0);
	}
	
	@Override
	protected void displayProgramVersion() 
			throws TerminationRequestedException { 
		throw new UnsupportedOperationException("not implemented");
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password for the "
					+ "key store for the DTLS connections to the other "
					+ "SOCKS server",
			name = "enter-chaining-dtls-key-store-pass",
			type = OptionType.GNU_LONG
	)	
	@Ordinal(ENTER_CHAINING_DTLS_KEY_STORE_PASS_OPTION_GROUP_ORDINAL)
	protected void enterChainingDtlsKeyStorePassword() {
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
	protected void enterChainingDtlsTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "DTLS connections to the other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);
	}
	
	@Option(
			doc = "Enter through an interactive prompt the password to be "
					+ "used to access the other SOCKS5 server",
			name = "enter-chaining-socks5-userpassauth-pass",
			type = OptionType.GNU_LONG
	)
	@Ordinal(ENTER_CHAINING_SOCKS5_USERPASSAUTH_PASS_OPTION_GROUP_ORDINAL)
	protected void enterChainingSocks5UserpassauthPassword() {
		String prompt = "Please enter the password to be used to access the "
				+ "other SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_PASSWORD.newSetting(
						encryptedPassword);
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
	protected void enterChainingSslKeyStorePassword() {
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
	protected void enterChainingSslTrustStorePassword() {
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
	protected void enterDtlsKeyStorePassword() {
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
	protected void enterDtlsTrustStorePassword() {
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
	protected void enterSslKeyStorePassword() {
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
	protected void enterSslTrustStorePassword() {
		String prompt = "Please enter the password for the trust store for the "
				+ "SSL/TLS connections to the SOCKS server: ";
		EncryptedPassword encryptedPassword = readEncryptedPassword(prompt);
		Setting<EncryptedPassword> setting = 
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSetting(
						encryptedPassword);
		this.modifiableConfiguration.addSetting(setting);		
	}
	
	protected final Configuration getConfiguration() {
		return ImmutableConfiguration.newInstance(
				this.modifiableConfiguration.getSettings());
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
	
	private void printHelpText(final Class<?> cls) {
		System.out.println();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			HelpText helpText = field.getAnnotation(HelpText.class);
			if (helpText != null) {
				System.out.print("    ");
				System.out.println(helpText.usage());
				String doc = helpText.doc();
				if (!doc.isEmpty()) {
					System.out.print("        ");
					System.out.println(doc);
				}
				System.out.println();
			}
		}
	}
	
	private void printSettings() {
		System.out.println("SETTINGS:");
		System.out.println();		
		System.out.println("  GENERAL SETTINGS:");
		this.printHelpText(GeneralSettingSpecConstants.class);
		System.out.println("  CHAINING GENERAL SETTINGS:");
		this.printHelpText(ChainingGeneralSettingSpecConstants.class);
		System.out.println("  CHAINING DTLS SETTINGS:");
		this.printHelpText(ChainingDtlsSettingSpecConstants.class);
		System.out.println("  CHAINING SOCKS5 SETTINGS:");
		this.printHelpText(ChainingSocks5SettingSpecConstants.class);
		System.out.println("  CHAINING SSL SETTINGS:");
		this.printHelpText(ChainingSslSettingSpecConstants.class);		
		System.out.println("  DTLS SETTINGS:");
		this.printHelpText(DtlsSettingSpecConstants.class);
		System.out.println("  SOCKS5 SETTINGS:");
		this.printHelpText(Socks5SettingSpecConstants.class);
		System.out.println("  SSL SETTINGS:");
		this.printHelpText(SslSettingSpecConstants.class);
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
	protected void printSettingsHelp() throws TerminationRequestedException {
		this.printSettings();
		this.printSettingValueSyntaxes();
		throw new TerminationRequestedException(0);
	}
	
	private void printSettingValueSyntaxes() {
		System.out.println("SETTING VALUE SYNTAXES:");
		System.out.println();
		System.out.println("  FIREWALL_ACTIONS:");
		this.printHelpText(FirewallAction.class);
		System.out.println("  GENERAL_RULE_CONDITIONS:");
		this.printHelpText(GeneralRuleConditionSpecConstants.class);
		System.out.println("  GENERAL_RULE_RESULTS:");
		this.printHelpText(GeneralRuleResultSpecConstants.class);
		System.out.println("  LOG_ACTIONS:");
		this.printHelpText(LogAction.class);
		System.out.println("  SCHEMES:");
		this.printHelpText(Scheme.class);
		System.out.println("  SELECTION_STRATEGIES:");
		this.printHelpText(SelectionStrategy.class);
		System.out.println("  SOCKET_SETTINGS:");
		this.printHelpText(StandardSocketSettingSpecConstants.class);
		System.out.println("  SOCKS5_COMMANDS:");
		this.printHelpText(Command.class);
		System.out.println("  SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS:");
		this.printHelpText(ProtectionLevel.class);
		System.out.println("  SOCKS5_METHODS:");
		this.printHelpText(Method.class);
		System.out.println("  SOCKS5_RULE_CONDITIONS:");
		this.printHelpText(Socks5RuleConditionSpecConstants.class);		
		System.out.println("  SOCKS5_RULE_RESULTS:");
		this.printHelpText(Socks5RuleResultSpecConstants.class);
	}
	
	private EncryptedPassword readEncryptedPassword(final String prompt) {
		ConsoleWrapper consoleWrapper = new ConsoleWrapper(System.console());
		return EncryptedPassword.newInstance(consoleWrapper.readPassword(
				prompt));
	}

}
