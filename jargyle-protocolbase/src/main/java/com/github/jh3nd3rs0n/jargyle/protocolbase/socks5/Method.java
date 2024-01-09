package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

@EnumValueTypeDoc(
		description = "",
		name = "SOCKS5 Method",
		syntax = "NO_AUTHENTICATION_REQUIRED|GSSAPI|USERNAME_PASSWORD",
		syntaxName = "SOCKS5_METHOD"
)
public enum Method {

	@EnumValueDoc(
			description = "No authentication required", 
			value = "NO_AUTHENTICATION_REQUIRED"
	)
	NO_AUTHENTICATION_REQUIRED((byte) 0x00),
	
	@EnumValueDoc(
			description = "GSS-API authentication", 
			value = "GSSAPI"
	)
	GSSAPI((byte) 0x01),
	
	@EnumValueDoc(
			description = "Username password authentication", 
			value = "USERNAME_PASSWORD"
	)
	USERNAME_PASSWORD((byte) 0x02),
	
	NO_ACCEPTABLE_METHODS((byte) 0xff);

	public static Method valueOfByte(final byte b) {
		for (Method method : Method.values()) {
			if (method.byteValue() == b) {
				return method;
			}
		}
		String str = Arrays.stream(Method.values())
				.map(Method::byteValue)
				.map(bv -> UnsignedByte.newInstanceOf(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected method must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.newInstanceOf(b).intValue())));
	}
	
	public static Method valueOfString(final String s) {
		Method method = null;
		try {
			method = Method.valueOf(s);
		} catch (IllegalArgumentException e) {
			String str = Arrays.stream(Method.values())
					.map(Method::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected method must be one of the following values: %s. "
					+ "actual value is %s",
					str,
					s));
		}
		return method;
	}
	
	private final byte byteValue;
	
	private Method(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}

}
