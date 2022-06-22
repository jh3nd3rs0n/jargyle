package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

abstract class HashedPasswordValue {

	public static HashedPasswordValue newInstance(
			final HashedPassword hashedPassword) {
		Objects.requireNonNull(hashedPassword);
		if (hashedPassword instanceof Pbkdf2WithHmacSha256HashedPassword) {
			return new Pbkdf2WithHmacSha256HashedPasswordValue(
					(Pbkdf2WithHmacSha256HashedPassword) hashedPassword); 
		}
		throw new IllegalArgumentException(String.format(
				"no %s for %s", 
				HashedPasswordValue.class.getName(),
				hashedPassword.getClass().getName()));
	}
	
	public static HashedPasswordValue newInstance(final String value) {
		String[] valueElements = value.split(":", 2);
		String className = valueElements[0];
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.format(
					"%s not found", 
					className));
		}
		if (cls.equals(Pbkdf2WithHmacSha256HashedPassword.class)) {
			return new Pbkdf2WithHmacSha256HashedPasswordValue(value);
		}
		throw new IllegalArgumentException(String.format(
				"no %s for %s", 
				HashedPasswordValue.class.getName(),
				cls.getName()));
	}
	
	public abstract HashedPassword toHashedPassword();
	
	@Override
	public abstract String toString();
	
}
