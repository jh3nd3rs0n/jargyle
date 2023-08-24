package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

public abstract class HashedPassword {

	public static HashedPassword newInstance(final char[] password) {
		return Pbkdf2WithHmacSha256HashedPassword.newInstance(password);
	}

	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();

	public abstract boolean passwordEquals(final char[] password);
	
}
