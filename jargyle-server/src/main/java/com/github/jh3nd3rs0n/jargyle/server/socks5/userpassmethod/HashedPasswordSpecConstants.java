package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.internal.hashedpassvalue.impl.Pbkdf2WithHmacSha256HashedPasswordValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class HashedPasswordSpecConstants {

	private static final HashedPasswordSpecs HASHED_PASSWORD_SPECS =
			new HashedPasswordSpecs();
	
	public static final HashedPasswordSpec PBKDF2_WITH_HMAC_SHA256_HASHED_PASSWORD = HASHED_PASSWORD_SPECS.addThenGet(new HashedPasswordSpec(
			"Pbkdf2WithHmacSha256HashedPassword") {

				@Override
				public HashedPassword newHashedPassword(
						final char[] password) {
					return new HashedPassword(
							this,
							new Pbkdf2WithHmacSha256HashedPasswordValue(
									password));
				}

				@Override
				public HashedPassword newHashedPassword(
						final String hashedPasswordValue) {
					return new HashedPassword(
							this,
							new Pbkdf2WithHmacSha256HashedPasswordValue(
									hashedPasswordValue));
				}
		
	});
	
	private static final List<HashedPasswordSpec> VALUES =
			HASHED_PASSWORD_SPECS.toList();
	
	private static final Map<String, HashedPasswordSpec> VALUES_MAP =
			HASHED_PASSWORD_SPECS.toMap();
	
	public static HashedPasswordSpec valueOfTypeName(final String typeName) {
		if (VALUES_MAP.containsKey(typeName)) {
			return VALUES_MAP.get(typeName);
		}
		String str = VALUES.stream()
				.map(HashedPasswordSpec::getTypeName)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected hashed password type name must be one of the "
				+ "following values: %s. actual value is %s",
				str,
				typeName));
	}
	
	public static List<HashedPasswordSpec> values() {
		return VALUES;
	}
	
	public static Map<String, HashedPasswordSpec> valuesMap() {
		return VALUES_MAP;
	}
	
	private HashedPasswordSpecConstants() { }

}
