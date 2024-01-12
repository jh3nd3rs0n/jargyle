package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortInputHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

public final class MessageInputHelper {
	
	public static Message readMessageFrom(
			final InputStream in) throws IOException {
		int tknStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = readVersionFrom(in);
		tknStartIndex++;
		out.write(UnsignedByte.valueOf(ver.byteValue()).intValue());
		MessageType mType = readMessageTypeFrom(in);
		tknStartIndex++;
		out.write(UnsignedByte.valueOf(mType.byteValue()).intValue());
		if (mType.equals(MessageType.ABORT)) {
			tknStartIndex++;
		} else {
			UnsignedShort len = UnsignedShortInputHelper.readUnsignedShortFrom(
					in);
			byte[] bytes = len.toByteArray();
			tknStartIndex += bytes.length;
			out.write(bytes);
			bytes = new byte[len.intValue()];
			int bytesRead = in.read(bytes);
			if (bytesRead != len.intValue()) {
				throw new EOFException(String.format(
						"expected token length is %s byte(s). "
						+ "actual token length is %s byte(s)", 
						len.intValue(), bytesRead));				
			}
			bytes = Arrays.copyOf(bytes, bytesRead);
			tknStartIndex++;
			out.write(bytes);
		}
		Message.Params params = new Message.Params();
		params.version = ver;
		params.messageType = mType;
		params.tokenStartIndex = tknStartIndex;
		params.byteArray = out.toByteArray();
		return new Message(params);		
	}
	
	private static MessageType readMessageTypeFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		MessageType messageType = null;
		try {
			messageType = MessageType.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return messageType;		
	}
	
	private static Version readVersionFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		Version version = null;
		try {
			version = Version.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return version;		
	}
	
	private MessageInputHelper() { }

}
