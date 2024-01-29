package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortInputHelper;

final class UdpRequestHeaderInputHelper {
	
	public static UdpRequestHeader readUdpRequestHeaderFrom(
			final InputStream in) throws IOException {
		int dataStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		UnsignedShort rsv = UnsignedShortInputHelper.readUnsignedShortFrom(in);
		if (rsv.intValue() != UdpRequestHeader.RSV) {
			 throw new IllegalArgumentException(String.format(
					 "expected RSV is %s, actual RSV is %s", 
					 UdpRequestHeader.RSV, rsv.intValue()));
		}
		byte[] bytes = rsv.toByteArray();
		dataStartIndex += bytes.length;
		out.write(rsv.toByteArray());
		UnsignedByte frag = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		dataStartIndex += 2;
		out.write(frag.intValue());
		Address dstAddr = AddressInputHelper.readAddressFrom(in);
		bytes = dstAddr.toByteArray();
		dataStartIndex += bytes.length;
		out.write(bytes);		
		Port dstPort = PortInputHelper.readPortFrom(in);
		bytes = dstPort.unsignedShortValue().toByteArray();
		dataStartIndex += bytes.length;
		out.write(bytes);
		int b = -1;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		UdpRequestHeader.Params params = new UdpRequestHeader.Params();
		params.currentFragmentNumber = frag;
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort;
		params.userDataStartIndex = dataStartIndex;
		params.byteArray = out.toByteArray();
		return new UdpRequestHeader(params);		
	}
	
	private UdpRequestHeaderInputHelper() { }

}
