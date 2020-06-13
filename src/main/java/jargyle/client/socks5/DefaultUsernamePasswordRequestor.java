package jargyle.client.socks5;

import java.io.Console;

public final class DefaultUsernamePasswordRequestor 
	extends UsernamePasswordRequestor {

	public DefaultUsernamePasswordRequestor() { }
	
	@Override
	public UsernamePassword requestUsernamePassword(
			final Socks5ServerUri socks5ServerUri, final String prompt) {
		Console console = System.console();
		if (prompt != null) {
			console.printf(prompt);
			console.printf("%n");
		}
		String username;
		while (true) {
			username = console.readLine("Username: ");
			try {
				UsernamePassword.validateUsername(username);
				break;
			} catch (IllegalArgumentException e) {
				console.printf(
						"Username must be no more than %s byte(s).%n", 
						UsernamePassword.MAX_USERNAME_LENGTH);
			}
		}
		char[] password;
		while (true) {
			password = console.readPassword("Password: ");
			try {
				UsernamePassword.validatePassword(password);
				break;
			} catch (IllegalArgumentException e) {
				console.printf(
						"Password must be no more than %s byte(s).%n", 
						UsernamePassword.MAX_PASSWORD_LENGTH);
			}
		}
		return UsernamePassword.newInstance(username, password);
	}
	
}
