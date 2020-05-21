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
import jargyle.server.socks5.Socks5RequestCriterion;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.UsernamePasswordAuthenticator;
import jargyle.server.socks5.Users;

final class SocksServerCli {
	
	private static final class Params {
		
		private final List<Criterion> allowedClientAddressCriteria;
		private final List<Criterion> allowedIncomingTcpAddressCriteria;
		private final List<Criterion> allowedIncomingUdpAddressCriteria;
		private final List<Socks5RequestCriterion> allowedSocks5RequestCriteria;
		private final List<Criterion> blockedClientAddressCriteria;
		private final List<Criterion> blockedIncomingTcpAddressCriteria;
		private final List<Criterion> blockedIncomingUdpAddressCriteria;		
		private final List<Socks5RequestCriterion> blockedSocks5RequestCriteria;
		private UsernamePassword externalClientSocks5UsernamePassword;
		private final List<Setting> settings;
		private UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
		
		public Params() {
			this.allowedClientAddressCriteria = new ArrayList<Criterion>();
			this.allowedIncomingTcpAddressCriteria = new ArrayList<Criterion>();
			this.allowedIncomingUdpAddressCriteria = new ArrayList<Criterion>();
			this.allowedSocks5RequestCriteria = 
					new ArrayList<Socks5RequestCriterion>();
			this.blockedClientAddressCriteria = new ArrayList<Criterion>();
			this.blockedIncomingTcpAddressCriteria = new ArrayList<Criterion>();
			this.blockedIncomingUdpAddressCriteria = new ArrayList<Criterion>();
			this.blockedSocks5RequestCriteria = 
					new ArrayList<Socks5RequestCriterion>();
			this.externalClientSocks5UsernamePassword = null;
			this.settings = new ArrayList<Setting>();
			this.socks5UsernamePasswordAuthenticator = null;
		}
		
		public void add(final Configuration configuration) {
			this.addAllowedClientAddressCriteria(
					configuration.getAllowedClientAddressCriteria().toList());
			this.addAllowedIncomingTcpAddressCriteria(
					configuration.getAllowedIncomingTcpAddressCriteria().toList());
			this.addAllowedIncomingUdpAddressCriteria(
					configuration.getAllowedIncomingUdpAddressCriteria().toList());
			this.addAllowedSocks5RequestCriteria(
					configuration.getAllowedSocks5RequestCriteria().toList());
			this.addBlockedClientAddressCriteria(
					configuration.getBlockedClientAddressCriteria().toList());
			this.addBlockedIncomingTcpAddressCriteria(
					configuration.getBlockedIncomingTcpAddressCriteria().toList());
			this.addBlockedIncomingUdpAddressCriteria(
					configuration.getBlockedIncomingUdpAddressCriteria().toList());			
			this.addBlockedSocks5RequestCriteria(
					configuration.getBlockedSocks5RequestCriteria().toList());
			this.setExternalClientSocks5UsernamePassword(
					configuration.getExternalClientSocks5UsernamePassword());
			this.addSettings(configuration.getSettings().toList());
			this.setSocks5UsernamePasswordAuthenticator(
					configuration.getSocks5UsernamePasswordAuthenticator());
		}

		public void addAllowedClientAddressCriteria(
				final List<Criterion> allowedClientAddrCriteria) {
			if (allowedClientAddrCriteria.isEmpty()) {
				return;
			}
			this.allowedClientAddressCriteria.addAll(allowedClientAddrCriteria);
		}
		
		public void addAllowedIncomingTcpAddressCriteria(
				final List<Criterion> allowedIncomingTcpAddrCriteria) {
			if (allowedIncomingTcpAddrCriteria.isEmpty()) {
				return;
			}
			this.allowedIncomingTcpAddressCriteria.addAll(
					allowedIncomingTcpAddrCriteria);
		}
		
		public void addAllowedIncomingUdpAddressCriteria(
				final List<Criterion> allowedIncomingUdpAddrCriteria) {
			if (allowedIncomingUdpAddrCriteria.isEmpty()) {
				return;
			}
			this.allowedIncomingUdpAddressCriteria.addAll(
					allowedIncomingUdpAddrCriteria);
		}
		
		public void addAllowedSocks5RequestCriteria(
				final List<Socks5RequestCriterion> allowedSocks5ReqCriteria) {
			if (allowedSocks5ReqCriteria.isEmpty()) {
				return;
			}
			this.allowedSocks5RequestCriteria.addAll(allowedSocks5ReqCriteria);
		}
		
		public void addBlockedClientAddressCriteria(
				final List<Criterion> blockedClientAddrCriteria) {
			if (blockedClientAddrCriteria.isEmpty()) {
				return;
			}
			this.blockedClientAddressCriteria.addAll(blockedClientAddrCriteria);
		}
		
