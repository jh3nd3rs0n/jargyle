package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class ServerMethodSelectionMessageInputStream 
	extends MethodSelectionMessageInputStream {

	public ServerMethodSelectionMessageInputStream(InputStream in) {
		super(in);
	}
	
	public ServerMethodSelectionMessage readServerMethodSelectionMessage() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = this.readVersion();
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		Method meth = this.readMethod();
		out.write(UnsignedByte.newInstance(meth.byteValue()).intValue());
		ServerMethodSelectionMessage.Params params = 
				new ServerMethodSelectionMessage.Params();
		params.version = ver;
		params.method = meth;
		params.byteArray = out.toByteArray();
		return new ServerMethodSelectionMessage(params);		
	}

}
