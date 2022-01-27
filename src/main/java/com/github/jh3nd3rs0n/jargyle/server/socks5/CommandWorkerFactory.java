package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;

public enum CommandWorkerFactory {

	BIND_COMMAND_WORKER_FACTORY(Command.BIND) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new BindCommandWorker(context);
		}
		
	},
	
	CONNECT_COMMAND_WORKER_FACTORY(Command.CONNECT) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new ConnectCommandWorker(context);
		}
		
	},
	
	RESOLVE_COMMAND_WORKER_FACTORY(Command.RESOLVE) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new ResolveCommandWorker(context);
		}
		
	},
	
	UDP_ASSOCIATE_COMMAND_WORKER_FACTORY(Command.UDP_ASSOCIATE) {
		
		@Override
		public CommandWorker newCommandWorker(
				final CommandWorkerContext context) {
			return new UdpAssociateCommandWorker(context);
		}
	};
	
	public static CommandWorkerFactory valueOfCommand(final Command cmd) {
		for (CommandWorkerFactory value : CommandWorkerFactory.values()) {
			if (value.commandValue().equals(cmd)) {
				return value;
			}
		}
		String str = Arrays.stream(CommandWorkerFactory.values())
				.map(CommandWorkerFactory::commandValue)
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
	}
	
	public Command commandValue() {
		return this.command;
	}
	
	public abstract CommandWorker newCommandWorker(
			final CommandWorkerContext context);
	
}
