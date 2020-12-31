package jargyle.common.security;

public final class DefaultEncryptedPasswordRequestor 
	extends EncryptedPasswordRequestor {

	public DefaultEncryptedPasswordRequestor() { }
	
	@Override
	public EncryptedPassword requestEncryptedPassword(final String prompt) {
		return EncryptedPassword.newInstance(System.console().readPassword(
				prompt));
	}

}
