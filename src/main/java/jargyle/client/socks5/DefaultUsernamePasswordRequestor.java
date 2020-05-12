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
			} catch (IllegalArgumentException e) {
				console.printf(
						"Username must be no less than %s byte(s) and no more than %s byte(s).%n", 
						UsernamePassword.MIN_USERNAME_LENGTH,
						UsernamePassword.MAX_USERNAME_LENGTH);
				continue;
			}
			break;
		}
		char[] password;
		while (true) {
			password = console.readPassword("Password: ");
			try {
				UsernamePassword.validatePassword(password);
			} catch (IllegalArgumentException e) {
				console.printf(
						"Password must be no less than %s byte(s) and no more than %s byte(s).%n", 
						UsernamePassword.MIN_PASSWORD_LENGTH,
						UsernamePassword.MAX_PASSWORD_LENGTH);
				continue;
			}
			break;
		}
		return UsernamePassword.newInstance(username, password);
	}
	
}
