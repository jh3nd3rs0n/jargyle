package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

public class PostMethodSubnegotiationMessageInputStream 
	extends Socks5MessageInputStream {

	PostMethodSubnegotiationMessageInputStream(final InputStream in) {
		super(in);
	}
	
	final Address readAddress() throws IOException {
		return new AddressInputStream(this.in).readAddress();
	}
	
	final Port readPort() throws IOException {
		UnsignedShort s = this.readUnsignedShort();
		return Port.newInstance(s);
	}

}
