package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortIoHelper;

import java.io.IOException;
import java.io.InputStream;

final class PortIoHelper {
	
	public static Port readPortFrom(final InputStream in) throws IOException {
		UnsignedShort s = UnsignedShortIoHelper.readUnsignedShortFrom(in);
		return Port.valueOf(s);
	}

	public static byte[] toByteArray(final Port port) {
		return UnsignedShortIoHelper.toByteArray(port.unsignedShortValue());
	}

	private PortIoHelper() { }
	
}
