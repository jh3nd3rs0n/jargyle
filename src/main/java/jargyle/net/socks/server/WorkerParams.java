package jargyle.net.socks.server;

import java.net.Socket;

import jargyle.net.NetObjectFactory;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public final class WorkerParams {

	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private final Socket clientSocket;
	private final Configuration configuration;
	private final NetObjectFactory externalNetObjectFactory;
	
	WorkerParams(
			final Socket clientSock,
			final Configuration config,
			final NetObjectFactory extNetObjectFactory,
			final DtlsDatagramSocketFactory clientDtlsDatagramSockFactory) {
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.externalNetObjectFactory = extNetObjectFactory;
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

	public NetObjectFactory getExternalNetObjectFactory() {
		return this.externalNetObjectFactory;
	}

}
