package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod;

public final class HashedPassword {

	public static HashedPassword newInstance(final char[] password) {
		return HashedPasswordSpecConstants.PBKDF2_WITH_HMAC_SHA256_HASHED_PASSWORD.newHashedPassword(
				password);
	}
	
	public static HashedPassword newInstanceFrom(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"hashed password must be in the following format: "
					+ "TYPE_NAME:HASHED_PASSWORD_VALUE");
		}
		String typeName = sElements[0];
		String hashedPasswordValue = sElements[1];
		HashedPasswordSpec hashedPasswordSpec = 
				HashedPasswordSpecConstants.valueOfTypeName(typeName);
		return hashedPasswordSpec.newHashedPassword(hashedPasswordValue);
	}

	private final HashedPasswordSpec hashedPasswordSpec;
	private final HashedPasswordValue hashedPasswordValue;
	private final String typeName;
	
	HashedPassword(
			final HashedPasswordSpec spec,
			final HashedPasswordValue hashedPassValue) {
		this.hashedPasswordSpec = spec;
		this.hashedPasswordValue = hashedPassValue;
		this.typeName = spec.getTypeName();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		HashedPassword other = (HashedPassword) obj;
		if (!this.typeName.equals(other.typeName)) {
			return false;
		}
		return this.hashedPasswordValue.equals(
				other.hashedPasswordValue);
	}

	public HashedPasswordSpec getHashedPasswordSpec() {
		return this.hashedPasswordSpec;
	}

	public HashedPasswordValue getHashedPasswordValue() {
		return this.hashedPasswordValue;
	}
	
	public String getTypeName() {
		return this.typeName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.typeName.hashCode();
		result = prime * result + this.hashedPasswordValue.hashCode();
		return result;
	}
	
	public boolean passwordEquals(final char[] password) {
		return this.hashedPasswordValue.passwordEquals(password);
	}
	
	@Override
	public final String toString() {
		return String.format(
				"%s:%s", 
				this.typeName, 
				this.hashedPasswordValue);
	}
	
}
