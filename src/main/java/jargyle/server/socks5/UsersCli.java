package jargyle.server.socks5;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import argmatey.ArgMatey.ArgsParser;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.NonparsedArgSink;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.OptionBuilder;
import argmatey.ArgMatey.OptionSink;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.PosixOption;
import jargyle.common.cli.HelpTextParams;
import jargyle.server.SystemPropertyNameConstants;

public final class UsersCli {
	
	private enum Command implements HelpTextParams {
		
		ADD_USERS_TO_FILE("add-users-to-file") {
			
			@Override
			public String getDoc() {
				return "Add users to an existing file through an interactive prompt";
			}

			@Override
			public String getUsage() {
				return String.format("%s FILE", this);
			}

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
		CREATE_NEW_FILE("create-new-file") {
			
			@Override
			public String getDoc() {
				return "Create a new file of zero or more users through an interactive prompt";
			}

			@Override
			public String getUsage() {
				return String.format("%s FILE", this);
			}

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
		REMOVE_USER("remove-user") {
			
			@Override
			public String getDoc() {
				return "Remove user by name from an existing file";
			}

			@Override
			public String getUsage() {
				return String.format("%s NAME FILE", this);
			}

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
				final String arg) throws JAXBException, IOException {
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
				File file = new File(arg);
				File tempFile = new File(tempArg);
				if (!tempFile.renameTo(file)) {
					throw new IOException(String.format(
							"unable to rename '%s' to '%s'", tempFile, file));
				}
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
			} catch (JAXBException e) { 
				throw new IllegalArgumentException(String.format(
						"possible invalid XML file '%s'", arg), 
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
	
	private static final int HELP_OPTION_ORDINAL = 0;
	private static final int XSD_OPTION_ORDINAL = 1;
	
	private final List<String> argList;
	private ArgsParser argsParser;
	private Command command;
	private final Options options;
	private final String programBeginningUsage;
	private boolean programHelpRequested;
	private final String programName;	
	private boolean xsdRequested;
	
	public UsersCli() {
		Options opts = Options.newInstanceFrom(this.getClass());
		String progName = System.getProperty(
				SystemPropertyNameConstants.PROGRAM_NAME);
		if (progName == null) {
			progName = Users.class.getName();
		}
		String progBeginningUsage = System.getProperty(
				SystemPropertyNameConstants.PROGRAM_BEGINNING_USAGE);
		if (progBeginningUsage == null) {
			progBeginningUsage = progName;
		}
		this.argList = new ArrayList<String>();
		this.argsParser = null;
		this.command = null;
		this.options = opts;
		this.programBeginningUsage = progBeginningUsage;
		this.programHelpRequested = false;
		this.programName = progName;		
		this.xsdRequested = false;
	}
	
	@NonparsedArgSink
	public void addNonparsedArg(final String nonparsedArg) {
		if (this.command == null) {
			this.command = Command.getInstance(nonparsedArg);
		} else {
			this.argList.add(nonparsedArg);
		}
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Print this help and exit",
					name = "help",
					ordinal = HELP_OPTION_ORDINAL,
					special = true,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "h",
							type = PosixOption.class
					)
			}
	)
	public void printHelp() {
		Option helpOption = this.options.toList().get(HELP_OPTION_ORDINAL);
		Option xsdOption = this.options.toList().get(XSD_OPTION_ORDINAL);
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
		for (HelpTextParams helpTextParams : Command.values()) {
			System.out.print("  ");
			System.out.println(helpTextParams.getUsage());
			System.out.print("      ");
			System.out.println(helpTextParams.getDoc());
		}
		System.out.println();
		System.out.println("OPTIONS:");
		this.options.printHelpText();
		System.out.println();
		System.out.println();
		this.programHelpRequested = true;
	}
	
	@OptionSink(
			optionBuilder = @OptionBuilder(
					doc = "Print the XSD and exit",
					name = "xsd",
					ordinal = XSD_OPTION_ORDINAL,
					special = true,
					type = GnuLongOption.class
			),
			otherOptionBuilders = {
					@OptionBuilder(
							name = "x",
							type = PosixOption.class
					)
			}
	)
	public void printXsd() throws JAXBException, IOException {
		byte[] xsd = Users.getXsd();
		System.out.write(xsd);
		System.out.flush();
		this.xsdRequested = true;
	}
	
	public int process(final String[] args) {
		Option helpOption = this.options.toList().get(HELP_OPTION_ORDINAL);
		String suggestion = String.format(
				"Try `%s %s' for more information", 
				this.programBeginningUsage, 
				helpOption.getUsage());
		this.argsParser = ArgsParser.newInstance(args, this.options, false);
		while (this.argsParser.hasNext()) {
			try {
				this.argsParser.parseNextTo(this);
			} catch (Throwable t) {
				System.err.printf("%s: %s%n", programName, t);
				System.err.println(suggestion);
				t.printStackTrace(System.err);
				return -1;
			}
			if (this.programHelpRequested || this.xsdRequested) {
				return 0;
			}
		}
		if (this.command == null) {
			System.err.printf("%s: command must be provided%n", programName);
			System.err.println(suggestion);
			return -1;
		}
		try {
			this.command.invoke(this.argList.toArray(
					new String[this.argList.size()]));
		} catch (Exception e) {
			System.err.printf("%s: %s%n", programName, e);
			System.err.println(suggestion);
			e.printStackTrace(System.err);
			return -1;
		}
		return 0;
	}
	
}