		public void addBlockedIncomingTcpAddressCriteria(
				final List<Criterion> blockedIncomingTcpAddrCriteria) {
			if (blockedIncomingTcpAddrCriteria.isEmpty()) {
				return;
			}
			this.blockedIncomingTcpAddressCriteria.addAll(
					blockedIncomingTcpAddrCriteria);
		}
		
		public void addBlockedIncomingUdpAddressCriteria(
				final List<Criterion> blockedIncomingUdpAddrCriteria) {
			if (blockedIncomingUdpAddrCriteria.isEmpty()) {
				return;
			}
			this.blockedIncomingUdpAddressCriteria.addAll(
					blockedIncomingUdpAddrCriteria);
		}
		
		public void addBlockedSocks5RequestCriteria(
				final List<Socks5RequestCriterion> blockedSocks5ReqCriteria) {
			if (blockedSocks5ReqCriteria.isEmpty()) {
				return;
			}
			this.blockedSocks5RequestCriteria.addAll(blockedSocks5ReqCriteria);
		}
		
		public void addSettings(final List<Setting> sttngs) {
			if (sttngs.isEmpty()) {
				return;
			}
			this.settings.addAll(sttngs);
		}

		public List<Criterion> getAllowedClientAddressCriteria() {
			return Collections.unmodifiableList(this.allowedClientAddressCriteria);
		}
		
		public List<Criterion> getAllowedIncomingTcpAddressCriteria() {
			return Collections.unmodifiableList(this.allowedIncomingTcpAddressCriteria);
		}
		
		public List<Criterion> getAllowedIncomingUdpAddressCriteria() {
			return Collections.unmodifiableList(this.allowedIncomingUdpAddressCriteria);
		}
		
		public List<Socks5RequestCriterion> getAllowedSocks5RequestCriteria() {
			return Collections.unmodifiableList(this.allowedSocks5RequestCriteria);
		}

		public List<Criterion> getBlockedClientAddressCriteria() {
			return Collections.unmodifiableList(this.blockedClientAddressCriteria);
		}
		
		public List<Criterion> getBlockedIncomingTcpAddressCriteria() {
			return Collections.unmodifiableList(this.blockedIncomingTcpAddressCriteria);
		}
		
		public List<Criterion> getBlockedIncomingUdpAddressCriteria() {
			return Collections.unmodifiableList(this.blockedIncomingUdpAddressCriteria);
		}
		
		public List<Socks5RequestCriterion> getBlockedSocks5RequestCriteria() {
			return Collections.unmodifiableList(this.blockedSocks5RequestCriteria);
		}
		
		public UsernamePassword getExternalClientSocks5UsernamePassword() {
			return this.externalClientSocks5UsernamePassword;
		}
		
		public List<Setting> getSettings() {
			return Collections.unmodifiableList(this.settings);
		}

		public UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator() {
			return this.socks5UsernamePasswordAuthenticator;
		}
		
		public void setExternalClientSocks5UsernamePassword(
				final UsernamePassword externalClientSocks5UsrnmPsswrd) {
			if (externalClientSocks5UsrnmPsswrd == null) {
				return;
			}
			this.externalClientSocks5UsernamePassword = 
					externalClientSocks5UsrnmPsswrd;
		}

