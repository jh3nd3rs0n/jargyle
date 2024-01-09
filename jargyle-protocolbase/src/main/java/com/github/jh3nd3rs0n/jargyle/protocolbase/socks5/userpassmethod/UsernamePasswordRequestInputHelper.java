package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;

public final class UsernamePasswordRequestInputHelper {
	
	public static UsernamePasswordRequest readUsernamePasswordRequestFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = VersionInputHelper.readVersionFrom(in);
		out.write(UnsignedByte.newInstanceOf(ver.byteValue()).intValue());
		UnsignedByte ulen = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		out.write(ulen.intValue());
		byte[] bytes = new byte[ulen.intValue()];
		int bytesRead = in.read(bytes);
		if (bytesRead != ulen.intValue()) {
			throw new EOFException(String.format(
					"expected username length is %s byte(s). "
					+ "actual username length is %s byte(s)", 
					ulen.intValue(), bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		String uname = new String(bytes);
		out.write(bytes);
		UnsignedByte plen = UnsignedByteInputHelper.readUnsignedByteFrom(in); 
		out.write(plen.intValue());
		bytes = new byte[plen.intValue()];
		bytesRead = in.read(bytes);
		if (bytesRead != plen.intValue()) {
			throw new EOFException(String.format(
					"expected password length is %s byte(s). "
					+ "actual password length is %s byte(s)", 
					plen.intValue(), bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		Reader reader = new InputStreamReader(new ByteArrayInputStream(
				bytes));
		int passwdLength = 0;
		char[] passwd = new char[passwdLength];
		int ch = -1;
		do {
			try {
				ch = reader.read();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			if (ch != -1) {
				passwd = Arrays.copyOf(passwd, ++passwdLength);
				passwd[passwdLength - 1] = (char) ch;
			}
		} while (ch != -1);
		out.write(bytes);
		UsernamePasswordRequest.Params params = 
				new UsernamePasswordRequest.Params();
		params.version = ver;
		params.username = uname;
		params.password = passwd;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordRequest(params);		
	}

	private UsernamePasswordRequestInputHelper() { }
	
}
