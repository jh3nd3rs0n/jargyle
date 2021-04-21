package jargyle.net.socks.server.v5;

import jargyle.net.socks.transport.v5.Command;
import jargyle.net.socks.transport.v5.Socks5Request;

final class CommandWorkerContext extends Socks5WorkerContext {

	private final Socks5Request socks5Request;
	
	public CommandWorkerContext(
			final Socks5WorkerContext context,
			final Socks5Request socks5Req) {
		super(context);
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
	
	public final Socks5Request getSocks5Request() {
		return this.socks5Request;
	}
	
}
