package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

public enum MessageType {

	AUTHENTICATION((byte) 0x01),
	
	ABORT((byte) 0xff),
	
	PROTECTION_LEVEL_NEGOTIATION((byte) 0x02),
	
	ENCAPSULATED_USER_DATA((byte) 0x03);
	
	public static MessageType valueOfByte(final byte b) {
		for (MessageType messageType : MessageType.values()) {
			if (messageType.byteValue() == b) {
				return messageType;
			}
		}
		String str = Arrays.stream(MessageType.values())
				.map(MessageType::byteValue)
				.map(bv -> UnsignedByte.newInstance(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected message type must be one of the following values: "
				+ "%s. actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.newInstance(b).intValue())));
	}
	
	public static MessageType valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		MessageType messageType = null;
		try {
			messageType = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return messageType;
	}
	
	private final byte byteValue;
	
	private MessageType(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
}
