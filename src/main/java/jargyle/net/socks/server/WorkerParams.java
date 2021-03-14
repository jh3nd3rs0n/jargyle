package jargyle.net.socks.server;

import java.net.Socket;

import jargyle.net.NetFactory;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public final class WorkerParams {

	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private final Socket clientSocket;
	private final Configuration configuration;
	private final NetFactory externalNetFactory;
	
	WorkerParams(
			final Socket clientSock,
			final Configuration config,
			final NetFactory extNetFactory,
			final DtlsDatagramSocketFactory clientDtlsDatagramSockFactory) {
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.externalNetFactory = extNetFactory;
	}

	public DtlsDatagramSocketFactory getClientDtlsDatagramSocketFactory() {
		return this.clientDtlsDatagramSocketFactory;
	}

	public Socket getClientSocket() {
		return this.clientSocket;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public NetFactory getExternalNetFactory() {
		return this.externalNetFactory;
	}

}
