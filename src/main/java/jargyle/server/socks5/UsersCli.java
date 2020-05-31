package jargyle.server.socks5;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import argmatey.ArgMatey.ArgsParser;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.ParseResultHolder;
import jargyle.common.cli.HelpTextParams;
import jargyle.server.SystemPropertyNames;

final class UsersCli {
	
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
		
		private final String value;
		
		private Command(final String val) {
			this.value = val;
		}
		
		public abstract void invoke(final String[] args) throws Exception;
		
		public String toString() {
			return this.value;
		}
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

	private static Users readFile(
			final String arg) throws JAXBException, IOException {
		InputStream in = null;
		if (arg.equals("-")) {
			in = System.in;
		} else {
			File file = new File(arg);
			in = new FileInputStream(file);
		}
		Users users = null;
		try {
			users = Users.newInstanceFrom(in);
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
				} catch (IllegalArgumentException e) {
					console.printf(
							"Name must be no more than %s byte(s).%n", 
							User.MAX_NAME_LENGTH);
					continue;
				}
				break;
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
				char[] retypedPassword = console.readPassword("Re-type password:");
				if (!Arrays.equals(password, retypedPassword)) {
					console.printf("Password and re-typed password do not match.%n");
					continue;
				}
				break;
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
	
	public UsersCli() { }
	
	private void printHelp(
			final String programBeginningUsage, final Options options) {
		System.out.printf("Usage: %s COMMAND%n", programBeginningUsage);
		System.out.printf("       %s %s%n", 
				programBeginningUsage, 
				UsersCliOptions.HELP_OPTION.getUsage());
		System.out.printf("       %s %s", 
				programBeginningUsage, 
				UsersCliOptions.XSD_OPTION.getUsage());
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
		options.printHelpText();
		System.out.println();
		System.out.println();
	}
	
	private void printXsd() throws JAXBException, IOException {
		byte[] xsd = Users.getXsd();
		System.out.write(xsd);
		System.out.flush();
	}
	
	public void process(final String[] args) {
		Options options = new UsersCliOptions();
		ArgsParser argsParser = ArgsParser.newInstance(
				args, options, false);
		String programName = System.getProperty(
				SystemPropertyNames.PROGRAM_NAME_PROPERTY_NAME);
		if (programName == null) {
			programName = Users.class.getName();
		}
		String programBeginningUsage = System.getProperty(
				SystemPropertyNames.PROGRAM_BEGINNING_USAGE_PROPERTY_NAME);
		if (programBeginningUsage == null) {
			programBeginningUsage = programName;
		}
		String suggestion = String.format(
				"Try `%s %s' for more information", 
				programBeginningUsage, 
				UsersCliOptions.HELP_OPTION.getUsage());
		Command command = null;
		List<String> argList = new ArrayList<String>();
		while (argsParser.hasNext()) {
			ParseResultHolder parseResultHolder = null;
			try {
				parseResultHolder = argsParser.parseNext();
			} catch (RuntimeException e) {
				System.err.printf("%s: %s%n", programName, e.toString());
				System.err.println(suggestion);
				System.exit(-1);
			}
			if (parseResultHolder.hasOptionOfAnyOf("--help", "-h")) {
				this.printHelp(programBeginningUsage, argsParser.getOptions());
				System.exit(0);
			}
			if (parseResultHolder.hasOptionOfAnyOf("--xsd", "-x")) {
				try {
					this.printXsd();
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
			if (parseResultHolder.hasNonparsedArg()) {
				String nonparsedArg = parseResultHolder.getNonparsedArg();
				if (command == null) {
					try {
						command = Command.getInstance(nonparsedArg);
					} catch (IllegalArgumentException e) {
						System.err.printf("%s: %s%n", programName, e.toString());
						System.err.println(suggestion);
						System.exit(-1);
					}
				} else {
					argList.add(nonparsedArg);
				}
			}
		}
		if (command == null) {
			System.err.printf("%s: command must be provided%n", programName);
			System.err.println(suggestion);
			System.exit(-1);
		}
		try {
			command.invoke(argList.toArray(new String[argList.size()]));
		} catch (Exception e) {
			System.err.printf("%s: %s%n", programName, e.toString());
			System.exit(-1);
		}
	}
	
}
