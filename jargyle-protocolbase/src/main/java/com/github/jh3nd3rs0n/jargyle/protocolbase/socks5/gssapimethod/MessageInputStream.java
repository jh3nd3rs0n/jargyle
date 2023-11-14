package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5InputStream;

public final class MessageInputStream extends Socks5InputStream {

	public MessageInputStream(final InputStream in) {
		super(in);
	}
	
	public Message readMessage() throws IOException {
		int tknStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = this.readVersion();
		tknStartIndex++;
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		MessageType mType = this.readMessageType();
		tknStartIndex++;
		out.write(UnsignedByte.newInstance(mType.byteValue()).intValue());
		if (mType.equals(MessageType.ABORT)) {
			tknStartIndex++;
		} else {
			UnsignedShort len = this.readUnsignedShort();
			byte[] bytes = len.toByteArray();
			tknStartIndex += bytes.length;
			out.write(bytes);
			bytes = new byte[len.intValue()];
			int bytesRead = this.in.read(bytes);
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
	
	private MessageType readMessageType() throws IOException {
		UnsignedByte b = this.readUnsignedByte();
		MessageType messageType = null;
		try {
			messageType = MessageType.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return messageType;		
	}
	
	private Version readVersion() throws IOException {
		UnsignedByte b = this.readUnsignedByte();
		Version version = null;
		try {
			version = Version.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return version;		
	}

}
