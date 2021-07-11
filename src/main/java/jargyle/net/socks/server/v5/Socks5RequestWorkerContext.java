package jargyle.net.socks.server.v5;

import jargyle.net.socks.transport.v5.Command;
import jargyle.net.socks.transport.v5.Socks5Request;

public class Socks5RequestWorkerContext extends Socks5WorkerContext {

	private final AuthResultSockets authResultSockets;
	private final Socks5Request socks5Request;
	
	public Socks5RequestWorkerContext(final Socks5RequestWorkerContext other) {
		super(other);
		this.authResultSockets = other.authResultSockets;
		this.socks5Request = other.socks5Request; 
	}
	
	public Socks5RequestWorkerContext(
			final Socks5WorkerContext context, 
			final AuthResultSockets authResultSocks, 
			final Socks5Request socks5Req) {
		super(context);
		this.authResultSockets = authResultSocks;
		this.socks5Request = socks5Req;
	}
	
	public final AuthResultSockets getAuthResultSockets() {
		return this.authResultSockets;
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
	
	public final Socks5Request getSocks5Request() {
		return this.socks5Request;
	}
	
}
