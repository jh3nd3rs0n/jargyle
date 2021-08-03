package com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.util.UnsignedByte;

public enum Command {
	
	CONNECT((byte) 0x01),
	
	BIND((byte) 0x02),
	
	UDP_ASSOCIATE((byte) 0x03),

	RESOLVE((byte) 0x04);
	
	public static Command valueOfByte(final byte b) {
		for (Command command : Command.values()) {
			if (command.byteValue() == b) {
				return command;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<Command> list = Arrays.asList(Command.values());
		for (Iterator<Command> iterator = list.iterator(); 
				iterator.hasNext();) {
			Command value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected command must be one of the following "
						+ "values: %s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	public static Command valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		Command command = null;
		try {
			command = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		return command;
	}
	
	private final byte byteValue;
	
	private Command(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}
