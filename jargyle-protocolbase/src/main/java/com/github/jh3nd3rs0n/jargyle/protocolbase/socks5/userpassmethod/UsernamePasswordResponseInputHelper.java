package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;

public final class UsernamePasswordResponseInputHelper {
	
	public static UsernamePasswordResponse readUsernamePasswordResponseFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = VersionInputHelper.readVersionFrom(in);
		out.write(UnsignedByte.newInstanceOf(ver.byteValue()).intValue());
		UnsignedByte status = UnsignedByteInputHelper.readUnsignedByteFrom(in); 
		out.write(status.intValue());
		UsernamePasswordResponse.Params params = 
				new UsernamePasswordResponse.Params();
		params.version = ver;
		params.status = status.byteValue();
		params.byteArray = out.toByteArray();
		return new UsernamePasswordResponse(params);		
	}
	
	private UsernamePasswordResponseInputHelper() { }
	
}
