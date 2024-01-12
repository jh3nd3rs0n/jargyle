package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public enum Version {

	V1((byte) 0x01);
	
	public static Version valueOfByte(final byte b) {
		for (Version version : Version.values()) {
			if (version.byteValue() == b) {
				return version;
			}
		}
		String str = Arrays.stream(Version.values())
				.map(Version::byteValue)
				.map(bv -> UnsignedByte.valueOf(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected version must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.valueOf(b).intValue())));
	}
	
	private final byte byteValue;
	
	private Version(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}
