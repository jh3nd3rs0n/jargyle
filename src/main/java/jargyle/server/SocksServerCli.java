package jargyle.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import argmatey.ArgMatey.ArgsParser;
import argmatey.ArgMatey.IllegalOptionArgException;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.ParseResultHolder;
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

final class SocksServerCli {
	
	private static final class Params {
		
		private final List<Expression> allowedClientAddresses;
		private final List<Expression> blockedClientAddresses;
		private UsernamePassword externalClientSocks5UsernamePassword;
		private boolean modified;
		private File monitoredConfigurationFile;
		private final List<Setting> settings;
		private UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
		
		public Params() {
			this.allowedClientAddresses = new ArrayList<Expression>();
			this.blockedClientAddresses = new ArrayList<Expression>();
			this.externalClientSocks5UsernamePassword = null;
			this.modified = false;
			this.monitoredConfigurationFile = null;
			this.settings = new ArrayList<Setting>();
			this.socks5UsernamePasswordAuthenticator = null;
		}
		
		public Params(final Params other) {
			this.allowedClientAddresses = new ArrayList<Expression>(
					other.allowedClientAddresses);
			this.blockedClientAddresses = new ArrayList<Expression>(
					other.blockedClientAddresses);
			this.externalClientSocks5UsernamePassword = 
					other.externalClientSocks5UsernamePassword;
			this.modified = false;
			this.monitoredConfigurationFile = other.monitoredConfigurationFile;			
			this.settings = new ArrayList<Setting>(other.settings);
			this.socks5UsernamePasswordAuthenticator = 
					other.socks5UsernamePasswordAuthenticator;
		}
		
		public void add(final Configuration configuration) {
			this.addAllowedClientAddresses(
					configuration.getAllowedClientAddresses().toList());
			this.addBlockedClientAddresses(
					configuration.getBlockedClientAddresses().toList());
			this.setExternalClientSocks5UsernamePassword(
					configuration.getExternalClientSocks5UsernamePassword());
			this.addSettings(configuration.getSettings().toList());
			this.setSocks5UsernamePasswordAuthenticator(
					configuration.getSocks5UsernamePasswordAuthenticator());
		}

		public void addAllowedClientAddresses(
				final List<Expression> allowedClientAddrs) {
			if (allowedClientAddrs.isEmpty()) {
				return;
			}
			this.allowedClientAddresses.addAll(allowedClientAddrs);
			this.modified = true;
		}
		
		public void addBlockedClientAddresses(
				final List<Expression> blockedClientAddrs) {
			if (blockedClientAddrs.isEmpty()) {
				return;
			}
			this.blockedClientAddresses.addAll(blockedClientAddrs);
			this.modified = true;
		}
		
		public void addSettings(final List<Setting> sttngs) {
			if (sttngs.isEmpty()) {
				return;
			}
			this.settings.addAll(sttngs);
			this.modified = true;
		}

		public List<Expression> getAllowedClientAddresses() {
			return Collections.unmodifiableList(this.allowedClientAddresses);
		}

		public List<Expression> getBlockedClientAddresses() {
			return Collections.unmodifiableList(this.blockedClientAddresses);
		}
		
		public UsernamePassword getExternalClientSocks5UsernamePassword() {
			return this.externalClientSocks5UsernamePassword;
		}

		public File getMonitoredConfigurationFile() {
			return this.monitoredConfigurationFile;
		}
		
		public List<Setting> getSettings() {
			return Collections.unmodifiableList(this.settings);
		}

		public UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator() {
			return this.socks5UsernamePasswordAuthenticator;
		}
		
		public boolean isModified() {
			return this.modified;
		}
		
		public void setExternalClientSocks5UsernamePassword(
				final UsernamePassword externalClientSocks5UsrnmPsswrd) {
			if (externalClientSocks5UsrnmPsswrd == null) {
				return;
			}
			this.externalClientSocks5UsernamePassword = 
					externalClientSocks5UsrnmPsswrd;
			this.modified = true;
		}
		
		public void setMonitoredConfigurationFile(
				final File monitoredConfigFile) {
			if (monitoredConfigFile == null) {
				return;
			}
			this.monitoredConfigurationFile = monitoredConfigFile;
			this.modified = true;
		}

