package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.number.impl.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

public enum Command {
	
	@HelpText(
			doc = "",
			usage = "CONNECT"
	)
	CONNECT((byte) 0x01),
	
	@HelpText(
			doc = "",
			usage = "BIND"
	)	
	BIND((byte) 0x02),
	
	@HelpText(
			doc = "",
			usage = "UDP_ASSOCIATE"
	)	
	UDP_ASSOCIATE((byte) 0x03),
	
	@HelpText(
			doc = "",
			usage = "RESOLVE"
	)
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
	
	public static Command valueOfString(final String s) {
		Command command = null;
		try {
			command = Command.valueOf(s);
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<Command> list = Arrays.asList(Command.values());
			for (Iterator<Command> iterator = list.iterator();
					iterator.hasNext();) {
				Command value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected command must be one of the following "
							+ "values: %s. actual value is %s",
							sb.toString(),
							s), 
					e);
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
