package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public class Socks5RequestWorkerContext extends Socks5WorkerContext {

	private final MethodEncapsulation methodEncapsulation;
	private final Socks5Request socks5Request;
	
	public Socks5RequestWorkerContext(final Socks5RequestWorkerContext other) {
		super(other);
		this.methodEncapsulation = other.methodEncapsulation;
		this.socks5Request = other.socks5Request; 
	}
	
	public Socks5RequestWorkerContext(
			final Socks5WorkerContext context,
			final MethodEncapsulation methEncapsulation,
			final Socks5Request socks5Req) {
		super(context);
		Objects.requireNonNull(methEncapsulation);
		Objects.requireNonNull(socks5Req);
		this.methodEncapsulation = methEncapsulation;
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
	
	public final MethodEncapsulation getMethodEncapsulation() {
		return this.methodEncapsulation;
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
		return this.methodEncapsulation.getDatagramSocket(
				clientFacingDatagramSck);
	}
	
}
