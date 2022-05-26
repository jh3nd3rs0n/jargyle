package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.IOException;
import java.net.DatagramSocket;

import javax.net.ssl.SSLContext;

public abstract class DtlsDatagramSocketFactory {

	public static DtlsDatagramSocketFactory newInstance(
			final SSLContext dtlsContext) {
		return new DefaultDtlsDatagramSocketFactory(dtlsContext);
	}
	
	public abstract DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException;
	
}
