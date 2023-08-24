package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public enum Method {

	@HelpText(doc = "No authentication required", usage = "NO_AUTHENTICATION_REQUIRED")
	NO_AUTHENTICATION_REQUIRED((byte) 0x00),
	
	@HelpText(doc = "GSS-API authentication", usage = "GSSAPI")
	GSSAPI((byte) 0x01),
	
	@HelpText(doc = "Username password authentication", usage = "USERNAME_PASSWORD")
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
				.map(bv -> UnsignedByte.newInstance(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected method must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.newInstance(b).intValue())));
	}
	
	public static Method valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		Method method = null;
		try {
			method = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return method;
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
