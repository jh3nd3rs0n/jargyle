package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class ClientMethodSelectionMessageInputStream 
	extends MethodSelectionMessageInputStream {

	public ClientMethodSelectionMessageInputStream(final InputStream in) {
		super(in);
	}
	
	public ClientMethodSelectionMessage readClientMethodSelectionMessage() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = this.readVersion();
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		UnsignedByte methodCount = this.readUnsignedByte();
		List<Method> meths = new ArrayList<Method>();
		for (int i = 0; i < methodCount.intValue(); i++) {
			Method meth = null;
			try {
				meth = this.readMethod();
			} catch (Socks5Exception e) {
				continue;
			}
			meths.add(meth);
		}
		out.write(meths.size());
		for (Method meth : meths) {
			out.write(UnsignedByte.newInstance(meth.byteValue()).intValue());
		}
		ClientMethodSelectionMessage.Params params = 
				new ClientMethodSelectionMessage.Params();
		params.version = ver;
		params.methods = Methods.newInstance(meths);
		params.byteArray = out.toByteArray();
		return new ClientMethodSelectionMessage(params);		
	}

}
