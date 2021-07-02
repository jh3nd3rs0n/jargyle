package jargyle.net.socks.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import jargyle.net.NetObjectFactory;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public class WorkerContext {

	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private Socket clientSocket;
	private final Configuration configuration;
	private final NetObjectFactory netObjectFactory;
	
	public WorkerContext(
			final Socket clientSock,
			final Configuration config,
			final NetObjectFactory netObjFactory,
			final DtlsDatagramSocketFactory clientDtlsDatagramSockFactory) {
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.netObjectFactory = netObjFactory;
	}
	
	public WorkerContext(final WorkerContext other) {
		this.clientDtlsDatagramSocketFactory = 
				other.clientDtlsDatagramSocketFactory;
		this.clientSocket = other.clientSocket;
		this.configuration = other.configuration;
		this.netObjectFactory = other.netObjectFactory;
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
			.append(" [clientSocket=")
			.append(this.clientSocket)
			.append("]");
		return builder.toString();
	}
	
	public final void writeThenFlush(final byte[] b) throws IOException {
		OutputStream clientOutputStream = this.clientSocket.getOutputStream();
		clientOutputStream.write(b);
		clientOutputStream.flush();
	}
	
}
