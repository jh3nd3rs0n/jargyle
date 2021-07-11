package jargyle.net.socks.client.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.MessageProp;

import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.gssapiauth.GssDatagramSocket;
import jargyle.net.socks.transport.v5.gssapiauth.GssSocket;

final class GssapiAuthResult extends AuthResult {

	private final GSSContext gssContext;
	private final MessageProp messageProp;
	private final Socket socket;
	
	public GssapiAuthResult(
			final GSSContext context, 
			final MessageProp prop,
			final Socket sock) {
		MessageProp prp = null;
		if (prop != null) {
			prp = new MessageProp(prop.getQOP(), prop.getPrivacy());
		}
		this.gssContext = context;
		this.messageProp = prp;
		this.socket = new GssSocket(sock, context, prp);
	}
	
	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws SocketException {
		return new GssDatagramSocket(
				datagramSocket, this.gssContext, this.messageProp);
	}

	@Override
	public Method getMethod() {
		return Method.GSSAPI;
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

}
