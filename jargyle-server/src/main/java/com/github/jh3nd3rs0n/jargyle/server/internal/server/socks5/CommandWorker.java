package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;

abstract class CommandWorker {

	private final CommandWorkerContext commandWorkerContext;
	
	public CommandWorker(final CommandWorkerContext context) {
		this.commandWorkerContext = context;
	}

	public abstract void run() throws IOException;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [commandWorkerContext=")
			.append(this.commandWorkerContext)
			.append("]");
		return builder.toString();
	}
	
}
