package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;

import com.github.jh3nd3rs0n.jargyle.net.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.net.ssl.DtlsDatagramSocketFactory;

public class WorkerContext {

	private final Optional<DtlsDatagramSocketFactory> clientFacingDtlsDatagramSocketFactory;
	private Socket clientFacingSocket;
	private final Configuration configuration;
	private final NetObjectFactory netObjectFactory;
	
	public WorkerContext(
			final Socket clientFacingSock,
			final Configuration config,
			final NetObjectFactory netObjFactory,
			final Optional<DtlsDatagramSocketFactory> clientFacingDtlsDatagramSockFactory) {
		Objects.requireNonNull(clientFacingSock);
		Objects.requireNonNull(config);
		Objects.requireNonNull(netObjFactory);
		Objects.requireNonNull(clientFacingDtlsDatagramSockFactory);
		this.clientFacingDtlsDatagramSocketFactory = clientFacingDtlsDatagramSockFactory;
		this.clientFacingSocket = clientFacingSock;
		this.configuration = config;
		this.netObjectFactory = netObjFactory;
	}
	
	public WorkerContext(final WorkerContext other) {
		this.clientFacingDtlsDatagramSocketFactory = 
				other.clientFacingDtlsDatagramSocketFactory;
		this.clientFacingSocket = other.clientFacingSocket;
		this.configuration = other.configuration;
		this.netObjectFactory = other.netObjectFactory;
	}

	public final Optional<DtlsDatagramSocketFactory> getClientFacingDtlsDatagramSocketFactory() {
		return this.clientFacingDtlsDatagramSocketFactory;
	}

	public final Socket getClientFacingSocket() {
		return this.clientFacingSocket;
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}

	public final NetObjectFactory getNetObjectFactory() {
		return this.netObjectFactory;
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
