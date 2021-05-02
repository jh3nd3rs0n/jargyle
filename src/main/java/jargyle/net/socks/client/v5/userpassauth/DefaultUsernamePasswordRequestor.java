package jargyle.net.socks.client.v5.userpassauth;

import jargyle.io.ConsoleWrapper;
import jargyle.net.socks.client.v5.Socks5ServerUri;

public final class DefaultUsernamePasswordRequestor 
	extends UsernamePasswordRequestor {

	public DefaultUsernamePasswordRequestor() { }
	
	@Override
	public UsernamePassword requestUsernamePassword(
			final Socks5ServerUri socks5ServerUri, final String prompt) {
		ConsoleWrapper consoleWrapper = new ConsoleWrapper(System.console());
		if (prompt != null) {
			consoleWrapper.printf(prompt);
			consoleWrapper.printf("%n");
		}
		String username;
		while (true) {
			username = consoleWrapper.readLine("Username: ");
			try {
				UsernamePassword.validateUsername(username);
				break;
			} catch (IllegalArgumentException e) {
				consoleWrapper.printf(
						"Username must be no more than %s byte(s).%n", 
						UsernamePassword.MAX_USERNAME_LENGTH);
			}
		}
		char[] password;
		while (true) {
			password = consoleWrapper.readPassword("Password: ");
			try {
				UsernamePassword.validatePassword(password);
				break;
			} catch (IllegalArgumentException e) {
				consoleWrapper.printf(
						"Password must be no more than %s byte(s).%n", 
						UsernamePassword.MAX_PASSWORD_LENGTH);
			}
		}
		return UsernamePassword.newInstance(username, password);
	}
	
}
