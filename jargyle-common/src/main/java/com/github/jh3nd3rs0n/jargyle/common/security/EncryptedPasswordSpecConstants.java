package com.github.jh3nd3rs0n.jargyle.common.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl.AesCfbPkcs5PaddingEncryptedPassword;

public final class EncryptedPasswordSpecConstants {

	private static final EncryptedPasswordSpecs ENCRYPTED_PASSWORD_SPECS =
			new EncryptedPasswordSpecs();
	
	public static final EncryptedPasswordSpec AES_CFB_PKCS5PADDING_ENCRYPTED_PASSWORD = ENCRYPTED_PASSWORD_SPECS.addThenGet(new EncryptedPasswordSpec(
			"AesCfbPkcs5PaddingEncryptedPassword") {

				@Override
				public EncryptedPassword newEncryptedPassword(
						final char[] password) {
					return AesCfbPkcs5PaddingEncryptedPassword.newInstance(
							this, password);
				}

				@Override
				public EncryptedPassword newEncryptedPassword(
						final String argumentsString) {
					return AesCfbPkcs5PaddingEncryptedPassword.newInstance(
							this, argumentsString);
				}
		
	});
	
	private static final List<EncryptedPasswordSpec> VALUES = 
			ENCRYPTED_PASSWORD_SPECS.toList();
	
	private static final Map<String, EncryptedPasswordSpec> VALUES_MAP =
			ENCRYPTED_PASSWORD_SPECS.toMap();
	
	public static EncryptedPasswordSpec valueOfTypeName(final String typeName) {
		if (VALUES_MAP.containsKey(typeName)) {
			return VALUES_MAP.get(typeName);
		}
		String str = VALUES.stream()
				.map(EncryptedPasswordSpec::getTypeName)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected encrypted password spec must be one of the following "
				+ "values: %s. actual value is %s",
				str,
				typeName));
	}
	
	public static List<EncryptedPasswordSpec> values() {
		return VALUES;
	}
	
	public static Map<String, EncryptedPasswordSpec> valuesMap() {
		return VALUES_MAP;
	}
	
	private EncryptedPasswordSpecConstants() { }

}
