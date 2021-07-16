package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.util.Objects;

import jargyle.net.socks.transport.v5.Command;

public class Socks5RequestWorker {

	private final Socks5RequestWorkerContext socks5RequestWorkerContext;
	private final String value;
	
	public Socks5RequestWorker(
			final Socks5RequestWorkerContext context, final String val) {
		Objects.requireNonNull(context);
		this.socks5RequestWorkerContext = context;
		this.value = val;
	}
	
	protected final Socks5RequestWorkerContext getSocks5RequestWorkerContext() {
		return this.socks5RequestWorkerContext;
	}
	
	protected final String getValue() {
		return this.value;
	}
	
	public void run() throws IOException {
		Command command = this.socks5RequestWorkerContext.getCommand();
		CommandWorkerFactory commandWorkerFactory = 
				CommandWorkerFactory.valueOfCommand(command);
		CommandWorkerContext commandWorkerContext = new CommandWorkerContext(
				this.socks5RequestWorkerContext);
		CommandWorker commandWorker = commandWorkerFactory.newCommandWorker(
				commandWorkerContext);
		commandWorker.run();		
	}
	
}
