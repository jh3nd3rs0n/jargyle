package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

public abstract class HashedPassword {

	public static HashedPassword newInstance(final char[] password) {
		return Pbkdf2WithHmacSha256HashedPassword.newInstance(password);
	}
	
	public static HashedPassword newInstance(
			final Class<?> cls, final String value) {
		if (cls.equals(HashedPassword.class) 
				|| !HashedPassword.class.isAssignableFrom(cls)) {
			throw new IllegalArgumentException(String.format(
					"class must extend '%s'", 
					HashedPassword.class.getName()));			
		}
		if (cls.equals(Pbkdf2WithHmacSha256HashedPassword.class)) {
			return Pbkdf2WithHmacSha256HashedPassword.newInstance(value);
		}
		throw new IllegalArgumentException(String.format(
				"unknown HashedPassword: %s",
				cls.getName()));
	}
	
	public static HashedPassword newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"user repository must be in the following format: "
					+ "CLASS_NAME:VALUE");
		}
		String className = sElements[0];
		String value = sElements[1];
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"%s not found", 
					className));
		}
		return newInstance(cls, value);		
	}

	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();

	public abstract boolean passwordEquals(final char[] password);
	
	@Override
	public String toString() {
		return String.format(
				"%s:%s", 
				this.getClass().getName(), 
				this.toValue());
	}
	
	public abstract String toValue();
	
}
