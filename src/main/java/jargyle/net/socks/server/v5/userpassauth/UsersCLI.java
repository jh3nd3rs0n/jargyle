package jargyle.net.socks.server.v5.userpassauth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import argmatey.ArgMatey;
import argmatey.ArgMatey.Annotations.Option;
import argmatey.ArgMatey.Annotations.Ordinal;
import argmatey.ArgMatey.CLI;
import argmatey.ArgMatey.OptionType;
import jargyle.internal.help.HelpText;
import jargyle.internal.io.ConsoleWrapper;
import jargyle.net.socks.server.SystemPropertyNameConstants;

public final class UsersCLI extends CLI {
	
	private enum Command {
		
		@HelpText(
				doc = "Add users to an existing file through an interactive "
						+ "prompt", 
				usage = "add-users-to-file FILE"
		)
		ADD_USERS_TO_FILE("add-users-to-file") {

			@Override
			public void invoke(final String[] args) throws Exception {
				if (args.length == 0) {
					throw new IllegalArgumentException("FILE is required");
				}
				String file = args[0];
				Users users = readUsersFromFile(file);
				Users addedUsers = readUsersFromPrompt();
				users.putAll(addedUsers);
				writeUsersToFile(users, file);
			}

		},
		@HelpText(
				doc = "Create a new file of zero or more users through an "
						+ "interactive prompt", 
				usage = "create-new-file FILE"
		)
		CREATE_NEW_FILE("create-new-file") {

			@Override
			public void invoke(final String[] args) throws Exception {
				if (args.length == 0) {
					throw new IllegalArgumentException("FILE is required");
				}
				String file = args[0];
				Users addedUsers = Users.newInstance();
				ConsoleWrapper consoleWrapper = new ConsoleWrapper(
						System.console());
				String decision = consoleWrapper.readLine(
						"Would you like to enter a user? ('Y' for yes): ");
				if (decision.equals("Y")) {
					addedUsers.putAll(readUsersFromPrompt());
				}
				writeUsersToFile(addedUsers, file);
			}
			
		},
		@HelpText(
				doc = "Remove user by name from an existing file", 
				usage = "remove-user NAME FILE"
		)		
		REMOVE_USER("remove-user") {

			@Override
			public void invoke(final String[] args) throws Exception {
				if (args.length < 2) {
					throw new IllegalArgumentException(
							"NAME and FILE are required");
				}
				String name = args[0];
				String file = args[1];
				Users users = readUsersFromFile(file);
				User removedUser = users.remove(name);
				if (removedUser == null) {
					throw new IllegalArgumentException(String.format(
							"User '%s' does not exist", name));
				}
				System.out.printf("User '%s' removed%n", name);
				writeUsersToFile(users, file);
			}
			
		};
		
		public static Command getInstance(final String s) {
			for (Command command : Command.values()) {
				if (command.toString().equals(s)) {
					return command;
				}
			}
			throw new IllegalArgumentException(String.format(
					"no command found for %s", s));
		}
		
		private static Users readUsersFromFile(
				final String file) throws IOException {
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
			Users users = null;
			try {
				users = Users.newInstanceFromXml(in);
			} catch (IOException e) { 
				throw new IllegalArgumentException(String.format(
						"error in reading XML file '%s'", file), 
						e);
			} finally {
				if (in instanceof FileInputStream) {
					in.close();
				}
			}
			return users;
		}

		private static Users readUsersFromPrompt() {
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
			return users;
		}
		
		private static void writeUsersToFile(
				final Users users,
				final String file) throws IOException {
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
				users.toXml(out);
				out.flush();
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
		}
		
		private final String value;
		
		private Command(final String val) {
			this.value = val;
		}
		
		public abstract void invoke(final String[] args) throws Exception;
		
		public String toString() {
			return this.value;
		}
		
	}
	
	private static final int HELP_OPTION_GROUP_ORDINAL = 0;
	private static final int XSD_OPTION_GROUP_ORDINAL = 1;
	
