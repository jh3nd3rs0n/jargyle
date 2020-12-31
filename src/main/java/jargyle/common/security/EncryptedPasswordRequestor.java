package jargyle.common.security;

public abstract class EncryptedPasswordRequestor {

	private static EncryptedPasswordRequestor defaultInstance;
	
	public static EncryptedPasswordRequestor getDefault() {
		return defaultInstance;
	}
	
	public static void setDefault(final EncryptedPasswordRequestor requestor) {
		defaultInstance = requestor;
	}
	
	public EncryptedPasswordRequestor() { }
	
	public abstract EncryptedPassword requestEncryptedPassword(
			final String prompt);
	
}
