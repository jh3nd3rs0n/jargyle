package jargyle.net.socks.server;

import java.net.Socket;

import jargyle.net.NetObjectFactory;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public class WorkerContext {

	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private Socket clientSocket;
	private final Configuration configuration;
	private final NetObjectFactory externalNetObjectFactory;
	
	public WorkerContext(
			final Socket clientSock,
			final Configuration config,
			final NetObjectFactory extNetObjectFactory,
			final DtlsDatagramSocketFactory clientDtlsDatagramSockFactory) {
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.externalNetObjectFactory = extNetObjectFactory;
	}
	
	public WorkerContext(final WorkerContext other) {
		this.clientDtlsDatagramSocketFactory = 
				other.clientDtlsDatagramSocketFactory;
		this.clientSocket = other.clientSocket;
		this.configuration = other.configuration;
		this.externalNetObjectFactory = other.externalNetObjectFactory;
	}

	public final DtlsDatagramSocketFactory getClientDtlsDatagramSocketFactory() {
		return this.clientDtlsDatagramSocketFactory;
	}

	public final Socket getClientSocket() {
		return this.clientSocket;
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}

	public final NetObjectFactory getExternalNetObjectFactory() {
		return this.externalNetObjectFactory;
	}
	
	public final Settings getSettings() {
		return this.configuration.getSettings();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientSocket=")
			.append(this.clientSocket)
			.append("]");
		return builder.toString();
	}
	
}