	public static void main(final String[] args) {
		UsersCLI usersCLI = new UsersCLI(null, null, args, false);
		Optional<Integer> status = usersCLI.handleArgs();
		if (status.isPresent() && status.get().intValue() != 0) { 
			System.exit(status.get().intValue());
		}
	}
	
	private List<String> argList;
	private Command command;
	private final String programBeginningUsage;
	private final String suggestion;
	
	private boolean xsdRequested;
	
	public UsersCLI(
			final String progName, 
			final String progBeginningUsage, 
			final String[] args, 
			final boolean posixlyCorrect) {
		super(args, posixlyCorrect);
		String prgName = progName;
		if (prgName == null) {
			prgName = System.getProperty(
					SystemPropertyNameConstants.PROGRAM_NAME);
		}
		if (prgName == null) {
			prgName = Users.class.getName();
		}
		String prgBeginningUsage = progBeginningUsage;
		if (prgBeginningUsage == null) {
			prgBeginningUsage = System.getProperty(
					SystemPropertyNameConstants.PROGRAM_BEGINNING_USAGE);
		}
		if (prgBeginningUsage == null) {
			prgBeginningUsage = progName;
		}
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		String suggest = String.format(
				"Try `%s %s' for more information", 
				prgBeginningUsage, 
				helpOption.getUsage());		
		this.programBeginningUsage = prgBeginningUsage;
		this.programName = prgName;
		this.suggestion = suggest;
	}
	
	@Override
	protected Optional<Integer> afterHandleArgs() {
		if (this.command == null) {
			System.err.printf("%s: command must be provided%n", this.programName);
			System.err.println(this.suggestion);
			return Optional.of(Integer.valueOf(-1));
		}
		try {
			this.command.invoke(this.argList.toArray(
					new String[this.argList.size()]));
		} catch (Exception e) {
			System.err.printf("%s: %s%n", this.programName, e);
			System.err.println(this.suggestion);
			e.printStackTrace(System.err);
			return Optional.of(Integer.valueOf(-1));
		}
		return Optional.of(Integer.valueOf(0));
	}
	
	@Override
	protected Optional<Integer> afterHandleNext() {
		if (this.xsdRequested) {
			return Optional.of(Integer.valueOf(0));
		}
		return Optional.empty();
	}
	
	@Override
	protected Optional<Integer> beforeHandleArgs() {
		this.argList = new ArrayList<String>();
		this.command = null;
		this.xsdRequested = false;		
		return Optional.empty();
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
	protected void displayProgramHelp() {
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		ArgMatey.Option xsdOption = this.getOptionGroups().get(
				XSD_OPTION_GROUP_ORDINAL).get(0);
		System.out.printf("Usage: %s COMMAND%n", this.programBeginningUsage);
		System.out.printf("       %s %s%n", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		System.out.printf("       %s %s", 
				this.programBeginningUsage, 
				xsdOption.getUsage());
		System.out.println();
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
	}
	
	@Override
	protected void displayProgramVersion() { 
		throw new UnsupportedOperationException("not implemented");
	}
	
	@Override
	protected void handleNonparsedArg(final String nonparsedArg) {
		if (this.command == null) {
			this.command = Command.getInstance(nonparsedArg);
		} else {
			this.argList.add(nonparsedArg);
		}
	}
	
	@Override
	protected Optional<Integer> handleThrowable(final Throwable t) {
		System.err.printf("%s: %s%n", this.programName, t);
		System.err.println(this.suggestion);
		t.printStackTrace(System.err);
		return Optional.of(Integer.valueOf(-1));		
	}

	@Option(
			doc = "Print the XSD and exit",
			name = "xsd",
			type = OptionType.GNU_LONG
	)
	@Option(
			name = "x",
			type = OptionType.POSIX
	)
	@Ordinal(XSD_OPTION_GROUP_ORDINAL)
	private void printXsd() throws IOException {
		Users.generateXsd(System.out);
		System.out.flush();
		this.xsdRequested = true;
	}
	
}
