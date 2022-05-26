package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.IOException;
import java.net.DatagramSocket;

import javax.net.ssl.SSLContext;

final class DefaultDtlsDatagramSocketFactory extends DtlsDatagramSocketFactory {

	private final SSLContext dtlsContext;
	
	public DefaultDtlsDatagramSocketFactory(final SSLContext dtlsCntxt) {
		this.dtlsContext = dtlsCntxt;
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		return new DtlsDatagramSocket(datagramSocket, this.dtlsContext);
	}

}
