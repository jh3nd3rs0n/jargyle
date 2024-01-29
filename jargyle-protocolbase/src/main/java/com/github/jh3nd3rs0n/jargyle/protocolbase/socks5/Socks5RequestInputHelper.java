package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;

public final class Socks5RequestInputHelper {
	
	private static Command readCommandFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		Command command = null;
		try {
			command = Command.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new CommandNotSupportedException(b);
		}
		return command;		
	}
	
	public static Socks5Request readSocks5RequestFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = VersionInputHelper.readVersionFrom(in);
		out.write(UnsignedByte.valueOf(ver.byteValue()).intValue());
		Command cmd = readCommandFrom(in);
		out.write(UnsignedByte.valueOf(cmd.byteValue()).intValue());
		UnsignedByte rsv = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		if (rsv.intValue() != Socks5Request.RSV) {
			throw new Socks5Exception(String.format(
					"expected RSV is %s, actual RSV is %s", 
					Socks5Request.RSV, rsv.intValue()));
		}
		out.write(rsv.intValue());
		Address dstAddr = AddressInputHelper.readAddressFrom(in);
		out.write(dstAddr.toByteArray());
		Port dstPort = PortInputHelper.readPortFrom(in);
		out.write(dstPort.unsignedShortValue().toByteArray());
		Socks5Request.Params params = new Socks5Request.Params();
		params.version = ver;
		params.command = cmd;
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort;
		params.byteArray = out.toByteArray();
		return new Socks5Request(params);		
	}
	
	private Socks5RequestInputHelper() { }

}
