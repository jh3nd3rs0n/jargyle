package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.argmatey.ArgMatey;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Option;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Ordinal;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionType;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;

public final class Socks5UserManagerCLI extends CLI {
	
	private static enum Command {
		
		@HelpText(
				doc = "Add user(s) through an interactive prompt", 
				usage = "add"
		)
		ADD("add") {

			@Override
			public void invoke(
					final UserRepository userRepository, 
					final String[] args) {
				if (args.length > 0) {
					throw new IllegalArgumentException(String.format(
							"extra operand(s) `%s'", 
							Arrays.asList(args).stream().collect(
									Collectors.joining(", "))));
				}
				Users users = Users.newInstance();
				ConsoleWrapper consoleWrapper = new ConsoleWrapper(
						System.console());
				boolean addAnotherUser = false;
				do {
					consoleWrapper.printf("User%n");
					String name;
					while (true) {
						name = consoleWrapper.readLine("Name: ");
						try {
							User.validateName(name);
							break;
						} catch (IllegalArgumentException e) {
							consoleWrapper.printf(
									"Name must be no more than %s byte(s).%n", 
									User.MAX_NAME_LENGTH);
						}
					}
					char[] password;
					while (true) {
						password = consoleWrapper.readPassword("Password: ");
						try {
							User.validatePassword(password);
						} catch (IllegalArgumentException e) {
							consoleWrapper.printf(
									"Password must be no more than %s byte(s).%n",
									User.MAX_PASSWORD_LENGTH);
							continue;
						}
						char[] retypedPassword = consoleWrapper.readPassword(
								"Re-type password: ");
						if (Arrays.equals(password, retypedPassword)) {
							break;
						} else {
							consoleWrapper.printf(
									"Password and re-typed password do not match.%n");
						}
					}
					users.put(User.newInstance(name, password));
					Arrays.fill(password, '\0');
					consoleWrapper.printf("User '%s' added.%n", name);
					String decision = consoleWrapper.readLine(
							"Would you like to enter another user? ('Y' for yes): ");
					addAnotherUser = decision.equals("Y");
				} while (addAnotherUser);
				userRepository.putAll(users);
			}

		},
		@HelpText(
				doc = "List users to standard output", 
				usage = "list"
		)		
		LIST("list") {

			@Override
			public void invoke(
					final UserRepository userRepository, 
					final String[] args) {
				if (args.length > 0) {
					throw new IllegalArgumentException(String.format(
							"extra operand(s) `%s'", 
							Arrays.asList(args).stream().collect(
									Collectors.joining(", "))));
				}
				Users users = userRepository.getAll();
				for (User user : users.toMap().values()) {
					System.out.println(user.getName());
				}
			}
		},
		@HelpText(
				doc = "Remove user by name", 
				usage = "remove NAME"
		)		
		REMOVE("remove") {

			@Override
			public void invoke(
					final UserRepository userRepository, 
					final String[] args) {
				if (args.length < 1) {
					throw new IllegalArgumentException(
							"NAME is required");
				}
				String name = args[0];
				User user = userRepository.get(name);
				if (user == null) {
					throw new IllegalArgumentException(String.format(
							"User '%s' does not exist", name));
				}
				userRepository.remove(name);
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
				final UserRepository userRepository, 
				final String[] args);
		
		public String toString() {
			return this.value;
		}
		
	}

	private static final int HELP_OPTION_GROUP_ORDINAL = 0;
	
	public static void main(final String[] args) {
		CLI cli = new Socks5UserManagerCLI(null, null, args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			System.exit(e.getExitStatusCode());
		}
	}
	
	private final String programBeginningUsage;
	private final String suggestion;
	private UserRepository userRepository;
	
	public Socks5UserManagerCLI(
			final String progName, 
			final String progBeginningUsage, 
			final String[] args, 
			final boolean posixlyCorrect) {
		super(args, posixlyCorrect);
		String prgName = progName;
		if (prgName == null) {
			prgName = this.getClass().getName();
		}
		String prgBeginningUsage = progBeginningUsage;
		if (prgBeginningUsage == null) {
			prgBeginningUsage = prgName;
		}
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		String suggest = String.format(
				"Try `%s %s' for more information", 
				prgBeginningUsage, 
				helpOption.getUsage());
		this.programBeginningUsage = prgBeginningUsage;
		this.suggestion = suggest;
		this.userRepository = null;
		this.setProgramName(prgName);		
	}
	
	@Override
	protected void afterHandleArgs() throws TerminationRequestedException {
		if (this.userRepository == null) {
			System.err.printf(
					"%s: user repository must be provided%n", this.getProgramName());
			System.err.println(this.suggestion);
			throw new TerminationRequestedException(-1);
		}
		throw new TerminationRequestedException(0);		
	}
	
	@Override
	protected void beforeHandleArgs() {
		this.userRepository = null;
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
		System.out.printf("Usage: %s "
				+ "USER_REPOSITORY_TYPE_NAME:INITIALIZATION_STRING COMMAND%n", 
				this.programBeginningUsage);
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				helpOption.getUsage());
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
	
	@Override
	protected void displayProgramVersion() { 
		throw new UnsupportedOperationException("not implemented");
	}
	
	@Override
	protected void handleNonparsedArg(
			final String nonparsedArg) throws TerminationRequestedException {
		this.userRepository = UserRepository.newInstance(nonparsedArg);
		if (!this.hasNext()) {
			System.err.printf(
					"%s: command must be provided%n", 
					this.getProgramName());
			System.err.println(this.suggestion);
			throw new TerminationRequestedException(-1);
		}
		Command command = Command.valueOfString(this.next());
		List<String> remainingArgList = new ArrayList<String>();
		while (this.hasNext()) {
			remainingArgList.add(this.next());
		}
		try {
			command.invoke(this.userRepository, remainingArgList.toArray(
					new String[remainingArgList.size()]));
		} catch (UncheckedIOException e) {
			System.err.printf("%s: %s%n", this.getProgramName(), e);
			e.printStackTrace(System.err);
			throw new TerminationRequestedException(-1);
		}
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
