package jargyle.net.socks.server.v5;

import jargyle.net.socks.transport.v5.Command;
import jargyle.net.socks.transport.v5.Socks5Request;

public class Socks5RequestWorkerContext extends Socks5WorkerContext {

	private final Encapsulator encapsulator;
	private final Socks5Request socks5Request;
	
	public Socks5RequestWorkerContext(final Socks5RequestWorkerContext other) {
		super(other);
		this.encapsulator = other.encapsulator;
		this.socks5Request = other.socks5Request; 
	}
	
	public Socks5RequestWorkerContext(
			final Socks5WorkerContext context,
			final Encapsulator encpsltr,
			final Socks5Request socks5Req) {
		super(context);
		this.encapsulator = encpsltr;
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
	
	public final Encapsulator getEncapsulator() {
		return this.encapsulator;
	}
	
	public final Socks5Request getSocks5Request() {
		return this.socks5Request;
	}
	
}
