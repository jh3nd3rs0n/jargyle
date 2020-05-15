package jargyle.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
		
		public UsernamePassword externalClientSocks5UsernamePassword;
		public final List<Setting> settings;
		public UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
		
		public Params() {
			this.externalClientSocks5UsernamePassword = null;
			this.settings = new ArrayList<Setting>();
			this.socks5UsernamePasswordAuthenticator = null;
		}
		
		public void add(final Configuration configuration) {
			UsernamePassword externalClientSocks5UsrnmPsswrd = 
					configuration.getExternalClientSocks5UsernamePassword();
			if (externalClientSocks5UsrnmPsswrd != null) {
				this.externalClientSocks5UsernamePassword = 
						configuration.getExternalClientSocks5UsernamePassword();
			}
			this.settings.addAll(configuration.getSettings().toList());
			UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator =
					configuration.getSocks5UsernamePasswordAuthenticator();
			if (socks5UsrnmPsswrdAuthenticator != null) {
				this.socks5UsernamePasswordAuthenticator =
						socks5UsrnmPsswrdAuthenticator;
			}
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
	
	private Configuration newConfiguration(final SocksServerCli.Params params) {
		Configuration.Builder builder = new Configuration.Builder();
		if (params.externalClientSocks5UsernamePassword != null) {
			builder.externalClientSocks5UsernamePassword(
					params.externalClientSocks5UsernamePassword);
		}
		if (!params.settings.isEmpty()) {
			builder.settings(Settings.newInstance(params.settings));
		}
		if (params.socks5UsernamePasswordAuthenticator != null) {
			builder.socks5UsernamePasswordAuthenticator(
					params.socks5UsernamePasswordAuthenticator);
		}
		return builder.build();
	}
	
	private void newConfigurationFile(
			final SocksServerCli.Params params,
			final String arg) throws JAXBException, IOException {
		Configuration configuration = this.newConfiguration(params);
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
					Configuration.ConfigurationXml.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(configuration.toConfigurationXml(), out);
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
				Configuration.ConfigurationXml.class);
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
				params.externalClientSocks5UsernamePassword = 
						this.readExternalClientSocks5UsernamePassword();
			}
			if (parseResultHolder.hasOptionOf(
					"--external-client-socks5-user-pass")) {
				params.externalClientSocks5UsernamePassword =
						parseResultHolder.getOptionArg().getTypeValue(
								UsernamePassword.class);
			}
			if (parseResultHolder.hasOptionOfAnyOf("--help", "-h")) {
				this.printHelp(programBeginningUsage, argsParser.getOptions());
				System.exit(0);
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
				params.settings.addAll(
						parseResultHolder.getOptionArg().getTypeValue(
								Settings.class).toList());
			}
			if (parseResultHolder.hasOptionOf(
					"--socks5-user-pass-authenticator")) {
				params.socks5UsernamePasswordAuthenticator =
						parseResultHolder.getOptionArg().getTypeValue(
								UsernamePasswordAuthenticator.class);
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
		Configuration configuration = this.newConfiguration(params);
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
		Configuration.ConfigurationXml configurationXml = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(			
					Configuration.ConfigurationXml.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
			configurationXml = 
					(Configuration.ConfigurationXml) unmarshaller.unmarshal(in);
		} finally {
			if (in instanceof FileInputStream) {
				in.close();
			}
		}
		return Configuration.newInstance(configurationXml);
	}
		
	private UsernamePassword readExternalClientSocks5UsernamePassword() {
		String prompt = "Please enter username and password for the external "
				+ "SOCKS5 server for external connections";
		UsernamePasswordRequestor usernamePasswordRequestor = 
				new DefaultUsernamePasswordRequestor();
		return usernamePasswordRequestor.requestUsernamePassword(null, prompt);
	}

}