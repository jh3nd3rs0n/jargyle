package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;

public class WorkerContext {

	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final Socket clientFacingSocket;
	private final Configuration configuration;
	private final Route route;
	private final Routes routes;
	
	public WorkerContext(
			final Socket clientFacingSock,
			final Configuration config,
			final Route rte,
			final Routes rtes,
			final DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory) {
		Objects.requireNonNull(clientFacingSock);
		Objects.requireNonNull(config);
		Objects.requireNonNull(rte);
		Objects.requireNonNull(rtes);
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.clientFacingSocket = clientFacingSock;
		this.configuration = config;
		this.route = rte;
		this.routes = rtes;		
	}
	
	public WorkerContext(final WorkerContext other) {
		this.clientFacingDtlsDatagramSocketFactory = 
				other.clientFacingDtlsDatagramSocketFactory;
		this.clientFacingSocket = other.clientFacingSocket;
		this.configuration = other.configuration;
		this.route = other.route;
		this.routes = Routes.newInstance(other.routes);		
	}

	public final DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
		return this.clientFacingDtlsDatagramSocketFactory;
	}

	public final Socket getClientFacingSocket() {
		return this.clientFacingSocket;
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}

	public final Route getRoute() {
		return this.route;
	}
	
	public final Routes getRoutes() {
		return this.routes;
	}
	
	public final Settings getSettings() {
		return this.configuration.getSettings();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientFacingSocket=")
			.append(this.clientFacingSocket)
			.append("]");
		return builder.toString();
	}
	
	public final void writeThenFlush(final byte[] b) throws IOException {
		OutputStream clientFacingOutputStream = 
				this.clientFacingSocket.getOutputStream();
		clientFacingOutputStream.write(b);
		clientFacingOutputStream.flush();
	}
	
}
