package jargyle.server.socks5;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import jargyle.common.net.socks5.Command;

@XmlType(name = "socks5Command")
@XmlEnum(String.class)
public enum Socks5Command {

	BIND(Command.BIND),
	
	CONNECT(Command.CONNECT),
	
	UDP_ASSOCIATE(Command.UDP_ASSOCIATE);
	
	private final Command commandValue;
	
	private Socks5Command(final Command cmdValue) {
		this.commandValue = cmdValue;
	}
	
	public Command commandValue() {
		return this.commandValue;
	}
}
