package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

abstract class CommandWorkerFactory {

	private static final class BindCommandWorkerFactory 
		extends CommandWorkerFactory {
		
		public BindCommandWorkerFactory() {
			super(Command.BIND);
		}
		
		@Override
		public CommandWorker newCommandWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubnegotiationResults methSubnegotiationResults, 
				final Socks5Request socks5Req) {
			return new BindCommandWorker(
					socks5Worker, methSubnegotiationResults, socks5Req);
		}
		
	}
	
	private static final class ConnectCommandWorkerFactory 
		extends CommandWorkerFactory {
		
		public ConnectCommandWorkerFactory() {
			super(Command.CONNECT);
		}
		
		@Override
		public CommandWorker newCommandWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubnegotiationResults methSubnegotiationResults, 
				final Socks5Request socks5Req) {
			return new ConnectCommandWorker(
					socks5Worker, methSubnegotiationResults, socks5Req);
		}
		
	}

	private static final class ResolveCommandWorkerFactory 
		extends CommandWorkerFactory {
		
		public ResolveCommandWorkerFactory() {
			super(Command.RESOLVE);
		}
		
		@Override
		public CommandWorker newCommandWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubnegotiationResults methSubnegotiationResults, 
				final Socks5Request socks5Req) {
			return new ResolveCommandWorker(
					socks5Worker, methSubnegotiationResults, socks5Req);
		}
		
	}

	private static final class UdpAssociateCommandWorkerFactory 
		extends CommandWorkerFactory {
		
		public UdpAssociateCommandWorkerFactory() {
			super(Command.UDP_ASSOCIATE);
		}
		
		@Override
		public CommandWorker newCommandWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubnegotiationResults methSubnegotiationResults, 
				final Socks5Request socks5Req) {
			return new UdpAssociateCommandWorker(
					socks5Worker, methSubnegotiationResults, socks5Req);
		}
		
	}
	
	private static final Map<Command, CommandWorkerFactory> COMMAND_WORKER_FACTORY_MAP;
	
	static {
		COMMAND_WORKER_FACTORY_MAP = 
				new HashMap<Command, CommandWorkerFactory>();
		CommandWorkerFactory bindCommandWorkerFactory = 
				new BindCommandWorkerFactory();
		COMMAND_WORKER_FACTORY_MAP.put(
				bindCommandWorkerFactory.getCommand(), 
				bindCommandWorkerFactory);
		CommandWorkerFactory connectCommandWorkerFactory =
				new ConnectCommandWorkerFactory();
		COMMAND_WORKER_FACTORY_MAP.put(
				connectCommandWorkerFactory.getCommand(), 
				connectCommandWorkerFactory);
		CommandWorkerFactory resolveCommandWorkerFactory =
				new ResolveCommandWorkerFactory();
		COMMAND_WORKER_FACTORY_MAP.put(
				resolveCommandWorkerFactory.getCommand(), 
				resolveCommandWorkerFactory);
		CommandWorkerFactory udpAssociateCommandWorkerFactory =
				new UdpAssociateCommandWorkerFactory();
		COMMAND_WORKER_FACTORY_MAP.put(
				udpAssociateCommandWorkerFactory.getCommand(), 
				udpAssociateCommandWorkerFactory);
	}
	
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
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public abstract CommandWorker newCommandWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubnegotiationResults methSubnegotiationResults, 
			final Socks5Request socks5Req);
	
}
