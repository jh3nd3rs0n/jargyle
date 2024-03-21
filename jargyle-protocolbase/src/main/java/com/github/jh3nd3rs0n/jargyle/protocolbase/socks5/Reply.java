package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Reply {

	SUCCEEDED((byte) 0x00),
	
	GENERAL_SOCKS_SERVER_FAILURE((byte) 0x01),
	
	CONNECTION_NOT_ALLOWED_BY_RULESET((byte) 0x02),
	
	NETWORK_UNREACHABLE((byte) 0x03),
	
	HOST_UNREACHABLE((byte) 0x04),
	
	CONNECTION_REFUSED((byte) 0x05),
	
	TTL_EXPIRED((byte) 0x06),
	
	COMMAND_NOT_SUPPORTED((byte) 0x07),
	
	ADDRESS_TYPE_NOT_SUPPORTED((byte) 0x08);
	
	public static Reply valueOfByte(final byte b) {
		for (Reply reply : Reply.values()) {
			if (reply.byteValue() == b) {
				return reply;
			}
		}
		String str = Arrays.stream(Reply.values())
				.map(Reply::byteValue)
				.map(bv -> UnsignedByte.valueOf(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected reply must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.valueOf(b).intValue())));
	}
	
	private final byte byteValue;
	
	private Reply(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}
