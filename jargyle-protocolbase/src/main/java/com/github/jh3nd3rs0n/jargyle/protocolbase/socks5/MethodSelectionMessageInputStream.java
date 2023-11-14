package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public class MethodSelectionMessageInputStream 
	extends Socks5MessageInputStream {

	MethodSelectionMessageInputStream(final InputStream in) {
		super(in);
	}
	
	final Method readMethod() throws IOException {
		UnsignedByte b = this.readUnsignedByte();
		Method method = null;
		try {
			method = Method.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return method;		
	}

}
