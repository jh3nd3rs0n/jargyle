package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import java.util.stream.Collectors;

public abstract class HashedPassword {

	public static HashedPassword newInstance(final char[] password) {
		return HashedPasswordSpecConstants.PBKDF2_WITH_HMAC_SHA256_HASHED_PASSWORD.newHashedPassword(
				password);
	}
	
	public static HashedPassword newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"hashed password must be in the following format: "
					+ "TYPE_NAME:ARGUMENTS_STRING");
		}
		String typeName = sElements[0];
		String argumentsString = sElements[1];
		HashedPasswordSpec hashedPasswordSpec = null;
		try {
			hashedPasswordSpec = HashedPasswordSpecConstants.valueOfTypeName(
					typeName);
		} catch (IllegalArgumentException e) {
			String str = HashedPasswordSpecConstants.values().stream()
					.map(HashedPasswordSpec::getTypeName)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected hashed password type name must be one of the "
					+ "following values: %s. actual value is %s",
					str,
					typeName));			
		}
		return hashedPasswordSpec.newHashedPassword(argumentsString);		
	}

	private final HashedPasswordSpec hashedPasswordSpec;
	private final String typeName;
	
	public HashedPassword(final HashedPasswordSpec spec) {
		this.hashedPasswordSpec = spec;
		this.typeName = spec.getTypeName();
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	public abstract String getArgumentsString();

	public final HashedPasswordSpec getHashedPasswordSpec() {
		return this.hashedPasswordSpec;
	}
	
	public final String getTypeName() {
		return this.typeName;
	}
	
	@Override
	public abstract int hashCode();
	
	public abstract boolean passwordEquals(final char[] password);
	
	@Override
	public final String toString() {
		return String.format(
				"%s:%s", 
				this.typeName, 
				this.getArgumentsString());
	}
	
}
