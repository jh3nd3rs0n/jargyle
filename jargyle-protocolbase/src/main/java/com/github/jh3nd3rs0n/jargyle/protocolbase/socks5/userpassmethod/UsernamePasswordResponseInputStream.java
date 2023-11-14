package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class UsernamePasswordResponseInputStream 
	extends UsernamePasswordMessageInputStream {

	public UsernamePasswordResponseInputStream(final InputStream in) {
		super(in);
	}
	
	public UsernamePasswordResponse readUsernamePasswordResponse() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = this.readVersion();
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		UnsignedByte status = this.readUnsignedByte(); 
		out.write(status.intValue());
		UsernamePasswordResponse.Params params = 
				new UsernamePasswordResponse.Params();
		params.version = ver;
		params.status = status.byteValue();
		params.byteArray = out.toByteArray();
		return new UsernamePasswordResponse(params);		
	}

}
