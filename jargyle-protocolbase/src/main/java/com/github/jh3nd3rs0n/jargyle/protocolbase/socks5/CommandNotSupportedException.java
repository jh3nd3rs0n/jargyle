package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class CommandNotSupportedException extends Socks5Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final UnsignedByte command;
	
	public CommandNotSupportedException(final UnsignedByte cmd) {
		super(String.format(
				"command not supported: %s", 
				Integer.toHexString(cmd.intValue())));
		this.command = cmd;
	}

	public UnsignedByte getCommand() {
		return this.command;
	}
	
}
