package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;

public final class ClientMethodSelectionMessageInputHelper {
	
	public static ClientMethodSelectionMessage readClientMethodSelectionMessageFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = VersionInputHelper.readVersionFrom(in);
		out.write(UnsignedByte.valueOf(ver.byteValue()).intValue());
		UnsignedByte methodCount = UnsignedByteInputHelper.readUnsignedByteFrom(
				in);
		List<Method> meths = new ArrayList<Method>();
		for (int i = 0; i < methodCount.intValue(); i++) {
			Method meth = null;
			try {
				meth = MethodInputHelper.readMethodFrom(in);
			} catch (Socks5Exception e) {
				continue;
			}
			meths.add(meth);
		}
		out.write(meths.size());
		for (Method meth : meths) {
			out.write(UnsignedByte.valueOf(meth.byteValue()).intValue());
		}
		ClientMethodSelectionMessage.Params params = 
				new ClientMethodSelectionMessage.Params();
		params.version = ver;
		params.methods = Methods.of(meths);
		params.byteArray = out.toByteArray();
		return new ClientMethodSelectionMessage(params);		
	}

	private ClientMethodSelectionMessageInputHelper() { }
	
}
