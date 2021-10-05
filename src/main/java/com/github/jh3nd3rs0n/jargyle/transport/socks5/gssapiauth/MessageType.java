package com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.number.impl.UnsignedByte;

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
		StringBuilder sb = new StringBuilder();
		List<MessageType> list = Arrays.asList(MessageType.values());
		for (Iterator<MessageType> iterator = list.iterator(); 
				iterator.hasNext();) {
			MessageType value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected message type must be one of the following "
						+ "values: %s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	public static MessageType valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		MessageType messageType = null;
		try {
			messageType = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
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
