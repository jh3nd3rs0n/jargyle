package com.github.jh3nd3rs0n.jargyle.net.ssl;

import java.io.IOException;
import java.net.DatagramSocket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

final class DefaultDtlsDatagramSocketFactory extends DtlsDatagramSocketFactory {

	private final SSLContext dtlsContext;
	
	public DefaultDtlsDatagramSocketFactory(final SSLContext dtlsCntxt) {
		this.dtlsContext = dtlsCntxt;
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket, 
			final String peerHost, 
			final int peerPort) throws IOException {
		SSLEngine sslEngine = this.dtlsContext.createSSLEngine(
				peerHost, peerPort);
		return new DtlsDatagramSocket(datagramSocket, sslEngine);
	}

}
