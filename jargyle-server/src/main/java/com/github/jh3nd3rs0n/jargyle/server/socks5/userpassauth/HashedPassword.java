package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

public abstract class HashedPassword {

	public static HashedPassword newInstance(final char[] password) {
		return Pbkdf2WithHmacSha256HashedPassword.newInstance(password);
	}
	
	private static HashedPassword newInstance(
			final Class<?> cls, final String argumentsValue) {
		if (cls.equals(HashedPassword.class) 
				|| !HashedPassword.class.isAssignableFrom(cls)) {
			throw new IllegalArgumentException(String.format(
					"class must extend '%s'", 
					HashedPassword.class.getName()));			
		}
		if (cls.equals(Pbkdf2WithHmacSha256HashedPassword.class)) {
			return Pbkdf2WithHmacSha256HashedPassword.newInstance(
					argumentsValue);
		}
		throw new IllegalArgumentException(String.format(
				"unknown HashedPassword: %s",
				cls.getName()));
	}
	
	public static HashedPassword newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"hashed password must be in the following format: "
					+ "CLASS_NAME:ARGUMENTS_VALUE");
		}
		String className = sElements[0];
		String argumentsValue = sElements[1];
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"%s not found", 
					className));
		}
		return newInstance(cls, argumentsValue);		
	}

	@Override
	public abstract boolean equals(Object obj);
	
	public abstract String getArgumentsValue();

	@Override
	public abstract int hashCode();
	
	public abstract boolean passwordEquals(final char[] password);
	
	@Override
	public final String toString() {
		return String.format(
				"%s:%s", 
				this.getClass().getName(), 
				this.getArgumentsValue());
	}
	
}
