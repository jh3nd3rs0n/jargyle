package jargyle.common.net.socks5;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.common.util.UnsignedByte;

public enum Command {

	RESOLVE((byte) 0x00),
	
	CONNECT((byte) 0x01),
	
	BIND((byte) 0x02),
	
	UDP_ASSOCIATE((byte) 0x03);
	
	public static Command valueOf(final byte b) {
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
	
	private final byte byteValue;
	
	private Command(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}
