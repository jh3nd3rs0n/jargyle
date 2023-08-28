package com.github.jh3nd3rs0n.jargyle.common.security;

import com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl.AesCfbPkcs5PaddingEncryptedPassword;

public abstract class EncryptedPassword {
	
	public static EncryptedPassword newInstance(final char[] password) {
		return AesCfbPkcs5PaddingEncryptedPassword.newInstance(password);
	}
	
	private static EncryptedPassword newInstance(
			final Class<?> cls, final String argumentsValue) {
		if (cls.equals(EncryptedPassword.class) 
				|| !EncryptedPassword.class.isAssignableFrom(cls)) {
			throw new IllegalArgumentException(String.format(
					"class must extend '%s'", 
					EncryptedPassword.class.getName()));			
		}
		if (cls.equals(AesCfbPkcs5PaddingEncryptedPassword.class)) {
			return AesCfbPkcs5PaddingEncryptedPassword.newInstance(
					argumentsValue);
		}
		throw new IllegalArgumentException(String.format(
				"unknown EncryptedPassword: %s",
				cls.getName()));
	}
	
	public static EncryptedPassword newInstance(
			final String className, final String argumentsValue) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		return newInstance(cls, argumentsValue);		
	}

	@Override
	public abstract boolean equals(Object obj);
	
	public abstract String getArgumentsValue();

	public abstract char[] getPassword();
	
	@Override
	public abstract int hashCode();
	
	@Override
	public final String toString() {
		return String.format(
				"%s:%s", 
				this.getClass().getName(), 
				this.getArgumentsValue());
	}
	
}
