package com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.MessageProp;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;

public final class GssapiMethodEncapsulation extends MethodEncapsulation {
	
	private final GSSContext gssContext;
	private final MessageProp messageProp;
	private final Socket socket;
	
	public GssapiMethodEncapsulation(
			final Socket sock, 
			final GSSContext context,
			final MessageProp prop) {
		this.gssContext = context;
		this.messageProp = prop;
		this.socket = new GssSocket(sock, this.gssContext, this.messageProp);
	}
	
	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		return new GssDatagramSocket(
				datagramSocket, this.gssContext, this.messageProp);
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socket=")
			.append(this.socket)
			.append("]");
		return builder.toString();
	}

}
