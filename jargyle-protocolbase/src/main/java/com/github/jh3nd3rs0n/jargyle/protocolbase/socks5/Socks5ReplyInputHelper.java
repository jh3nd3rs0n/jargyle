package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;

public final class Socks5ReplyInputHelper {
	
	private static Reply readReplyFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		Reply reply = null;
		try {
			reply = Reply.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return reply;
	}
	
	public static Socks5Reply readSocks5ReplyFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = VersionInputHelper.readVersionFrom(in);
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		Reply rep = readReplyFrom(in);
		out.write(UnsignedByte.newInstance(rep.byteValue()).intValue());
		UnsignedByte rsv = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		if (rsv.intValue() != Socks5Reply.RSV) {
			throw new Socks5Exception(String.format(
					"expected RSV is %s, actual RSV is %s", 
					Socks5Reply.RSV, rsv.intValue()));
		}
		out.write(rsv.intValue());
		Address bndAddr = AddressInputHelper.readAddressFrom(in);
		out.write(bndAddr.toByteArray());
		Port bndPort = PortInputHelper.readPortFrom(in);
		out.write(bndPort.toByteArray());
		Socks5Reply.Params params = new Socks5Reply.Params();
		params.version = ver;
		params.reply = rep;
		params.addressType = bndAddr.getAddressType();
		params.serverBoundAddress = bndAddr;
		params.serverBoundPort = bndPort;
		params.byteArray = out.toByteArray();
		return new Socks5Reply(params);		
	}
	
	private Socks5ReplyInputHelper() { }

}
