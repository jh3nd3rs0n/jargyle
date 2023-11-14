package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class Socks5ReplyInputStream 
	extends PostMethodSubnegotiationMessageInputStream {

	public Socks5ReplyInputStream(final InputStream in) {
		super(in);
	}
	
	private Reply readReply() throws IOException {
		UnsignedByte b = this.readUnsignedByte();
		Reply reply = null;
		try {
			reply = Reply.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return reply;
	}
	
	public Socks5Reply readSocks5Reply() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = this.readVersion();
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		Reply rep = this.readReply();
		out.write(UnsignedByte.newInstance(rep.byteValue()).intValue());
		UnsignedByte rsv = this.readUnsignedByte();
		if (rsv.intValue() != Socks5Reply.RSV) {
			throw new Socks5Exception(String.format(
					"expected RSV is %s, actual RSV is %s", 
					Socks5Reply.RSV, rsv.intValue()));
		}
		out.write(rsv.intValue());
		Address bndAddr = this.readAddress();
		out.write(bndAddr.toByteArray());
		Port bndPort = this.readPort();
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

}
