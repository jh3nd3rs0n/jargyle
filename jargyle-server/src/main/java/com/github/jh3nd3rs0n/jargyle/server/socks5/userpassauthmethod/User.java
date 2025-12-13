package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod.Request;

import java.util.Objects;

public final class User {

	public static final int MAX_NAME_LENGTH = 
			Request.MAX_UNAME_LENGTH;
	public static final int MAX_PASSWORD_LENGTH = 
			Request.MAX_PASSWD_LENGTH;
	
	public static User newInstance(final String name, final char[] password) {
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(password, "password must not be null");
		validateName(name);
		validatePassword(password);
		return new User(name, HashedPassword.newInstance(password));
	}
	
	public static User newInstance(
			final String name, final HashedPassword hashedPassword) {
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(
				hashedPassword, "hashed password must not be null");
		validateName(name);
		return new User(name, hashedPassword);
	}

	public static void validateName(final String name) {
		Request.validateUsername(name);
	}
	
	public static void validatePassword(final char[] password) {
		Request.validatePassword(password);
	}
	
	private final String name;
	private final HashedPassword hashedPassword;
	
	private User(final String n, final HashedPassword hashedPssword) {
		this.name = n;
		this.hashedPassword = hashedPssword;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	public HashedPassword getHashedPassword() {
		return this.hashedPassword;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
				.append(" [name=")
				.append(this.name)
				.append(", hashedPassword=")
				.append(this.hashedPassword)
				.append("]");
		return builder.toString();
	}
}
