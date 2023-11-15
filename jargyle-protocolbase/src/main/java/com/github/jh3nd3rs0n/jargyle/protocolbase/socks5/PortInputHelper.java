package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortInputHelper;

final class PortInputHelper {
	
	public static Port readPortFrom(final InputStream in) throws IOException {
		UnsignedShort s = UnsignedShortInputHelper.readUnsignedShortFrom(in);
		return Port.newInstance(s);
	}

	private PortInputHelper() { }
	
}
