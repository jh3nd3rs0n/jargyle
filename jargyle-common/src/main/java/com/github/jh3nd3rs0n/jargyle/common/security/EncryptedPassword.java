package com.github.jh3nd3rs0n.jargyle.common.security;

public abstract class EncryptedPassword {
	
	public static EncryptedPassword newInstance(final char[] password) {
		return EncryptedPasswordSpecConstants.AES_CFB_PKCS5PADDING_ENCRYPTED_PASSWORD.newEncryptedPassword(
				password);
	}
	
	public static EncryptedPassword newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"encrypted password must be in the following format: "
					+ "TYPE_NAME:ARGUMENTS_STRING");
		}
		String typeName = sElements[0];
		String argumentsString = sElements[1];
		return newInstance(typeName, argumentsString);
	}
	
	public static EncryptedPassword newInstance(
			final String typeName, final String argumentsString) {
		EncryptedPasswordSpec encryptedPasswordSpec = 
				EncryptedPasswordSpecConstants.valueOfTypeName(typeName);
		return encryptedPasswordSpec.newEncryptedPassword(argumentsString);		
	}

	private final EncryptedPasswordSpec encryptedPasswordSpec;
	private final String typeName;
	
	public EncryptedPassword(final EncryptedPasswordSpec spec) {
		this.encryptedPasswordSpec = spec;
		this.typeName = spec.getTypeName();
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	public abstract String getArgumentsString();

	public final EncryptedPasswordSpec getEncryptedPasswordSpec() {
		return this.encryptedPasswordSpec;
	}
	
	public abstract char[] getPassword();
	
	public final String getTypeName() {
		return this.typeName;
	}
	
	@Override
	public abstract int hashCode();
	
	@Override
	public final String toString() {
		return String.format(
				"%s:%s", 
				this.typeName, 
				this.getArgumentsString());
	}
	
}
