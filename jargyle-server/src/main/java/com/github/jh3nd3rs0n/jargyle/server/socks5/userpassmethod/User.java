package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.UsernamePasswordRequest;

public final class User {

	public static final int MAX_NAME_LENGTH = 
			UsernamePasswordRequest.MAX_UNAME_LENGTH;
	public static final int MAX_PASSWORD_LENGTH = 
			UsernamePasswordRequest.MAX_PASSWD_LENGTH;
	
	public static User newInstance(final String name, final char[] password) {
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(password, "password must not be null");
		validateName(name);
		validatePassword(password);
		return new User(name, HashedPassword.newInstance(password));
	}
	
	public static User newInstance(
			final String name, final HashedPassword hashedPassword) {
		return new User(name, hashedPassword);
	}
	
	public static User newInstanceFromUserWithHashedPassword(
			final String s) {
		String[] sElements = s.split(":");
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"user must be in the following format: NAME:HASHED_PASSWORD");
		}
		String name = null;
		try {
			name = URLDecoder.decode(sElements[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		String hashedPassword = null;
		try {
			hashedPassword = URLDecoder.decode(sElements[1], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return newInstance(name, HashedPassword.newInstance(hashedPassword));
	}
	
	public static User newInstanceFromUserWithPlaintextPassword(
			final String s) {
		String[] sElements = s.split(":");
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"user must be in the following format: NAME:PASSWORD");
		}
		String name = null;
		try {
			name = URLDecoder.decode(sElements[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		String password = null;
		try {
			password = URLDecoder.decode(sElements[1], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return newInstance(name, password.toCharArray());
	}
	
	public static void validateName(final String name) {
		UsernamePasswordRequest.validateUsername(name);
	}
	
	public static void validatePassword(final char[] password) {
		UsernamePasswordRequest.validatePassword(password);
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
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(this.name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		String encodedHashedPassword = null;
		try {
			encodedHashedPassword = URLEncoder.encode(
					this.hashedPassword.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return String.format(
				"%s:%s", 
				encodedName,
				encodedHashedPassword);
	}
}
