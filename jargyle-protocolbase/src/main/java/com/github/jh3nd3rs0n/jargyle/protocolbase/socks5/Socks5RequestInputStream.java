package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class Socks5RequestInputStream 
	extends PostMethodSubnegotiationMessageInputStream {

	public Socks5RequestInputStream(final InputStream in) {
		super(in);
	}
	
	private Command readCommand() throws IOException {
		UnsignedByte b = this.readUnsignedByte();
		Command command = null;
		try {
			command = Command.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new CommandNotSupportedException(b);
		}
		return command;		
	}
	
	public Socks5Request readSocks5Request() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = this.readVersion();
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		Command cmd = this.readCommand();
		out.write(UnsignedByte.newInstance(cmd.byteValue()).intValue());
		UnsignedByte rsv = this.readUnsignedByte();
		if (rsv.intValue() != Socks5Request.RSV) {
			throw new Socks5Exception(String.format(
					"expected RSV is %s, actual RSV is %s", 
					Socks5Request.RSV, rsv.intValue()));
		}
		out.write(rsv.intValue());
		Address dstAddr = this.readAddress();
		out.write(dstAddr.toByteArray());
		Port dstPort = this.readPort();
		out.write(dstPort.toByteArray());
		Socks5Request.Params params = new Socks5Request.Params();
		params.version = ver;
		params.command = cmd;
		params.addressType = dstAddr.getAddressType();
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort;
		params.byteArray = out.toByteArray();
		return new Socks5Request(params);		
	}

}
