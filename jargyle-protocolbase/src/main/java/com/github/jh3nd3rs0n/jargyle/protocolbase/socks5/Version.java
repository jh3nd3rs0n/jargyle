package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;

public enum Version {

	V5((byte) 0x05);
	
	public static Version valueOfByte(final byte b) {
		for (Version version : Version.values()) {
			if (version.byteValue() == b) {
				return version;
			}
		}
		String str = Arrays.stream(Version.values())
				.map(Version::byteValue)
				.map(bv -> UnsignedByte.newInstance(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected version must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.newInstance(b).intValue())));
	}
	
	public static Version valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		Version version = null;
		try {
			version = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return version;
	}
	
	private final byte byteValue;
	
	private Version(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}
