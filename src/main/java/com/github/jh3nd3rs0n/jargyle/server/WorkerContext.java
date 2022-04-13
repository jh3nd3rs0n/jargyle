package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;

public class WorkerContext {

	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private Socket clientFacingSocket;
	private final Configuration configuration;
	private Route route;
	private final Routes routes;
	private final WorkerContext workerContext;
	
	WorkerContext(
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
		this.workerContext = null;
	}
	
	protected WorkerContext(final WorkerContext context) {
		Objects.requireNonNull(context);
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientFacingSocket = null;
		this.configuration = null;
		this.route = null;
		this.routes = null;
		this.workerContext = context;
	}

	public final DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
		if (this.workerContext != null) {
			return this.workerContext.getClientFacingDtlsDatagramSocketFactory();
		}
		return this.clientFacingDtlsDatagramSocketFactory;
	}

	public final Socket getClientFacingSocket() {
		if (this.workerContext != null) {
			return this.workerContext.getClientFacingSocket();
		}
		return this.clientFacingSocket;
	}

	public final Configuration getConfiguration() {
		if (this.workerContext != null) {
			return this.workerContext.getConfiguration();
		}
		return this.configuration;
	}

	public final Route getRoute() {
		if (this.workerContext != null) {
			return this.workerContext.getRoute();
		}
		return this.route;
	}
	
	public final Routes getRoutes() {
		if (this.workerContext != null) {
			return this.workerContext.getRoutes();
		}
		return this.routes;
	}
	
	public final Settings getSettings() {
		if (this.workerContext != null) {
			return this.workerContext.getSettings();
		}
		return this.configuration.getSettings();
	}
	
	public final void setClientFacingSocket(final Socket clientFacingSock) {
		if (this.workerContext != null) {
			this.workerContext.setClientFacingSocket(clientFacingSock);
			return;
		}
		this.clientFacingSocket = Objects.requireNonNull(clientFacingSock);
	}
	
	public final void setRoute(final Route rte) {
		if (this.workerContext != null) {
			this.workerContext.setRoute(rte);
			return;
		}
		this.route = Objects.requireNonNull(rte);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getClientFacingSocket()=")
			.append(this.getClientFacingSocket())
			.append("]");
		return builder.toString();
	}
	
	public final void writeThenFlush(final byte[] b) throws IOException {
		if (this.workerContext != null) {
			this.workerContext.writeThenFlush(b);
			return;
		}
		OutputStream clientFacingOutputStream = 
				this.clientFacingSocket.getOutputStream();
		clientFacingOutputStream.write(b);
		clientFacingOutputStream.flush();
	}
	
}
