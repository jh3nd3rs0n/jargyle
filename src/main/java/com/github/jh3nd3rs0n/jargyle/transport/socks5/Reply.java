package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.number.impl.UnsignedByte;

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
		StringBuilder sb = new StringBuilder();
		List<Reply> list = Arrays.asList(Reply.values());
		for (Iterator<Reply> iterator = list.iterator(); iterator.hasNext();) {
			Reply value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected reply must be one of the following values: "
						+ "%s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	public static Reply valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		Reply reply = null;
		try {
			reply = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return reply;
	}
	
	private final byte byteValue;
	
	private Reply(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}
