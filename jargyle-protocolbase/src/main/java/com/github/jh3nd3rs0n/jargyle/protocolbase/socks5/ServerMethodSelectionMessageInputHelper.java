package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class ServerMethodSelectionMessageInputHelper {
	
	public static ServerMethodSelectionMessage readServerMethodSelectionMessageFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = VersionInputHelper.readVersionFrom(in);
		out.write(UnsignedByte.valueOf(ver.byteValue()).intValue());
		Method meth = MethodInputHelper.readMethodFrom(in);
		out.write(UnsignedByte.valueOf(meth.byteValue()).intValue());
		ServerMethodSelectionMessage.Params params = 
				new ServerMethodSelectionMessage.Params();
		params.version = ver;
		params.method = meth;
		params.byteArray = out.toByteArray();
		return new ServerMethodSelectionMessage(params);		
	}
	
	private ServerMethodSelectionMessageInputHelper() { }

}
