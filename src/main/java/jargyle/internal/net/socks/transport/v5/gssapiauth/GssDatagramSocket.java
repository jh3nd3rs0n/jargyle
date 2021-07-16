package jargyle.internal.net.socks.transport.v5.gssapiauth;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Optional;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

import jargyle.internal.net.FilterDatagramSocket;

public final class GssDatagramSocket extends FilterDatagramSocket {

	private final GSSContext gssContext;
	private final Optional<MessageProp> messageProp;
	private final int wrapSizeLimit;
	
	public GssDatagramSocket(
			final DatagramSocket datagramSock,
			final GSSContext context, 
			final Optional<MessageProp> prop) throws SocketException {
		super(datagramSock);
		Optional<MessageProp> prp = Optional.empty();
		int sizeLimit = Message.MAX_TOKEN_LENGTH;
		if (prop.isPresent()) {
			prp = Optional.of(new MessageProp(
					prop.get().getQOP(), prop.get().getPrivacy()));
			try {
				sizeLimit = context.getWrapSizeLimit(
						prop.get().getQOP(), 
						prop.get().getPrivacy(), 
						Message.MAX_TOKEN_LENGTH);
			} catch (GSSException e) {
				throw new AssertionError(e);
			}
		}
		this.gssContext = context;
		this.messageProp = prp;
		this.wrapSizeLimit = sizeLimit;		
	}

	public GSSContext getGSSContext() {
		return this.gssContext;
	}

	public Optional<MessageProp> getMessageProp() {
		if (!this.messageProp.isPresent()) {
			return this.messageProp;
		}
		return Optional.of(new MessageProp(
				this.messageProp.get().getQOP(), 
				this.messageProp.get().getPrivacy()));
	}
	
	@Override
	public synchronized void receive(DatagramPacket p) throws IOException {
		super.receive(p);
		if (this.messageProp.isPresent()) {
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
	}
	
	@Override
	public void send(DatagramPacket p) throws IOException {
		if (this.messageProp.isPresent()) {
			byte[] data = p.getData();
			int dataLength = data.length;
			if (dataLength > this.wrapSizeLimit) {
				dataLength = this.wrapSizeLimit;
			}
			byte[] token;
			MessageProp prop = new MessageProp(
					this.messageProp.get().getQOP(), 
					this.messageProp.get().getPrivacy());
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
