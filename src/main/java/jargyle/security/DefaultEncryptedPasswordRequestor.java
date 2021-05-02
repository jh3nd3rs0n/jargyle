package jargyle.security;

import jargyle.io.ConsoleWrapper;

public final class DefaultEncryptedPasswordRequestor 
	extends EncryptedPasswordRequestor {

	public DefaultEncryptedPasswordRequestor() { }
	
	@Override
	public EncryptedPassword requestEncryptedPassword(final String prompt) {
		ConsoleWrapper consoleWrapper = new ConsoleWrapper(System.console());
		return EncryptedPassword.newInstance(consoleWrapper.readPassword(
				prompt));
	}

}
