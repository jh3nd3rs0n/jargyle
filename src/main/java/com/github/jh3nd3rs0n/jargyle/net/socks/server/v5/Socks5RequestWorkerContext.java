package com.github.jh3nd3rs0n.jargyle.net.socks.server.v5;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Command;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Socks5Request;

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
	
}