		public void setSocks5UsernamePasswordAuthenticator(
				final UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator) {
			if (socks5UsrnmPsswrdAuthenticator == null) {
				return;
			}
			this.socks5UsernamePasswordAuthenticator = socks5UsrnmPsswrdAuthenticator;
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
	
	private ImmutableConfiguration newConfiguration(
			final SocksServerCli.Params params) {
		ImmutableConfiguration.Builder builder = 
				new ImmutableConfiguration.Builder();
		if (!params.getAllowedClientAddressCriteria().isEmpty()) {
			builder.allowedClientAddressCriteria(Criteria.newInstance(
					params.getAllowedClientAddressCriteria()));
		}
		if (!params.getAllowedIncomingTcpAddressCriteria().isEmpty()) {
			builder.allowedIncomingTcpAddressCriteria(Criteria.newInstance(
					params.getAllowedIncomingTcpAddressCriteria()));
		}
		if (!params.getAllowedIncomingUdpAddressCriteria().isEmpty()) {
			builder.allowedIncomingUdpAddressCriteria(Criteria.newInstance(
					params.getAllowedIncomingUdpAddressCriteria()));
		}
		if (!params.getAllowedSocks5RequestCriteria().isEmpty()) {
			builder.allowedSocks5RequestCriteria(new Socks5RequestCriteria(
					params.getAllowedSocks5RequestCriteria()));
		}
		if (!params.getBlockedClientAddressCriteria().isEmpty()) {
			builder.blockedClientAddressCriteria(Criteria.newInstance(
					params.getBlockedClientAddressCriteria()));
		}
		if (!params.getBlockedIncomingTcpAddressCriteria().isEmpty()) {
			builder.blockedIncomingTcpAddressCriteria(Criteria.newInstance(
					params.getBlockedIncomingTcpAddressCriteria()));
		}
		if (!params.getBlockedIncomingUdpAddressCriteria().isEmpty()) {
			builder.blockedIncomingUdpAddressCriteria(Criteria.newInstance(
					params.getBlockedIncomingUdpAddressCriteria()));
		}		
		if (!params.getBlockedSocks5RequestCriteria().isEmpty()) {
			builder.allowedSocks5RequestCriteria(new Socks5RequestCriteria(
					params.getBlockedSocks5RequestCriteria()));
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
		ImmutableConfiguration immutableConfiguration = 
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
					ImmutableConfiguration.ConfigurationXml.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(immutableConfiguration.toConfigurationXml(), out);
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
				ImmutableConfiguration.ConfigurationXml.class);
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
		String configurationFileArg = null;
		boolean updateConfiguration = false;
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
			if (parseResultHolder.hasOptionOf(
					"--allowed-client-address-criteria")) {
				params.addAllowedClientAddressCriteria(
						parseResultHolder.getOptionArg().getTypeValue(
								Criteria.class).toList());
			}
			if (parseResultHolder.hasOptionOf(
					"--allowed-incoming-tcp-address-criteria")) {
				params.addAllowedIncomingTcpAddressCriteria(
						parseResultHolder.getOptionArg().getTypeValue(
								Criteria.class).toList());
			}
			if (parseResultHolder.hasOptionOf(
					"--allowed-incoming-udp-address-criteria")) {
				params.addAllowedIncomingUdpAddressCriteria(
						parseResultHolder.getOptionArg().getTypeValue(
								Criteria.class).toList());
			}
			if (parseResultHolder.hasOptionOf(
					"--blocked-client-address-criteria")) {
				params.addBlockedClientAddressCriteria(
						parseResultHolder.getOptionArg().getTypeValue(
								Criteria.class).toList());
			}
			if (parseResultHolder.hasOptionOf(
					"--blocked-incoming-tcp-address-criteria")) {
				params.addBlockedIncomingTcpAddressCriteria(
						parseResultHolder.getOptionArg().getTypeValue(
								Criteria.class).toList());
			}
			if (parseResultHolder.hasOptionOf(
					"--blocked-incoming-udp-address-criteria")) {
				params.addBlockedIncomingUdpAddressCriteria(
						parseResultHolder.getOptionArg().getTypeValue(
								Criteria.class).toList());
			}
			if (parseResultHolder.hasOptionOfAnyOf("--config-file", "-f")) {
				configurationFileArg = 
						parseResultHolder.getOptionArg().toString();
				Configuration configuration = null;
				try {
					configuration = this.readConfiguration(
							configurationFileArg);
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
			if (parseResultHolder.hasOptionOfAnyOf("--new-config-file", "-n")) {
				String newConfigurationFileArg = 
						parseResultHolder.getOptionArg().toString();
				try {
					this.newConfigurationFile(params, newConfigurationFileArg);
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
			if (parseResultHolder.hasOptionOfAnyOf("--update-config", "-u")) {
				updateConfiguration = true;
			}
		}
		if (configurationFileArg != null && updateConfiguration) {
			ConfigurationService configurationService = null;
			try {
				configurationService = new XmlFileSourceConfigurationService(
						new File(configurationFileArg),
						LoggerHolder.LOGGER);
			} catch (IllegalArgumentException e) {
				System.err.printf("%s: %s%n", programName, e.toString());
				e.printStackTrace();
				System.exit(-1);
			}
			return new ProcessResult(new MutableConfiguration(
					configurationService));
		}
		return new ProcessResult(this.newConfiguration(params));
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
		ImmutableConfiguration.ConfigurationXml configurationXml = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(			
					ImmutableConfiguration.ConfigurationXml.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
			configurationXml = 
					(ImmutableConfiguration.ConfigurationXml) unmarshaller.unmarshal(in);
		} finally {
			if (in instanceof FileInputStream) {
				in.close();
			}
		}
		return ImmutableConfiguration.newInstance(configurationXml);
	}
		
	private UsernamePassword readExternalClientSocks5UsernamePassword() {
		String prompt = "Please enter username and password for the external "
				+ "SOCKS5 server for external connections";
		UsernamePasswordRequestor usernamePasswordRequestor = 
				new DefaultUsernamePasswordRequestor();
		return usernamePasswordRequestor.requestUsernamePassword(null, prompt);
	}

}