package jargyle.net.socks.server.v5;

final class CommandWorkerContext extends Socks5WorkerContext {

	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	
	public CommandWorkerContext(
			final Socks5WorkerContext context,
			final String desiredDestinationAddr,
			final int desiredDestinationPrt) {
		super(context);
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
	}
	
	public final String getDesiredDestinationAddress() {
		return this.desiredDestinationAddress;
	}
	
	public final int getDesiredDestinationPort() {
		return this.desiredDestinationPort;
	}
	
}
