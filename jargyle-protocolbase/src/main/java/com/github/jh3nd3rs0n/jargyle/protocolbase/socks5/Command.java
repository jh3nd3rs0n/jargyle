package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public enum Command {
	
	@HelpText(
			doc = "A request to the SOCKS server to connect to another server",
			usage = "CONNECT"
	)
	CONNECT((byte) 0x01),
	
	@HelpText(
			doc = "A request to the SOCKS server to bind to another address "
					+ "and port in order to receive an inbound connection",
			usage = "BIND"
	)	
	BIND((byte) 0x02),
	
	@HelpText(
			doc = "A request to the SOCKS server to associate a UDP socket for "
					+ "sending and receiving datagrams",
			usage = "UDP_ASSOCIATE"
	)	
	UDP_ASSOCIATE((byte) 0x03),
	
	@HelpText(
			doc = "A request to the SOCKS server to resolve a host name",
			usage = "RESOLVE"
	)
	RESOLVE((byte) 0x04);
	
	public static Command valueOfByte(final byte b) {
		for (Command command : Command.values()) {
			if (command.byteValue() == b) {
				return command;
			}
		}
		String str = Arrays.stream(Command.values())
				.map(Command::byteValue)
				.map(bv -> UnsignedByte.newInstance(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected command must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.newInstance(b).intValue())));
	}
	
	public static Command valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		Command command = null;
		try {
			command = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new CommandNotSupportedException(b);
		}
		return command;
	}
	
	public static Command valueOfString(final String s) {
		Command command = null;
		try {
			command = Command.valueOf(s);
		} catch (IllegalArgumentException e) {
			String str = Arrays.stream(Command.values())
					.map(Command::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected command must be one of the following values: %s. "
					+ "actual value is %s",
					str,
					s));
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
