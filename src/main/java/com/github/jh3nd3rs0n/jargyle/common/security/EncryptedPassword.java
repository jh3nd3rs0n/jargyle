package com.github.jh3nd3rs0n.jargyle.common.security;

import com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl.AesCfbPkcs5PaddingEncryptedPassword;

public abstract class EncryptedPassword {
	
	public static EncryptedPassword newInstance(final char[] password) {
		return AesCfbPkcs5PaddingEncryptedPassword.newInstance(password);
	}

	@Override
	public abstract boolean equals(Object obj);
	
	public abstract char[] getPassword();
	
	@Override
	public abstract int hashCode();
	
}
