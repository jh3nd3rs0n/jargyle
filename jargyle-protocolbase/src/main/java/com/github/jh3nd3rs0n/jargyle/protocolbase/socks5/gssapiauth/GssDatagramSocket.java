package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauth;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

import com.github.jh3nd3rs0n.jargyle.internal.net.FilterDatagramSocket;

final class GssDatagramSocket extends FilterDatagramSocket {

	private final GSSContext gssContext;
	private final MessageProp messageProp;
	private final int wrapSizeLimit;
	
	public GssDatagramSocket(
			final DatagramSocket datagramSock,
			final GSSContext context, 
			final MessageProp prop) throws SocketException {
		super(datagramSock);
		MessageProp prp = null;
		int sizeLimit = Message.MAX_TOKEN_LENGTH;
		if (prop != null) {
			prp = new MessageProp(prop.getQOP(), prop.getPrivacy());
			try {
				sizeLimit = context.getWrapSizeLimit(
						prop.getQOP(), 
						prop.getPrivacy(), 
						Message.MAX_TOKEN_LENGTH);
			} catch (GSSException e) {
				throw new AssertionError(e);
			}
		}
		this.gssContext = context;
		this.messageProp = prp;
		this.wrapSizeLimit = sizeLimit;		
	}
	
	@Override
	public void close() {
		try {
			this.gssContext.dispose();
		} catch (GSSException e) {
			throw new UncheckedIOException(new IOException(e));
		}
		super.close();
	}
	
	@Override
	public DatagramChannel getChannel() {
		throw new UnsupportedOperationException();
	}
	
	public GSSContext getGSSContext() {
		return this.gssContext;
	}

	public MessageProp getMessageProp() {
		if (this.messageProp == null) {
			return this.messageProp;
		}
		return new MessageProp(
				this.messageProp.getQOP(), this.messageProp.getPrivacy());
	}
	
	@Override
	public synchronized void receive(DatagramPacket p) throws IOException {
		super.receive(p);
		if (this.messageProp != null) {
			byte[] data = Arrays.copyOfRange(
					p.getData(), p.getOffset(), p.getLength());
			Message message = Message.newInstance(data);
			byte[] token = message.getToken();
			MessageProp prop = new MessageProp(0, false);
			try {
				token = this.gssContext.unwrap(token, 0, token.length, prop);
			} catch (GSSException e) {
				throw new IOException(e);
			}
			p.setData(token, 0, token.length);
			p.setLength(token.length);
		}
	}
	
	@Override
	public void send(DatagramPacket p) throws IOException {
		if (this.messageProp != null) {
			byte[] data = Arrays.copyOfRange(
					p.getData(), p.getOffset(), p.getLength());
			int dataLength = data.length;
			if (dataLength > this.wrapSizeLimit) {
				dataLength = this.wrapSizeLimit;
			}
			byte[] token;
			MessageProp prop = new MessageProp(
					this.messageProp.getQOP(), 
					this.messageProp.getPrivacy());
			try {
				token = this.gssContext.wrap(data, 0, dataLength, prop);
			} catch (GSSException e) {
				throw new IOException(e);
			}
			Message message = Message.newInstance(
					MessageType.ENCAPSULATED_USER_DATA, 
					token);
			byte[] messageBytes = message.toByteArray();
			p.setData(messageBytes, 0, messageBytes.length);
			p.setLength(messageBytes.length);			
		}
		super.send(p);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", gssContext=")
			.append(this.gssContext)
			.append(", messageProp=")
			.append(this.messageProp)
			.append("]");
		return builder.toString();
	}

}
