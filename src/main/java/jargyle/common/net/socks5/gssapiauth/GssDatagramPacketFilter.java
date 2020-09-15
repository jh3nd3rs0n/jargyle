package jargyle.common.net.socks5.gssapiauth;

import java.io.IOException;
import java.net.DatagramPacket;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

import jargyle.common.net.DatagramPacketFilter;

public final class GssDatagramPacketFilter extends DatagramPacketFilter {

	private final GSSContext gssContext;
	private final MessageProp messageProp;
	private final int wrapSizeLimit;
	
	public GssDatagramPacketFilter(
			final GSSContext context, final MessageProp prop) {
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
	public void filterAfterReceive(final DatagramPacket p) throws IOException {
		if (this.messageProp == null) {
			return;
		}
		byte[] data = p.getData();
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
	
	@Override
	public void filterBeforeSend(final DatagramPacket p) throws IOException {
		if (this.messageProp == null) {
			return;
		}
		byte[] data = p.getData();
		int dataLength = data.length;
		if (dataLength > this.wrapSizeLimit) {
			dataLength = this.wrapSizeLimit;
		}
		byte[] token;
		MessageProp prop = new MessageProp(
				this.messageProp.getQOP(), this.messageProp.getPrivacy());
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

	public GSSContext getGSSContext() {
		return this.gssContext;
	}

	public MessageProp getMessageProp() {
		if (this.messageProp == null) {
			return null;
		}
		return new MessageProp(
				this.messageProp.getQOP(), this.messageProp.getPrivacy());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [gssContext=")
			.append(this.gssContext)
			.append("]");
		return builder.toString();
	}

}
