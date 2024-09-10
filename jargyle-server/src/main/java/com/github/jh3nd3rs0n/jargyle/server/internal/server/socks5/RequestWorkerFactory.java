package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;

abstract class RequestWorkerFactory {

	private static final class BindRequestWorkerFactory
		extends RequestWorkerFactory {
		
		public BindRequestWorkerFactory() {
			super(Command.BIND);
		}
		
		@Override
		public RequestWorker newRequestWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubNegotiationResults methSubNegotiationResults, 
				final Request req) {
			return new BindRequestWorker(
					socks5Worker, methSubNegotiationResults, req);
		}
		
	}
	
	private static final class ConnectRequestWorkerFactory
		extends RequestWorkerFactory {
		
		public ConnectRequestWorkerFactory() {
			super(Command.CONNECT);
		}
		
		@Override
		public RequestWorker newRequestWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubNegotiationResults methSubNegotiationResults, 
				final Request req) {
			return new ConnectRequestWorker(
					socks5Worker, methSubNegotiationResults, req);
		}
		
	}

	private static final class ResolveRequestWorkerFactory
		extends RequestWorkerFactory {
		
		public ResolveRequestWorkerFactory() {
			super(Command.RESOLVE);
		}
		
		@Override
		public RequestWorker newRequestWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubNegotiationResults methSubNegotiationResults, 
				final Request req) {
			return new ResolveRequestWorker(
					socks5Worker, methSubNegotiationResults, req);
		}
		
	}

	private static final class UdpAssociateRequestWorkerFactory
		extends RequestWorkerFactory {
		
		public UdpAssociateRequestWorkerFactory() {
			super(Command.UDP_ASSOCIATE);
		}
		
		@Override
		public RequestWorker newRequestWorker(
				final Socks5Worker socks5Worker, 
				final MethodSubNegotiationResults methSubNegotiationResults, 
				final Request req) {
			return new UdpAssociateRequestWorker(
					socks5Worker, methSubNegotiationResults, req);
		}
		
	}
	
	private static final Map<Command, RequestWorkerFactory> REQUEST_WORKER_FACTORY_MAP;
	
	static {
		REQUEST_WORKER_FACTORY_MAP =
				new HashMap<Command, RequestWorkerFactory>();
		RequestWorkerFactory bindRequestWorkerFactory =
				new BindRequestWorkerFactory();
		REQUEST_WORKER_FACTORY_MAP.put(
				bindRequestWorkerFactory.getCommand(),
				bindRequestWorkerFactory);
		RequestWorkerFactory connectRequestWorkerFactory =
				new ConnectRequestWorkerFactory();
		REQUEST_WORKER_FACTORY_MAP.put(
				connectRequestWorkerFactory.getCommand(),
				connectRequestWorkerFactory);
		RequestWorkerFactory resolveRequestWorkerFactory =
				new ResolveRequestWorkerFactory();
		REQUEST_WORKER_FACTORY_MAP.put(
				resolveRequestWorkerFactory.getCommand(),
				resolveRequestWorkerFactory);
		RequestWorkerFactory udpAssociateRequestWorkerFactory =
				new UdpAssociateRequestWorkerFactory();
		REQUEST_WORKER_FACTORY_MAP.put(
				udpAssociateRequestWorkerFactory.getCommand(),
				udpAssociateRequestWorkerFactory);
	}
	
	public static RequestWorkerFactory getInstance(final Command cmd) {
		RequestWorkerFactory requestWorkerFactory =
				REQUEST_WORKER_FACTORY_MAP.get(cmd);
		if (requestWorkerFactory != null) {
			return requestWorkerFactory;
		}
		String str = REQUEST_WORKER_FACTORY_MAP.keySet().stream()
				.map(Command::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected command must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				cmd));		
	}
	
	private final Command command;
	
	private RequestWorkerFactory(final Command cmd) {
		this.command = cmd;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public abstract RequestWorker newRequestWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubNegotiationResults methSubNegotiationResults, 
			final Request req);
	
}
