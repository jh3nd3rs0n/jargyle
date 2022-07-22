package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

final class HashedPasswordValueHelper {

	public static HashedPassword toHashedPassword(final String value) {
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
			return Pbkdf2WithHmacSha256HashedPasswordValueHelper.toHashedPassword(
					value);
		}
		throw new IllegalArgumentException(String.format(
				"no value helper for %s",
				cls.getName()));		
	}
	
	public static String toValue(final HashedPassword hashedPassword) {
		Objects.requireNonNull(hashedPassword);
		if (hashedPassword instanceof Pbkdf2WithHmacSha256HashedPassword) {
			return Pbkdf2WithHmacSha256HashedPasswordValueHelper.toValue((
					(Pbkdf2WithHmacSha256HashedPassword) hashedPassword)); 
		}
		throw new IllegalArgumentException(String.format(
				"no value helper for %s",
				hashedPassword.getClass().getName()));
	}
	
	private HashedPasswordValueHelper() { }
	
}
