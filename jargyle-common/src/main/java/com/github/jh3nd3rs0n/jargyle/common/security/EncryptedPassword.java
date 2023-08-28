package com.github.jh3nd3rs0n.jargyle.common.security;

import com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl.AesCfbPkcs5PaddingEncryptedPassword;

public abstract class EncryptedPassword {
	
	public static EncryptedPassword newInstance(final char[] password) {
		return AesCfbPkcs5PaddingEncryptedPassword.newInstance(password);
	}
	
	public static EncryptedPassword newInstance(
			final Class<?> cls, final String value) {
		if (cls.equals(EncryptedPassword.class) 
				|| !EncryptedPassword.class.isAssignableFrom(cls)) {
			throw new IllegalArgumentException(String.format(
					"class must extend '%s'", 
					EncryptedPassword.class.getName()));			
		}
		if (cls.equals(AesCfbPkcs5PaddingEncryptedPassword.class)) {
			return AesCfbPkcs5PaddingEncryptedPassword.newInstance(value);
		}
		throw new IllegalArgumentException(String.format(
				"unknown EncryptedPassword: %s",
				cls.getName()));
	}
	
	public static EncryptedPassword newInstance(
			final String className, final String value) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return newInstance(cls, value);		
	}

	@Override
	public abstract boolean equals(Object obj);
	
	public abstract char[] getPassword();

	@Override
	public abstract int hashCode();
	
	
	@Override
	public String toString() {
		return String.format(
				"%s:%s", 
				this.getClass().getName(), 
				this.toValue());
	}
	
	public abstract String toValue();
	
}
