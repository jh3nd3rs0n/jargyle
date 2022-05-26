package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;

abstract class CommandWorkerFactory {

	private static final Map<Command, CommandWorkerFactory> COMMAND_WORKER_FACTORY_MAP =
			new HashMap<Command, CommandWorkerFactory>();
	
	@SuppressWarnings("unused")
	private static final CommandWorkerFactory BIND_COMMAND_WORKER_FACTORY = new CommandWorkerFactory(
			Command.BIND) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new BindCommandWorker(context);
		}
		
	};
	
	@SuppressWarnings("unused")
	private static final CommandWorkerFactory CONNECT_COMMAND_WORKER_FACTORY = new CommandWorkerFactory(
			Command.CONNECT) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new ConnectCommandWorker(context);
		}
		
	};
	
	@SuppressWarnings("unused")
	private static final CommandWorkerFactory RESOLVE_COMMAND_WORKER_FACTORY = new CommandWorkerFactory(
			Command.RESOLVE) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new ResolveCommandWorker(context);
		}
		
	};
	
	@SuppressWarnings("unused")
	private static final CommandWorkerFactory UDP_ASSOCIATE_COMMAND_WORKER_FACTORY = new CommandWorkerFactory(
			Command.UDP_ASSOCIATE) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new UdpAssociateCommandWorker(context);
		}
	};
	
	public static CommandWorkerFactory getInstance(final Command cmd) {
		CommandWorkerFactory commandWorkerFactory = 
				COMMAND_WORKER_FACTORY_MAP.get(cmd);
		if (commandWorkerFactory != null) {
			return commandWorkerFactory;
		}
		String str = COMMAND_WORKER_FACTORY_MAP.keySet().stream()
				.map(Command::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected command must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				cmd));		
	}
	
	private final Command command;
	
	private CommandWorkerFactory(final Command cmd) {
		this.command = cmd;
		COMMAND_WORKER_FACTORY_MAP.put(cmd, this);
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public abstract CommandWorker newCommandWorker(
			final CommandWorkerContext context);
	
}
