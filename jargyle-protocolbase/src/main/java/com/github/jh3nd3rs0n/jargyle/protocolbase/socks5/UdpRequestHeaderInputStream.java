package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

final class UdpRequestHeaderInputStream 
	extends PostMethodSubnegotiationMessageInputStream {

	public UdpRequestHeaderInputStream(final InputStream in) {
		super(in);
	}
	
	public UdpRequestHeader readUdpRequestHeader() throws IOException {
		int dataStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		UnsignedShort rsv = this.readUnsignedShort();
		if (rsv.intValue() != UdpRequestHeader.RSV) {
			 throw new IllegalArgumentException(String.format(
					 "expected RSV is %s, actual RSV is %s", 
					 UdpRequestHeader.RSV, rsv.intValue()));
		}
		byte[] bytes = rsv.toByteArray();
		dataStartIndex += bytes.length;
		out.write(rsv.toByteArray());
		UnsignedByte frag = this.readUnsignedByte();
		dataStartIndex += 2;
		out.write(frag.intValue());
		Address dstAddr = this.readAddress();
		bytes = dstAddr.toByteArray();
		dataStartIndex += bytes.length;
		out.write(bytes);		
		Port dstPort = this.readPort();
		bytes = dstPort.toByteArray();
		dataStartIndex += bytes.length;
		out.write(bytes);
		int b = -1;
		while ((b = this.in.read()) != -1) {
			out.write(b);
		}
		UdpRequestHeader.Params params = new UdpRequestHeader.Params();
		params.currentFragmentNumber = frag;
		params.addressType = dstAddr.getAddressType();
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort;
		params.userDataStartIndex = dataStartIndex;
		params.byteArray = out.toByteArray();
		return new UdpRequestHeader(params);		
	}

}
