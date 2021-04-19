package jargyle.net.socks.server.v5.userpassauth;

import java.io.Console;
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
import jargyle.help.HelpText;
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
				String arg = args[0];
				Users users = readFile(arg);
				List<User> userList = new ArrayList<User>(users.toList());
				List<User> addedUserList = readUsers();
				userList.addAll(addedUserList);
				Users combinedUsers = Users.newInstance(userList);  
				newFile(combinedUsers, arg);
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
				String arg = args[0];
				List<User> addedUserList = new ArrayList<User>();
				Console console = System.console();
				String decision = console.readLine(
						"Would you like to enter a user? ('Y' for yes): ");
				if (decision.equals("Y")) {
					addedUserList.addAll(readUsers());
				}
				Users addedUsers = Users.newInstance(addedUserList);  
				newFile(addedUsers, arg);
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
				String username = args[0];
				String filename = args[1];
				Users users = readFile(filename);
				List<User> userList = new ArrayList<User>(users.toList());
				List<User> modifiedUserList = new ArrayList<User>();
				for (User user : userList) {
					if (user.getName().equals(username)) {
						continue;
					}
					modifiedUserList.add(user);
				}
				int numOfUsersRemoved = 
						userList.size() - modifiedUserList.size();
				if (numOfUsersRemoved == 0) {
					throw new IllegalArgumentException(String.format(
							"User '%s' does not exist", username));
				}
				System.out.printf("User '%s' removed%n", username);
				newFile(Users.newInstance(modifiedUserList), filename);
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
		
		private static void newFile(
				final Users users,
				final String arg) throws IOException {
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
				byte[] xml = users.toXml();
				out.write(xml);
				out.flush();
			} finally {
				if (out instanceof FileOutputStream) {
					out.close();
				}
			}
			if (!arg.equals("-")) {
				Files.move(
						new File(tempArg).toPath(), 
						new File(arg).toPath(), 
						StandardCopyOption.REPLACE_EXISTING);
			}
		}

		private static Users readFile(final String arg) throws IOException {
			InputStream in = null;
			if (arg.equals("-")) {
				in = System.in;
			} else {
				File file = new File(arg);
				try {
					in = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					throw new IllegalArgumentException(e);
				}
			}
			Users users = null;
			try {
				users = Users.newInstanceFrom(in);
			} catch (IOException e) { 
				throw new IllegalArgumentException(String.format(
						"error in reading XML file '%s'", arg), 
						e);
			} finally {
				if (in instanceof FileInputStream) {
					in.close();
				}
			}
			return users;
		}
		
		private static List<User> readUsers() {
			List<User> users = new ArrayList<User>();
			Console console = System.console();
			boolean addAnotherUser = false;
			do {
				console.printf("User%n");
				String name;
				while (true) {
					name = console.readLine("Name: ");
					try {
						User.validateName(name);
						break;
					} catch (IllegalArgumentException e) {
						console.printf(
								"Name must be no more than %s byte(s).%n", 
								User.MAX_NAME_LENGTH);
					}
				}
				char[] password;
				while (true) {
					password = console.readPassword("Password: ");
					try {
						User.validatePassword(password);
					} catch (IllegalArgumentException e) {
						console.printf(
								"Password must be no more than %s byte(s).%n",
								User.MAX_PASSWORD_LENGTH);
						continue;
					}
					char[] retypedPassword = console.readPassword(
							"Re-type password:");
					if (Arrays.equals(password, retypedPassword)) {
						break;
					} else {
						console.printf(
								"Password and re-typed password do not match.%n");
					}
				}
				users.add(User.newInstance(name, password));
				Arrays.fill(password, '\0');
				console.printf("User '%s' added.%n", name);
				String decision = console.readLine(
						"Would you like to enter another user? ('Y' for yes): ");
				addAnotherUser = decision.equals("Y");
			} while (addAnotherUser);
			return users;
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
		byte[] xsd = Users.getXsd();
		System.out.write(xsd);
		System.out.flush();
		this.xsdRequested = true;
	}
	
}
