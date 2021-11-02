package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public class Socks5RequestWorkerContext extends Socks5WorkerContext {

	private final MethodSubnegotiationResults methodSubnegotiationResults;
	private final Socks5Request socks5Request;
	
	public Socks5RequestWorkerContext(final Socks5RequestWorkerContext other) {
		super(other);
		this.methodSubnegotiationResults = other.methodSubnegotiationResults;
		this.socks5Request = other.socks5Request; 
	}
	
	public Socks5RequestWorkerContext(
			final Socks5WorkerContext context,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		super(context);
		Objects.requireNonNull(methSubnegotiationResults);
		Objects.requireNonNull(socks5Req);
		this.methodSubnegotiationResults = methSubnegotiationResults;
		this.socks5Request = socks5Req;
	}
	
	public final Command getCommand() {
		return this.socks5Request.getCommand();
	}
	
	public final String getDesiredDestinationAddress() {
		return this.socks5Request.getDesiredDestinationAddress();
	}
	
	public final int getDesiredDestinationPort() {
		return this.socks5Request.getDesiredDestinationPort();
	}
	
	public final MethodSubnegotiationResults getMethodSubnegotiationResults() {
		return this.methodSubnegotiationResults;
	}
	
	public final Socks5Request getSocks5Request() {
		return this.socks5Request;
	}
	
	@Override
	public final DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock, 
			final String clientHost, 
			final int clientPort) throws IOException {
		DatagramSocket clientFacingDatagramSck = 
				super.wrapClientFacingDatagramSocket(
						clientFacingDatagramSock, clientHost, clientPort);
		return this.methodSubnegotiationResults.getDatagramSocket(
				clientFacingDatagramSck);
	}
	
}
