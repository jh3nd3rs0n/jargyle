package com.github.jh3nd3rs0n.jargyle.cli;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.argmatey.ArgMatey;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Option;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Ordinal;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionType;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationFileCreatorCLI;
import com.github.jh3nd3rs0n.jargyle.server.SocksServerCLI;
import com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind.ConfigurationXsd;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserManagerCLI;

public final class JargyleCLI extends CLI {

	private static enum Command {
		
		@HelpText(
				doc = "Manage SOCKS5 users", 
				usage = "manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_VALUE COMMAND"
		)
		MANAGE_SOCKS5_USERS("manage-socks5-users") {
			
			@Override
			public void invoke(
					final String progName, 
					final String progBeginningUsage, 
					final String[] args, 
					final boolean posixCorrect) 
					throws TerminationRequestedException {
				CLI cli = new UserManagerCLI(
						progName,
						progBeginningUsage,
						args,
						posixCorrect);
				cli.handleArgs();
			}
			
		},
		
		@HelpText(
				doc = "Create a new server configuration file based on the "
						+ "provided options", 
				usage = "new-server-config-file [OPTIONS] FILE"
		)
		NEW_SERVER_CONFIG_FILE("new-server-config-file") {
			
			@Override
			public void invoke(
					final String progName, 
					final String progBeginningUsage, 
					final String[] args, 
					final boolean posixCorrect)	
					throws TerminationRequestedException {
				CLI cli = new ConfigurationFileCreatorCLI(
						progName,
						progBeginningUsage,
						args,
						posixCorrect);
				cli.handleArgs();
			}
		},
		
		SERVER_CONFIG_FILE_SCHEMA("server-config-file-schema") {
			
			@Override
			public void invoke(
					final String progName, 
					final String progBeginningUsage, 
					final String[] args, 
					final boolean posixCorrect)
					throws TerminationRequestedException {
				ConfigurationXsd.main(new String[] { });
			}
			
		},
		
		@HelpText(
				doc = "Start the SOCKS server", 
				usage = "start-server [OPTIONS] [MONITORED_CONFIG_FILE]"
		)
		START_SERVER("start-server") {
			
			@Override
			public void invoke(
					final String progName, 
					final String progBeginningUsage, 
					final String[] args, 
					final boolean posixCorrect) 
					throws TerminationRequestedException {
				CLI cli = new SocksServerCLI(
						progName,
						progBeginningUsage,
						args,
						posixCorrect);
				cli.handleArgs();
			}
			
		};
		
		public static Command valueOfString(final String s) {
			for (Command command : Command.values()) {
				if (command.toString().equals(s)) {
					return command;
				}
			}
			throw new IllegalArgumentException(String.format(
					"no command found for %s", s));
		}
		
		private final String value;
		
		private Command(final String val) {
			this.value = val;
		}
		
		public abstract void invoke(
				final String progName, 
				final String progBeginningUsage, 
				final String[] args, 
				final boolean posixCorrect)
				throws TerminationRequestedException;
		
		public String toString() {
			return this.value;
		}
		
	}
	
	private static final int HELP_OPTION_GROUP_ORDINAL = 0;
	private static final int VERSION_OPTION_GROUP_ORDINAL = 1;
	
	public static void main(final String[] args) {
		CLI cli = new JargyleCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			System.exit(e.getExitStatusCode());
		}
	}
	
	private Command command;
	private final boolean posixlyCorrect;
	private final String programBeginningUsage;
	private final String suggestion;
	
	public JargyleCLI(
			final String progName, 
			final String progBeginningUsage,
			final String[] args, 
			final boolean posixCorrect) {
		super(args, posixCorrect);
		String prgName = progName;
		if (prgName == null) {
			prgName = System.getProperty("program.name");
		}
		if (prgName == null) {
			prgName = this.getClass().getName();
		}
		String prgBeginningUsage = progBeginningUsage;
		if (prgBeginningUsage == null) {
			prgBeginningUsage = System.getProperty("program.beginningUsage");
		}
		if (prgBeginningUsage == null) {
			prgBeginningUsage = prgName;
		}
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		String suggest = String.format(
				"Try `%s %s' for more information", 
				prgBeginningUsage, 
				helpOption.getUsage());
		this.posixlyCorrect = posixCorrect;
		this.programBeginningUsage = prgBeginningUsage;
		this.suggestion = suggest;
		this.setProgramName(prgName);
	}
	
	@Override
	protected void afterHandleArgs() throws TerminationRequestedException {
		if (this.command == null) {
			System.err.printf(
					"%s: command must be provided%n", this.getProgramName());
			System.err.println(this.suggestion);
			throw new TerminationRequestedException(-1);
		}
	}
	
	@Override
	protected void beforeHandleArgs() {
		this.command = null;
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
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option versionOption = this.getOptionGroups().get(
				VERSION_OPTION_GROUP_ORDINAL).get(0);
		System.out.printf("Usage: %s COMMAND%n", this.programBeginningUsage);
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				versionOption.getUsage());
		System.out.println();
		System.out.println("COMMANDS:");
		Field[] fields = Command.class.getDeclaredFields();
		for (Field field : fields) {
			HelpText helpText = field.getAnnotation(HelpText.class);
			if (helpText != null) {
				System.out.print("  ");
				System.out.println(helpText.usage());
				System.out.print("      ");
				System.out.println(helpText.doc());
			}
		}		
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
			name = "V",
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
	
	@Override
	protected void handleNonparsedArg(
			final String nonparsedArg) throws TerminationRequestedException {
		this.command = Command.valueOfString(nonparsedArg);
		String newProgramBeginningUsage = String.format("%s %s", 
				this.programBeginningUsage, 
				this.command);
		List<String> remainingArgList = new ArrayList<String>();
		while (this.hasNext()) {
			remainingArgList.add(this.next());
		}
		this.command.invoke(
				this.getProgramName(),
				newProgramBeginningUsage,
				remainingArgList.toArray(new String[remainingArgList.size()]),
				this.posixlyCorrect);
	}
	
	@Override
	protected void handleThrowable(final Throwable t) 
			throws TerminationRequestedException {
		System.err.printf("%s: %s%n", this.getProgramName(), t);
		System.err.println(this.suggestion);
		t.printStackTrace(System.err);
		throw new TerminationRequestedException(-1);
	}
	
}
