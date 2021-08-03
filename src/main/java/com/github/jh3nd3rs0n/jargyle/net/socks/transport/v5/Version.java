package com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.util.UnsignedByte;

public enum Version {

	V5((byte) 0x05);
	
	public static Version valueOfByte(final byte b) {
		for (Version version : Version.values()) {
			if (version.byteValue() == b) {
				return version;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<Version> list = Arrays.asList(Version.values());
		for (Iterator<Version> iterator = list.iterator();
				iterator.hasNext();) {
			Version value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected version must be one of the following "
						+ "values: %s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	public static Version valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		Version version = null;
		try {
			version = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
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
