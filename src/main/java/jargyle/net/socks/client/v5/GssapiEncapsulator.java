package jargyle.net.socks.client.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.MessageProp;

import jargyle.net.socks.transport.v5.gssapiauth.GssDatagramSocket;
import jargyle.net.socks.transport.v5.gssapiauth.GssSocket;

final class GssapiEncapsulator extends Encapsulator {

	private final GSSContext gssContext;
	private final MessageProp messageProp;
	
	public GssapiEncapsulator(
			final GSSContext context, final MessageProp prop) {
		MessageProp prp = null;
		if (prop != null) {
			prp = new MessageProp(prop.getQOP(), prop.getPrivacy());
		}
		this.gssContext = context;
		this.messageProp = prp;
	}
	
	@Override
	public DatagramSocket encapsulate(
			final DatagramSocket datagramSocket) throws SocketException {
		return new GssDatagramSocket(
				datagramSocket, this.gssContext, this.messageProp);
	}

	@Override
	public Socket encapsulate(final Socket socket) {
		return new GssSocket(socket, this.gssContext, this.messageProp);
	}

}