		public void setSocks5UsernamePasswordAuthenticator(
				final UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator) {
			if (socks5UsrnmPsswrdAuthenticator == null) {
				return;
			}
			this.socks5UsernamePasswordAuthenticator = socks5UsrnmPsswrdAuthenticator;
			this.modified = true;
		}
		
	}
	
	public static final class ProcessResult {
		
		private final Configuration configuration;
		
		private ProcessResult(final Configuration config) {
			this.configuration = config;
		}
		
		public Configuration getConfiguration() {
			return this.configuration;
		}
		
	}
	
	public SocksServerCli() { }
	
	private UnmodifiableConfiguration newConfiguration(
			final SocksServerCli.Params params) {
		UnmodifiableConfiguration.Builder builder = 
				new UnmodifiableConfiguration.Builder();
		if (!params.getAllowedClientAddresses().isEmpty()) {
			builder.allowedClientAddresses(Expressions.newInstance(
					params.getAllowedClientAddresses()));
		}
		if (!params.getBlockedClientAddresses().isEmpty()) {
			builder.blockedClientAddresses(Expressions.newInstance(
					params.getBlockedClientAddresses()));
		}
		if (params.getExternalClientSocks5UsernamePassword() != null) {
			builder.externalClientSocks5UsernamePassword(
					params.getExternalClientSocks5UsernamePassword());
		}
		if (!params.getSettings().isEmpty()) {
			builder.settings(Settings.newInstance(params.getSettings()));
		}
		if (params.getSocks5UsernamePasswordAuthenticator() != null) {
			builder.socks5UsernamePasswordAuthenticator(
					params.getSocks5UsernamePasswordAuthenticator());
		}
		return builder.build();
	}
	
	private void newConfigurationFile(
			final SocksServerCli.Params params,
			final String arg) throws JAXBException, IOException {
		UnmodifiableConfiguration unmodifiableConfiguration = 
				this.newConfiguration(params);
		String tempArg = arg;
		System.out.print("Writing to ");
		OutputStream out = null;
		if (arg.equals("-")) {
			System.out.printf("standard output...%n");
			out = System.out;
		} else {
			File file = new File(arg);
			System.out.printf("'%s'...%n", file.getAbsolutePath());
			File tempFile = null;
			do {
				tempArg = tempArg.concat(".tmp");
				tempFile = new File(tempArg);
			} while (tempFile.exists());
			tempFile.createNewFile();
			out = new FileOutputStream(tempFile);
		}
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(
					UnmodifiableConfiguration.ConfigurationXml.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(unmodifiableConfiguration.toConfigurationXml(), out);
		} finally {
			if (out instanceof FileOutputStream) {
				out.close();
			}
		}
		if (!arg.equals("-")) {
			File file = new File(arg);
			File tempFile = new File(tempArg);
			if (!tempFile.renameTo(file)) {
				throw new IOException(String.format(
						"unable to rename '%s' to '%s'", tempFile, file));
			}
		}
	}

