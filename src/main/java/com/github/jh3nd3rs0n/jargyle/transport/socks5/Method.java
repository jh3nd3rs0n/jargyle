package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.number.impl.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

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
		StringBuilder sb = new StringBuilder();
		List<Method> list = Arrays.asList(Method.values());
		for (Iterator<Method> iterator = list.iterator(); 
				iterator.hasNext();) {
			Method value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected method must be one of the following values: "
						+ "%s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	public static Method valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		Method method = null;
		try {
			method = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		return method;
	}
	
	public static Method valueOfString(final String s) {
		Method method = null;
		try {
			method = Method.valueOf(s);
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<Method> list = Arrays.asList(Method.values());
			for (Iterator<Method> iterator = list.iterator();
					iterator.hasNext();) {
				Method value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected method must be one of the following "
							+ "values: %s. actual value is %s",
							sb.toString(),
							s), 
					e);
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
