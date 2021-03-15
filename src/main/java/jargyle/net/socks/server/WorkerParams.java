package jargyle.net.socks.server;

import java.net.Socket;

import jargyle.net.NetObjectFactoryFactory;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public final class WorkerParams {

	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private final Socket clientSocket;
	private final Configuration configuration;
	private final NetObjectFactoryFactory externalNetObjectFactoryFactory;
	
	WorkerParams(
			final Socket clientSock,
			final Configuration config,
			final NetObjectFactoryFactory extNetObjectFactoryFactory,
			final DtlsDatagramSocketFactory clientDtlsDatagramSockFactory) {
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.externalNetObjectFactoryFactory = extNetObjectFactoryFactory;
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

	public NetObjectFactoryFactory getExternalNetObjectFactoryFactory() {
		return this.externalNetObjectFactoryFactory;
	}

}