	private void printConfigurationFileXsd() throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				UnmodifiableConfiguration.ConfigurationXml.class);
		jaxbContext.generateSchema(new SchemaOutputResolver() {

			@Override
			public Result createOutput(
					final String namespaceUri, 
					final String suggestedFileName) throws IOException {
				StreamResult result = new StreamResult(System.out);
				result.setSystemId("-");
				return result;
			}
			
		});
	}
	
	private void printHelp(
			final String programBeginningUsage, final Options options) {
		System.out.printf("Usage: %s [OPTIONS]%n", programBeginningUsage);
		System.out.printf("       %s %s%n", 
				programBeginningUsage, 
				SocksServerCliOptions.CONFIG_FILE_XSD_OPTION.getUsage());
		System.out.printf("       %s %s%n", 
				programBeginningUsage, 
				SocksServerCliOptions.HELP_OPTION.getUsage());
		System.out.printf("       %s [OPTIONS] %s%n", 
				programBeginningUsage, 
				SocksServerCliOptions.NEW_CONFIG_FILE_OPTION.getUsage());
		System.out.printf("       %s %s%n", 
				programBeginningUsage, 
				SocksServerCliOptions.SETTINGS_HELP_OPTION.getUsage());
		System.out.printf("       %s %s ARGS", 
				programBeginningUsage, 
				SocksServerCliOptions.SOCKS5_USERS_OPTION.getUsage());
		System.out.println();
		System.out.println();
		System.out.println("OPTIONS:");
		options.printHelpText();
		System.out.println();
		System.out.println();
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
	
	private void printSettingsHelp() {
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
	}
	
	public ProcessResult process(final String[] args) {
		Options options = new SocksServerCliOptions();
		ArgsParser argsParser = ArgsParser.newInstance(
				args, options, false);
		String programName = System.getProperty(
				SystemPropertyNames.PROGRAM_NAME_PROPERTY_NAME);
		if (programName == null) {
			programName = SocksServer.class.getName();
		}
		String programBeginningUsage = System.getProperty(
				SystemPropertyNames.PROGRAM_BEGINNING_USAGE_PROPERTY_NAME);
		if (programBeginningUsage == null) {
			programBeginningUsage = programName;
		}
		String suggestion = String.format(
				"Try `%s %s' for more information", 
				programBeginningUsage, 
				SocksServerCliOptions.HELP_OPTION.getUsage());
		String settingsHelpSuggestion = String.format(
				"Try `%s %s' for more information", 
				programBeginningUsage, 
				SocksServerCliOptions.SETTINGS_HELP_OPTION.getUsage());
		Params params = new Params();
		while (argsParser.hasNext()) {
			ParseResultHolder parseResultHolder = null;
			try {
				parseResultHolder = argsParser.parseNext();
			} catch (RuntimeException e) {
				System.err.printf("%s: %s%n", programName, e.toString());
				Option erringSettingsOption = null;
				if (e instanceof IllegalOptionArgException) {
					IllegalOptionArgException ioae = 
							(IllegalOptionArgException) e;
					Option option = ioae.getOption();
					if (option.isOfAnyOf("--settings", "-s")) {
						erringSettingsOption = option;
					}
				}
				if (erringSettingsOption != null) {
					System.err.println(settingsHelpSuggestion);
				} else {
					System.err.println(suggestion);
				}
				System.exit(-1);
			}
			if (parseResultHolder.hasOptionOf("--allowed-client-addresses")) {
				params.addAllowedClientAddresses(
						parseResultHolder.getOptionArg().getTypeValue(
								Expressions.class).toList());
			}
			if (parseResultHolder.hasOptionOf("--blocked-client-addresses")) {
				params.addBlockedClientAddresses(
						parseResultHolder.getOptionArg().getTypeValue(
								Expressions.class).toList());
			}
			if (parseResultHolder.hasOptionOfAnyOf("--config-file", "-f")) {
				Configuration configuration = null;
				try {
					configuration = this.readConfiguration(
							parseResultHolder.getOptionArg().toString());
				} catch (IOException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				} catch (JAXBException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				}
				params.add(configuration);
			}
			if (parseResultHolder.hasOptionOfAnyOf("--config-file-xsd", "-x")) {
				try {
					this.printConfigurationFileXsd();
				} catch (JAXBException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				} catch (IOException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				}
				System.exit(0);
			}
			if (parseResultHolder.hasOptionOf(
					"--enter-external-client-socks5-user-pass")) {
				params.setExternalClientSocks5UsernamePassword(
						this.readExternalClientSocks5UsernamePassword());
			}
			if (parseResultHolder.hasOptionOf(
					"--external-client-socks5-user-pass")) {
				params.setExternalClientSocks5UsernamePassword(
						parseResultHolder.getOptionArg().getTypeValue(
								UsernamePassword.class));
			}
			if (parseResultHolder.hasOptionOfAnyOf("--help", "-h")) {
				this.printHelp(programBeginningUsage, argsParser.getOptions());
				System.exit(0);
			}
			if (parseResultHolder.hasOptionOfAnyOf(
					"--monitored-config-file", "-m")) {
				boolean modifiedBefore = params.isModified();
				boolean nullBefore = 
						params.getMonitoredConfigurationFile() == null;
				params.setMonitoredConfigurationFile(new File(
						parseResultHolder.getOptionArg().toString()));
				if (params.getMonitoredConfigurationFile().exists()) {
					Configuration configuration = null;
					try {
						configuration = this.readConfiguration(
								params.getMonitoredConfigurationFile().toString());
					} catch (IOException e) {
						System.err.printf("%s: %s%n", programName, e.toString());
						e.printStackTrace();
						System.exit(-1);
					} catch (JAXBException e) {
						System.err.printf("%s: %s%n", programName, e.toString());
						e.printStackTrace();
						System.exit(-1);
					}
					params.add(configuration);
					if (!modifiedBefore && nullBefore) {
						params = new Params(params);
					}
				}
			}
			if (parseResultHolder.hasOptionOfAnyOf("--new-config-file", "-n")) {
				try {
					this.newConfigurationFile(
							params, 
							parseResultHolder.getOptionArg().toString());
				} catch (JAXBException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				} catch (IOException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				}
				System.exit(0);
			}
			if (parseResultHolder.hasOptionOfAnyOf("--settings-help", "-H")) {
				this.printSettingsHelp();
				System.exit(0);
			}
			if (parseResultHolder.hasOptionOfAnyOf("--settings", "-s")) {
				params.addSettings(
						parseResultHolder.getOptionArg().getTypeValue(
								Settings.class).toList());
			}
			if (parseResultHolder.hasOptionOf(
					"--socks5-user-pass-authenticator")) {
				params.setSocks5UsernamePasswordAuthenticator(
						parseResultHolder.getOptionArg().getTypeValue(
								UsernamePasswordAuthenticator.class));
			}
			if (parseResultHolder.hasOptionOf("--socks5-users")) {
				String newProgramBeginningUsage = String.format("%s %s", 
						programBeginningUsage, 
						SocksServerCliOptions.SOCKS5_USERS_OPTION.getUsage());
				System.setProperty(
						SystemPropertyNames.PROGRAM_NAME_PROPERTY_NAME, 
						programName);
				System.setProperty(
						SystemPropertyNames.PROGRAM_BEGINNING_USAGE_PROPERTY_NAME, 
						newProgramBeginningUsage);
				List<String> remainingArgsList = new ArrayList<String>();
				while (argsParser.hasNext()) {
					String arg = argsParser.next();
					remainingArgsList.add(arg);
				}
				Users.main(remainingArgsList.toArray(
						new String[remainingArgsList.size()]));
				System.exit(0);
			}
		}
		Configuration configuration = null;
		if (params.getMonitoredConfigurationFile() == null) {
			configuration = this.newConfiguration(params);
		} else {
			if (params.isModified()) {
				try {
					this.newConfigurationFile(
							params, 
							params.getMonitoredConfigurationFile().toString());
				} catch (JAXBException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				} catch (IOException e) {
					System.err.printf("%s: %s%n", programName, e.toString());
					e.printStackTrace();
					System.exit(-1);
				}
			}
			UpdatedConfigurationService configurationService =
					new UpdatedConfigurationService(
							params.getMonitoredConfigurationFile(),
							LoggerHolder.LOGGER);
			configurationService.start();
			configuration = new ModifiableConfiguration(configurationService);
		}
		return new ProcessResult(configuration);
	}
	
	private Configuration readConfiguration(
			final String arg) throws JAXBException, IOException {
		InputStream in = null;
		if (arg.equals("-")) {
			in = System.in;
		} else {
			File file = new File(arg);
			in = new FileInputStream(file);
		}
		UnmodifiableConfiguration.ConfigurationXml configurationXml = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(			
					UnmodifiableConfiguration.ConfigurationXml.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
			configurationXml = 
					(UnmodifiableConfiguration.ConfigurationXml) unmarshaller.unmarshal(in);
		} finally {
			if (in instanceof FileInputStream) {
				in.close();
			}
		}
		return UnmodifiableConfiguration.newInstance(configurationXml);
	}
		
	private UsernamePassword readExternalClientSocks5UsernamePassword() {
		String prompt = "Please enter username and password for the external "
				+ "SOCKS5 server for external connections";
		UsernamePasswordRequestor usernamePasswordRequestor = 
				new DefaultUsernamePasswordRequestor();
		return usernamePasswordRequestor.requestUsernamePassword(null, prompt);
	}

}